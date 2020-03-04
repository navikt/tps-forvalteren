-------------------------------
-- C R E A T E   T A B L E S --
-------------------------------
CREATE TABLE T_INNVANDRET_UTVANDRET
(
    ID                  NUMBER(9) PRIMARY KEY,
    PERSON_ID           NUMBER(9) NOT NULL,
    INNUTVANDRET        VARCHAR2(10) NOT NULL,
    LANDKODE            VARCHAR2(4) NOT NULL,
    FLYTTEDATO          DATE NOT NULL
);

---------------------------------------------------
-- P R I M A R Y   K E Y   C O N S T R A I N T S --
---------------------------------------------------
ALTER TABLE T_INNVANDRET_UTVANDRET
    ADD CONSTRAINT T_INNVANDRET_FK FOREIGN KEY (PERSON_ID) REFERENCES T_PERSON (PERSON_ID);

-----------------------
-- S E Q U E N C E S --
-----------------------
CREATE SEQUENCE T_INNVANDRET_UTVANDRET_SEQ
    START WITH 100000000;

-----------------------------
-- M I G R A T E   D A T A --
-----------------------------
INSERT INTO T_INNVANDRET_UTVANDRET
SELECT T_INNVANDRET_UTVANDRET_SEQ.NEXTVAL, P.PERSON_ID, 'INNVANDRET', P.INNVANDRET_FRA_LAND, P.INNVANDRET_FRA_LAND_FLYTTEDATO
FROM T_PERSON P
WHERE P.INNVANDRET_FRA_LAND IS NOT NULL AND P.INNVANDRET_FRA_LAND_FLYTTEDATO IS NOT NULL;

INSERT INTO T_INNVANDRET_UTVANDRET
SELECT T_INNVANDRET_UTVANDRET_SEQ.NEXTVAL, P.PERSON_ID, 'UTVANDRET', P.UTVANDRET_TIL_LAND, P.UTVANDRET_TIL_LAND_FLYTTEDATO
FROM T_PERSON P
WHERE P.UTVANDRET_TIL_LAND IS NOT NULL AND P.UTVANDRET_TIL_LAND_FLYTTEDATO IS NOT NULL;

COMMIT;

-----------------------------
-- A L T E R   T A B L E S --
-----------------------------
--ALTER TABLE T_PERSON DROP (INNVANDRET_FRA_LAND, INNVANDRET_FRA_LAND_FLYTTEDATO, INNVANDRET_FRA_LAND_REGDATO,
--                           UTVANDRET_TIL_LAND, UTVANDRET_TIL_LAND_FLYTTEDATO, UTVANDRET_TIL_LAND_REGDATO);