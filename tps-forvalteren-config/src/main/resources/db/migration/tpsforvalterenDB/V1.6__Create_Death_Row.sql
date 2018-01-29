-----------------
-- T A B L E S --
-----------------
CREATE TABLE T_DEATH_ROW (
  DEATH_ROW_ID   NUMBER(9)    NOT NULL,
  IDENT          VARCHAR2(11) NOT NULL,
  HANDLING       VARCHAR2(50) NOT NULL,
  MILJOE         VARCHAR2(3)  NOT NULL,
  STATUS         VARCHAR2(50),
  TILSTAND       VARCHAR2(50),
  DOEDSDATO      TIMESTAMP    NOT NULL,
  OPPRETTET_DATO TIMESTAMP    NOT NULL,
  OPPRETTET_AV   VARCHAR2(20) NOT NULL,
  ENDRET_DATO    TIMESTAMP    NOT NULL,
  ENDRET_AV      VARCHAR2(20) NOT NULL
);

---------------------------------------------------
-- P R I M A R Y   K E Y   C O N S T R A I N T S --
---------------------------------------------------
ALTER TABLE T_DEATH_ROW
  ADD CONSTRAINT T_DODAREN_PK PRIMARY KEY (DEATH_ROW_ID);

-----------------------
-- S E Q U E N C E S --
-----------------------
CREATE SEQUENCE T_DEATH_ROW_SEQ
  START WITH 100000000;