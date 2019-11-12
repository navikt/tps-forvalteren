-------------------------------
-- C R E A T E   T A B L E S --
-------------------------------
CREATE TABLE T_SIVILSTAND
(
    ID                  NUMBER(9) PRIMARY KEY,
    PERSON_ID           NUMBER(9) NOT NULL,
    SIVILSTAND          VARCHAR2(4),
    SIVILSTAND_REGDATO  DATE,
    PERSON_RELASJON_MED NUMBER(9)
);

-----------------------------
-- A L T E R   T A B L E S --
-----------------------------
ALTER TABLE T_PERSON
    ADD (SIVILSTAND_REGDATO DATE);

---------------------------------------------------
-- P R I M A R Y   K E Y   C O N S T R A I N T S --
---------------------------------------------------
ALTER TABLE T_SIVILSTAND
    ADD CONSTRAINT T_SIVILSTAND_FK FOREIGN KEY (PERSON_ID) REFERENCES T_PERSON (PERSON_ID);

ALTER TABLE T_SIVILSTAND
    ADD CONSTRAINT T_SIVILSTAND_REL_FK FOREIGN KEY (PERSON_RELASJON_MED) REFERENCES T_PERSON (PERSON_ID);

-----------------------
-- S E Q U E N C E S --
-----------------------
CREATE SEQUENCE T_SIVILSTAND_SEQ
    START WITH 100000000;