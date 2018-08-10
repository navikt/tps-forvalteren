CREATE TABLE T_SKD_MELDINGER_AVSPILLERDATA (
  ID            NUMBER(10) PRIMARY KEY,
  SKD_MELDING   VARCHAR2(1600 CHAR) NOT NULL,
  TESTPERSON_ID NUMBER(9)           NOT NULL,
  GRUPPE_ID     NUMBER(9)           NOT NULL,
  SEKVENSNUMMER NUMBER(10)          NOT NULL,
  AARSAKSKODE   NUMBER(2)           NOT NULL
);

ALTER TABLE T_SKD_MELDINGER_AVSPILLERDATA
  ADD CONSTRAINT GRUPPE_ID_FK FOREIGN KEY (GRUPPE_ID) REFERENCES T_GRUPPE (GRUPPE_ID);

CREATE SEQUENCE T_SKD_AVSPILLERDATA_SEQ START WITH 1000000000;