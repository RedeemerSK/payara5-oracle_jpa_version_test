alter session set "_ORACLE_SCRIPT"=true;

CREATE USER PAYARA_TEST IDENTIFIED BY password DEFAULT TABLESPACE "SYSTEM" TEMPORARY TABLESPACE "TEMP";
ALTER USER PAYARA_TEST QUOTA UNLIMITED ON SYSTEM;
GRANT "CONNECT" TO PAYARA_TEST ;

CREATE TABLE PAYARA_TEST.TEST_ENTITY 
(
  ID CHAR(36) NOT NULL,
  DATA VARCHAR2(128),
  VERSION TIMESTAMP,
  CONSTRAINT TEST_ENTITY_PK PRIMARY KEY (ID) ENABLE 
);
exit;