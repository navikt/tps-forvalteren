CREATE TABLE T_DOEDSMELDING (
  DOEDSMELDING_ID NUMBER(9) NOT NULL,
  PERSON_ID       NUMBER(9) NOT NULL,
  MELDING_SENDT   BIT
);

---------------------------------------------------
-- P R I M A R Y   K E Y   C O N S T R A I N T S --
---------------------------------------------------
ALTER TABLE T_DOEDSMELDING
  ADD CONSTRAINT T_DOEDSMELDING_PK PRIMARY KEY (DOEDSMELDING_ID);

---------------------------------------------------
-- F O R E I G N   K E Y   C O N S T R A I N T S --
---------------------------------------------------
ALTER TABLE T_DOEDSMELDING
  ADD CONSTRAINT DOEDSMELDING_PERSON_FK FOREIGN KEY (PERSON_ID) REFERENCES T_PERSON (PERSON_ID);

-----------------------
-- S E Q U E N C E S --
-----------------------
CREATE SEQUENCE T_DOEDSMELDING_SEQ START WITH 100000000;