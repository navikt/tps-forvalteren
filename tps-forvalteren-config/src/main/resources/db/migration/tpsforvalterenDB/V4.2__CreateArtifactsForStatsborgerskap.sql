-------------------------------
-- C R E A T E   T A B L E S --
-------------------------------
CREATE TABLE T_STATSBORGERSKAP
(
    ID                      NUMBER(9) PRIMARY KEY,
    PERSON_ID               NUMBER(9) NOT NULL,
    STATSBORGERSKAP         VARCHAR2(4),
    STATSBORGERSKAP_REGDATO DATE
);

---------------------------------------------------
-- P R I M A R Y   K E Y   C O N S T R A I N T S --
---------------------------------------------------
ALTER TABLE T_STATSBORGERSKAP
    ADD CONSTRAINT T_STATSBORGERSKAP_FK FOREIGN KEY (PERSON_ID) REFERENCES T_PERSON (PERSON_ID);

-----------------------
-- S E Q U E N C E S --
-----------------------
CREATE SEQUENCE T_STATSBORGERSKAP_SEQ
    START WITH 100000000;

-----------------------------
-- M I G R A T E   D A T A --
-----------------------------
INSERT INTO T_STATSBORGERSKAP
SELECT T_STATSBORGERSKAP_SEQ.NEXTVAL, PERSON_ID, STATSBORGERSKAP, STATSBORGERSKAP_REGDATO
FROM T_PERSON
WHERE STATSBORGERSKAP IS NOT NULL;

COMMIT;

-------------------------------
-- M O D I F Y   T A B L E S --
-------------------------------
ALTER TABLE T_PERSON
    DROP (
          STATSBORGERSKAP,
          STATSBORGERSKAP_REGDATO
        );
