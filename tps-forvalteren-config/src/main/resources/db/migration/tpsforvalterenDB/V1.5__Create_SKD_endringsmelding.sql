-----------------
-- T A B L E S --
-----------------
CREATE TABLE T_SKD_ENDRINGSMELDING_GRUPPE (
  SKD_ENDRINGSMELDING_GRUPPE_ID NUMBER(9)    NOT NULL,
  NAVN                          VARCHAR2(50) NOT NULL,
  BESKRIVELSE                   VARCHAR2(200),
  OPPRETTET_DATO                TIMESTAMP    NOT NULL,
  OPPRETTET_AV                  VARCHAR2(20) NOT NULL,
  ENDRET_DATO                   TIMESTAMP    NOT NULL,
  ENDRET_AV                     VARCHAR2(20) NOT NULL
);

CREATE TABLE T_SKD_ENDRINGSMELDING (
  SKD_ENDRINGSMELDING           NUMBER(9)      NOT NULL,
  SKD_ENDRINGSMELDING_GRUPPE_ID NUMBER(9)      NOT NULL,
  ENDRINGSMELDING_JSON          VARCHAR2(4000) NOT NULL,
  OPPRETTET_DATO                TIMESTAMP      NOT NULL,
  OPPRETTET_AV                  VARCHAR2(20)   NOT NULL,
  ENDRET_DATO                   TIMESTAMP      NOT NULL,
  ENDRET_AV                     VARCHAR2(20)   NOT NULL
);

CREATE TABLE T_SKD_ENDRINGSMELDING_LOGG (
  SKD_ENDRINGSMELDING_LOGG_ID    NUMBER(9)      NOT NULL,
  SKD_ENDRINGSMELDING_GRUPPE_REF NUMBER(9)      NOT NULL,
  ENDRINGSMELDING                VARCHAR2(1500) NOT NULL,
  BESKRIVELSE                    VARCHAR2(200)  NOT NULL,
  ENVIRONMENT                    VARCHAR(2)     NOT NULL,
  INNSENDT_DATO                  TIMESTAMP      NOT NULL,
  INNSENDT_AV                    VARCHAR2(20)   NOT NULL
);

---------------------------------------------------
-- P R I M A R Y   K E Y   C O N S T R A I N T S --
---------------------------------------------------
ALTER TABLE T_SKD_ENDRINGSMELDING_GRUPPE
  ADD CONSTRAINT T_MELDINGSGRUPPE_PK PRIMARY KEY (SKD_ENDRINGSMELDING_GRUPPE_ID);
ALTER TABLE T_SKD_ENDRINGSMELDING
  ADD CONSTRAINT T_SKD_ENDRINGSMELDING_PK PRIMARY KEY (SKD_ENDRINGSMELDING);
ALTER TABLE T_SKD_ENDRINGSMELDING_LOGG
  ADD CONSTRAINT T_SKD_ENDRINGSMELDING_LOGG_PK PRIMARY KEY (SKD_ENDRINGSMELDING_LOGG_ID);

---------------------------------------------------
-- F O R E I G N   K E Y   C O N S T R A I N T S --
---------------------------------------------------
ALTER TABLE T_SKD_ENDRINGSMELDING
  ADD CONSTRAINT T_SKD_ENDRINGSMELDING_FK FOREIGN KEY (SKD_ENDRINGSMELDING_GRUPPE_ID) REFERENCES T_SKD_ENDRINGSMELDING_GRUPPE (SKD_ENDRINGSMELDING_GRUPPE_ID);

-----------------------
-- S E Q U E N C E S --
-----------------------
CREATE SEQUENCE T_SKD_MELDINGSGRUPPE_SEQ START WITH 100000000;
CREATE SEQUENCE T_SKD_ENDRINGSMELDING_SEQ START WITH 100000000;
CREATE SEQUENCE T_SKD_ENDRINGSMELDING_LOGG_SEQ START WITH 100000000;

--------------------------
-- M O D I F Y  S I Z E --
--------------------------
ALTER TABLE T_GATEADRESSE MODIFY HUSNUMMER VARCHAR2(5);