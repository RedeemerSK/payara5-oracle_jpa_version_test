FROM azul/zulu-openjdk:11

# Default payara ports to expose
# 4848: admin console
# 9009: debug port (JPDA)
# 8080: http
# 8181: https
EXPOSE 4848 9009 8080 8181

# Payara version (5.183+)
ARG PAYARA_VERSION=5.201
ARG PAYARA_PKG=https://search.maven.org/remotecontent?filepath=fish/payara/distributions/payara/${PAYARA_VERSION}/payara-${PAYARA_VERSION}.zip
ARG PAYARA_SHA1=ea86d69233826b4d35612260ea4e8f81a9b992f2
ARG TINI_VERSION=v0.18.0

# Initialize the configurable environment variables
ENV HOME_DIR=/opt/payara\
    PAYARA_DIR=/opt/payara/appserver\
    SCRIPT_DIR=/opt/payara/scripts\
    CONFIG_DIR=/opt/payara/config\
    DEPLOY_DIR=/opt/payara/deployments\
    PASSWORD_FILE=/opt/payara/passwordFile\
    # Payara Server Domain options
    DOMAIN_NAME=production\
    ADMIN_USER=admin\
    ADMIN_PASSWORD=admin \
    # Utility environment variables
    JVM_ARGS=\
    PAYARA_ARGS=\
    DEPLOY_PROPS=\
    POSTBOOT_COMMANDS=/opt/payara/config/post-boot-commands.asadmin\
    PREBOOT_COMMANDS=/opt/payara/config/pre-boot-commands.asadmin
ENV PATH="${PATH}:${PAYARA_DIR}/bin"

# Create and set the Payara user and working directory owned by the new user
RUN groupadd -g 1000 payara && \
    useradd -u 1000 -M -s /bin/bash -d ${HOME_DIR} payara -g payara && \
    echo payara:payara | chpasswd && \
    mkdir -p ${DEPLOY_DIR} && \
    mkdir -p ${CONFIG_DIR} && \
    mkdir -p ${SCRIPT_DIR} && \
    chown -R payara: ${HOME_DIR} && \
    # Install required packages
    apt-get update && \
    apt-get install -y wget unzip && \
    rm -rf /var/lib/apt/lists/*

# Install tini as minimized init system
RUN wget --no-verbose -O /tini https://github.com/krallin/tini/releases/download/${TINI_VERSION}/tini && \
    wget --no-verbose -O /tini.asc https://github.com/krallin/tini/releases/download/${TINI_VERSION}/tini.asc && \
    gpg --batch --keyserver "hkp://p80.pool.sks-keyservers.net:80" --recv-keys 595E85A6B1B4779EA4DAAEC70B588DFF0527A9B7 && \
    gpg --batch --verify /tini.asc /tini && \
    chmod +x /tini

USER payara
WORKDIR ${HOME_DIR}

# Download and unzip the Payara distribution
RUN wget --no-verbose -O payara.zip ${PAYARA_PKG} && \
    echo "${PAYARA_SHA1} *payara.zip" | sha1sum -c - && \
    unzip -qq payara.zip -d ./ && \
    mv payara*/ appserver && \
    # Configure the password file for configuring Payara
    echo "AS_ADMIN_PASSWORD=\nAS_ADMIN_NEWPASSWORD=${ADMIN_PASSWORD}" > /tmp/tmpfile && \
    echo "AS_ADMIN_PASSWORD=${ADMIN_PASSWORD}" >> ${PASSWORD_FILE} && \
    # Configure the payara domain
    ${PAYARA_DIR}/bin/asadmin --user ${ADMIN_USER} --passwordfile=/tmp/tmpfile change-admin-password --domain_name=${DOMAIN_NAME} && \
    ${PAYARA_DIR}/bin/asadmin --user=${ADMIN_USER} --passwordfile=${PASSWORD_FILE} start-domain ${DOMAIN_NAME} && \
    ${PAYARA_DIR}/bin/asadmin --user=${ADMIN_USER} --passwordfile=${PASSWORD_FILE} enable-secure-admin && \
    for MEMORY_JVM_OPTION in $(${PAYARA_DIR}/bin/asadmin --user=${ADMIN_USER} --passwordfile=${PASSWORD_FILE} list-jvm-options | grep "Xm[sx]"); do\
        ${PAYARA_DIR}/bin/asadmin --user=${ADMIN_USER} --passwordfile=${PASSWORD_FILE} delete-jvm-options $MEMORY_JVM_OPTION;\
    done && \
    # FIXME: when upgrading this container to Java 10+, this needs to be changed to '-XX:+UseContainerSupport' and '-XX:MaxRAMPercentage'
    ${PAYARA_DIR}/bin/asadmin --user=${ADMIN_USER} --passwordfile=${PASSWORD_FILE} create-jvm-options '-XX\:+UnlockExperimentalVMOptions:-XX\:MaxRAMFraction=1' && \
    ${PAYARA_DIR}/bin/asadmin --user=${ADMIN_USER} --passwordfile=${PASSWORD_FILE} set-log-attributes com.sun.enterprise.server.logging.GFFileHandler.logtoFile=false && \
    ${PAYARA_DIR}/bin/asadmin --user=${ADMIN_USER} --passwordfile=${PASSWORD_FILE} stop-domain ${DOMAIN_NAME} && \
    # Cleanup unused files
    rm -rf \
        /tmp/tmpFile \
        payara.zip \
        ${PAYARA_DIR}/glassfish/domains/${DOMAIN_NAME}/osgi-cache \
        ${PAYARA_DIR}/glassfish/domains/${DOMAIN_NAME}/logs \
        ${PAYARA_DIR}/glassfish/domains/domain1

# Copy across docker scripts
COPY --chown=payara:payara bin/*.sh ${SCRIPT_DIR}/
RUN mkdir -p ${SCRIPT_DIR}/init.d && \
    chmod +x ${SCRIPT_DIR}/*
	
ENTRYPOINT ["/tini", "--"]
CMD ${SCRIPT_DIR}/entrypoint.sh

# [payara5-on-java11-jpa-test] Start

# Delete expired certificates from cacerts.jks to not pollute server log
RUN keytool -delete -keystore /opt/payara/appserver/glassfish/domains/production/config/cacerts.jks -storepass changeit -alias "cert_47_staat_der_nederlanden_root_ca___g247" && \
    keytool -delete -keystore /opt/payara/appserver/glassfish/domains/production/config/cacerts.jks -storepass changeit -alias "cert_57_staat_der_nederlanden_root_ca___g257" && \
    keytool -delete -keystore /opt/payara/appserver/glassfish/domains/production/config/cacerts.jks -storepass changeit -alias "keynectisrootca" && \
    keytool -delete -keystore /opt/payara/appserver/glassfish/domains/production/config/cacerts.jks -storepass changeit -alias "keynectisrootca [jdk]" && \
    keytool -delete -keystore /opt/payara/appserver/glassfish/domains/production/config/cacerts.jks -storepass changeit -alias "addtrustclass1ca" && \
    keytool -delete -keystore /opt/payara/appserver/glassfish/domains/production/config/cacerts.jks -storepass changeit -alias "addtrustclass1ca [jdk]" && \
    keytool -delete -keystore /opt/payara/appserver/glassfish/domains/production/config/cacerts.jks -storepass changeit -alias "addtrustqualifiedca" && \
    keytool -delete -keystore /opt/payara/appserver/glassfish/domains/production/config/cacerts.jks -storepass changeit -alias "addtrustqualifiedca [jdk]" && \
    keytool -delete -keystore /opt/payara/appserver/glassfish/domains/production/config/cacerts.jks -storepass changeit -alias "addtrustexternalca" && \
    keytool -delete -keystore /opt/payara/appserver/glassfish/domains/production/config/cacerts.jks -storepass changeit -alias "addtrustexternalca [jdk]" && \
    keytool -delete -keystore /opt/payara/appserver/glassfish/domains/production/config/cacerts.jks -storepass changeit -alias "cert_6_addtrust_external_root6" && \
    keytool -delete -keystore /opt/payara/appserver/glassfish/domains/production/config/cacerts.jks -storepass changeit -alias "equifaxsecureebusinessca1" && \
    keytool -delete -keystore /opt/payara/appserver/glassfish/domains/production/config/cacerts.jks -storepass changeit -alias "equifaxsecureglobalebusinessca1"

# Oracle JDBC driver JAR
COPY --chown=payara:payara custom/ojdbc8.jar ${PAYARA_DIR}/glassfish/domains/${DOMAIN_NAME}/lib
# Test application WAR
COPY --chown=payara:payara custom/payara5-oracle_jpa_version_test.war ${DEPLOY_DIR}

# Add wait utility
ADD --chown=payara:payara https://github.com/ufoscout/docker-compose-wait/releases/download/2.7.3/wait {HOME_DIR}/wait
RUN chmod +x {HOME_DIR}/wait

CMD {HOME_DIR}/wait && ${SCRIPT_DIR}/entrypoint.sh