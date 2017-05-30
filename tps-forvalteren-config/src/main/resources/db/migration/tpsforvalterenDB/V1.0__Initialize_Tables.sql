-----------------
-- T A B L E S --
-----------------
CREATE TABLE T_PERSON (
  PERSON_ID       NUMBER                 NOT NULL,
  IDENT           CHAR(11)               NOT NULL,
  IDENTTYPE       CHAR(3)                NOT NULL,
  KJONN           CHAR(1)                NOT NULL,
  FORNAVN         VARCHAR2(50)           NOT NULL,
  MELLOMNAVN      VARCHAR2(50),
  ETTERNAVN       VARCHAR2(50)           NOT NULL,
  STATSBORGERSKAP CHAR(3),
  REGDATO         TIMESTAMP(6)           NOT NULL,
  OPPRETTET_DATO  TIMESTAMP(6)           NOT NULL,
  OPPRETTET_AV    VARCHAR2(20)           NOT NULL,
  ENDRET_DATO     TIMESTAMP(6)           NOT NULL,
  ENDRET_AV       VARCHAR2(20)           NOT NULL
);

---------------------------------------------------
-- P R I M A R Y   K E Y   C O N S T R A I N T S --
---------------------------------------------------
ALTER TABLE T_PERSON     ADD CONSTRAINT T_PERSON_PK     PRIMARY KEY (PERSON_ID);

-----------------------
-- S E Q U E N C E S --
-----------------------
CREATE SEQUENCE T_PERSON_SEQ     START WITH 100000000;