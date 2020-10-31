FROM payara/server-full:5.201

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
ADD --chown=payara:payara https://repo1.maven.org/maven2/com/oracle/database/jdbc/ojdbc8/12.2.0.1/ojdbc8-12.2.0.1.jar ${PAYARA_DIR}/glassfish/domains/${DOMAIN_NAME}/lib/ext
# Test application WAR
COPY --chown=payara:payara payara5-oracle_jpa_version_test.war ${DEPLOY_DIR}

# Add wait utility
ADD --chown=payara:payara https://github.com/ufoscout/docker-compose-wait/releases/download/2.7.3/wait {HOME_DIR}/wait
RUN chmod +x {HOME_DIR}/wait

CMD {HOME_DIR}/wait && ${SCRIPT_DIR}/entrypoint.sh