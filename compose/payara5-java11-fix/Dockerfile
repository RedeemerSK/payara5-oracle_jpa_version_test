FROM payara/server-full:5.2020.5-jdk11

# Move org.eclipse.persistence.oracle.jar to lib folder (and delete obsolete glassfish-oracle-jdbc-driver-packages.jar) 
RUN mv ${PAYARA_DIR}/glassfish/modules/org.eclipse.persistence.oracle.jar ${PAYARA_DIR}/glassfish/domains/${DOMAIN_NAME}/lib && rm ${PAYARA_DIR}/glassfish/modules/glassfish-oracle-jdbc-driver-packages.jar
# Oracle JDBC driver JAR
ADD --chown=payara:payara https://repo1.maven.org/maven2/com/oracle/database/jdbc/ojdbc8/12.2.0.1/ojdbc8-12.2.0.1.jar ${PAYARA_DIR}/glassfish/domains/${DOMAIN_NAME}/lib
# Test application WAR
COPY --chown=payara:payara payara5-oracle_jpa_version_test.war ${DEPLOY_DIR}

# Add wait utility
ADD --chown=payara:payara https://github.com/ufoscout/docker-compose-wait/releases/download/2.7.3/wait {HOME_DIR}/wait
RUN chmod +x {HOME_DIR}/wait

CMD {HOME_DIR}/wait && ${SCRIPT_DIR}/entrypoint.sh