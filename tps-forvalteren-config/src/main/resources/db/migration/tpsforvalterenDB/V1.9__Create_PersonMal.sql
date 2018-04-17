-----------------
-- T A B L E S --
-----------------
CREATE TABLE T_PERSONMAL (

  PERSONMAL_ID            NUMBER(9)     NOT NULL,
  PERSONMAL_NAVN          VARCHAR2(25)  NOT NULL,
  PERSONMAL_BESKRIVELSE   VARCHAR2(50),

  OPPRETTET_DATO          TIMESTAMP     NOT NULL,
  OPPRETTET_AV            VARCHAR2(20)  NOT NULL,
  ENDRET_DATO             TIMESTAMP     NOT NULL,
  ENDRET_AV               VARCHAR2(20)  NOT NULL,

  FODT_ETTER              VARCHAR2(8),
  FODT_FOR                VARCHAR2(8),
  KJONN                   VARCHAR2(1),
  STATSBORGERSKAP         VARCHAR2(3),
  SPESREG                 VARCHAR2(4),
  SPESREG_DATO            VARCHAR2(8),
  DOEDSDATO               VARCHAR2(8),
  SIVILSTAND              VARCHAR2(1),
  INNVANDRET_FRA_LAND     VARCHAR2(3),
  MIN_ANTALL_BARN         NUMBER(2),
  MAX_ANTALL_BARN         NUMBER(2),
  GATEADRESSE             VARCHAR2(50),
  GATE_HUSNUMMER          VARCHAR2(4),
  GATE_POSTNR             VARCHAR2(4),
  GATE_KOMMUNENR          VARCHAR2(4),
  GATE_FLYTTEDATO_FRA     VARCHAR2(8),
  GATE_FLYTTEDATO_TIL     VARCHAR2(8),
  POST_LINJE1             VARCHAR2(30),
  POST_LINJE2             VARCHAR2(30),
  POST_LINJE3             VARCHAR2(30),
  POST_LAND               VARCHAR2(3),
  POST_GARDNR             VARCHAR2(5),
  POST_BRUKSNR            VARCHAR2(4),
  POST_FESTENR            VARCHAR2(4),
  POST_UNDERNR            VARCHAR2(3),
  POST_POSTNR             VARCHAR2(4),
  POST_KOMMUNENR          VARCHAR2(4),
  POST_FLYTTEDATO_FRA     VARCHAR2(8),
  POST_FLYTTEDATO_TIL     VARCHAR2(8)
);

---------------------------------------------------
-- P R I M A R Y   K E Y   C O N S T R A I N T S --
---------------------------------------------------

ALTER TABLE T_PERSONMAL ADD CONSTRAINT T_PERSONMAL  PRIMARY KEY (PERSONMAL_ID);

-----------------------
-- S E Q U E N C E S --
-----------------------
CREATE SEQUENCE T_PERSONMAL_SEQ  START WITH 100000000;