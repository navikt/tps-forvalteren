-----------------
-- T A B L E S --
-----------------
CREATE TABLE T_GRUPPE (
  GRUPPE_ID       NUMBER(9)              NOT NULL,
  NAVN            VARCHAR2(30)           NOT NULL,
  BESKRIVELSE     VARCHAR2(200),
  OPPRETTET_DATO  TIMESTAMP              NOT NULL,
  OPPRETTET_AV    VARCHAR2(20)           NOT NULL,
  ENDRET_DATO     TIMESTAMP              NOT NULL,
  ENDRET_AV       VARCHAR2(20)           NOT NULL
);

CREATE TABLE T_TAG (
  TAG_ID          NUMBER(9)              NOT NULL,
  NAVN            VARCHAR(25)            NOT NULL
);

CREATE TABLE T_GRUPPE_TAG (
  GRUPPE_ID       NUMBER(9)              NOT NULL,
  TAG_ID          NUMBER(9)              NOT NULL
);

---------------------------------------------------
-- P R I M A R Y   K E Y   C O N S T R A I N T S --
---------------------------------------------------
ALTER TABLE T_GRUPPE      ADD CONSTRAINT T_GRUPPE_PK      PRIMARY KEY (GRUPPE_ID);
ALTER TABLE T_TAG         ADD CONSTRAINT T_TAG_PK         PRIMARY KEY (TAG_ID);

---------------------------------------------------
-- F O R E I G N   K E Y   C O N S T R A I N T S --
---------------------------------------------------
ALTER TABLE T_GRUPPE_TAG ADD CONSTRAINT GRUPPE_ID_FK FOREIGN KEY (GRUPPE_ID) REFERENCES T_GRUPPE (GRUPPE_ID);
ALTER TABLE T_GRUPPE_TAG ADD CONSTRAINT TAG_ID_FK    FOREIGN KEY (TAG_ID)    REFERENCES T_TAG    (TAG_ID);

-----------------------
-- S E Q U E N C E S --
-----------------------
CREATE SEQUENCE T_GRUPPE_SEQ          START WITH 100000000;
CREATE SEQUENCE T_BRUKER_SEQ          START WITH 100000000;
CREATE SEQUENCE T_TAG_SEQ             START WITH 100000000;

