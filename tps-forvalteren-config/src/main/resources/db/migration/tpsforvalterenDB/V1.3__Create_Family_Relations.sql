CREATE TABLE T_RELASJON (
    RELASJON_ID NUMBER(9) NOT NULL,
    PERSON_ID NUMBER(9) NOT NULL,
    PERSON_RELASJON_ID NUMBER(9) NOT NULL,
    RELASJON_TYPE_NAVN VARCHAR(10) NOT NULL
);

---------------------------------------------------
-- P R I M A R Y   K E Y   C O N S T R A I N T S --
---------------------------------------------------
ALTER TABLE T_RELASJON ADD CONSTRAINT T_RELASJON_ID_PK PRIMARY KEY (RELASJON_ID);

---------------------------------------------------
-- F O R E I G N   K E Y   C O N S T R A I N T S --
---------------------------------------------------
ALTER TABLE T_RELASJON ADD CONSTRAINT PERSON_ID_FK FOREIGN KEY (PERSON_ID) REFERENCES T_PERSON(PERSON_ID);

ALTER TABLE T_RELASJON ADD CONSTRAINT PERSON_RELASJON_ID_FK FOREIGN KEY (PERSON_RELASJON_ID) REFERENCES T_PERSON(PERSON_ID);


-----------------------
-- S E Q U E N C E S --
-----------------------
CREATE SEQUENCE T_RELASJON_SEQ          START WITH 100000000;