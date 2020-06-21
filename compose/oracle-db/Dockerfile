FROM store/oracle/database-enterprise:12.2.0.1-slim

ARG SETUP_DIR_=/home/oracle/setup/

COPY --chown=oracle:oinstall _setupDB.sh _setupSchema.sql ${SETUP_DIR_}

RUN chmod 754 ${SETUP_DIR_}/_setupDB.sh && \
    chmod 644 ${SETUP_DIR_}/_setupSchema.sql && \
    mv ${SETUP_DIR_}/setupDB.sh ${SETUP_DIR_}/setupDB.orig.sh && \
    mv ${SETUP_DIR_}/_setupDB.sh ${SETUP_DIR_}/setupDB.sh