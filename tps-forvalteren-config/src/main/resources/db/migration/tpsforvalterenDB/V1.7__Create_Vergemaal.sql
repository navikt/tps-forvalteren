-----------------
-- T A B L E S --
-----------------
CREATE TABLE T_VERGEMAAL (
  VERGEMAAL_ID    NUMBER(9)     NOT NULL,
  IDENT           VARCHAR2(11)  NOT NULL,
  SAKS_ID         NUMBER(7)     NOT NULL,
  EMBETE          VARCHAR2(4),
  SAKSTYPE        VARCHAR2(3),
  VEDTAKSDATO     DATE,
  INTERN_VERGE_ID NUMBER(7)     NOT NULL,
  VERGE_FNR       VARCHAR2(11),
  VERGETYPE       VARCHAR2(3),
  MANDATTYPE      VARCHAR2(3),
  MANDATTEKST     VARCHAR2(100)
);

---------------------------------------------------
-- P R I M A R Y   K E Y   C O N S T R A I N T S --
---------------------------------------------------
ALTER TABLE T_VERGEMAAL
  ADD CONSTRAINT T_VERGEMAAL_PK PRIMARY KEY (VERGEMAAL_ID);


---------------------------------------------------
-- F O R E I G N   K E Y   C O N S T R A I N T S --
---------------------------------------------------
ALTER TABLE T_VERGEMAAL ADD CONSTRAINT VERGEMAAL_ID_FK FOREIGN KEY (IDENT) REFERENCES T_PERSON(IDENT);

ALTER TABLE T_VERGEMAAL ADD CONSTRAINT INTERN_FNR_FK FOREIGN KEY (VERGE_FNR) REFERENCES T_PERSON(IDENT);

-----------------------
-- S E Q U E N C E S --
-----------------------
CREATE SEQUENCE T_VERGEMAAL_SEQ
  START WITH 100000000;