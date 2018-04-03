-----------------
-- T A B L E S --
-----------------
CREATE TABLE T_VERGEMAAL (
  VERGEMAAL_ID    NUMBER(9)    NOT NULL,
  IDENT           VARCHAR2(11) NOT NULL,
  SAKS_ID         VARCHAR2(50) NOT NULL,
  EMBETE          VARCHAR2(3)  NOT NULL,
  SAKSTYPE        VARCHAR2(50),
  VEDTAKSDATO     VARCHAR2(50),
  INTERN_VERGE_ID DATE,
  VERGE_FNR       VARCHAR2(11),
  VERGETYPE       VARCHAR2(3),
  MANDATTYPE      VARCHAR2(3),
  MANDATTEKST     VARCHAR2(50)
);

---------------------------------------------------
-- P R I M A R Y   K E Y   C O N S T R A I N T S --
---------------------------------------------------
ALTER TABLE T_VERGEMAAL
  ADD CONSTRAINT T_VERGEMAAL_PK PRIMARY KEY (VERGEMAAL_ID);


---------------------------------------------------
-- F O R E I G N   K E Y   C O N S T R A I N T S --
---------------------------------------------------
ALTER TABLE T_VERGEMAAL ADD CONSTRAINT VERGEMAAL_ID_FK FOREIGN KEY (IDENT) REFERENCES T_PERSON(PERSON_ID);

ALTER TABLE T_VERGEMAAL ADD CONSTRAINT INTERN_VERGE_ID_FK FOREIGN KEY (INTERN_VERGE_ID) REFERENCES T_PERSON(PERSON_ID);
-----------------------
-- S E Q U E N C E S --
-----------------------
CREATE SEQUENCE T_VERGEMAAL_SEQ
  START WITH 100000000;