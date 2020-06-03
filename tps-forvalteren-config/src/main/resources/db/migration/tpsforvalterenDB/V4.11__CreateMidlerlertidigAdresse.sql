-----------------------------
-- C R E A T E   T A B L E --
-----------------------------
CREATE TABLE T_MIDLERTIDIG_ADRESSE
(
    ID              NUMBER(9) PRIMARY KEY,
    PERSON_ID       NUMBER(9)    NOT NULL,
    ADRESSETYPE     VARCHAR2(4)  NOT NULL,
    POSTNR          VARCHAR2(4)  NOT NULL,
    GYLDIG_TOM      DATE         NULL,
    TILLEGGSADRESSE VARCHAR2(50) NULL,
    GATENAVN        VARCHAR2(50) NULL,
    GATEKODE        VARCHAR2(5)  NULL,
    HUSNR           VARCHAR2(5)  NULL,
    EIENDOMSNAVN    VARCHAR2(50) NULL,
    POSTBOKSNR      VARCHAR2(5)  NULL,
    POSTBOKS_ANLEGG VARCHAR2(50) NULL,
    POST_LINJE_1    VARCHAR2(30) NULL,
    POST_LINJE_2    VARCHAR2(30) NULL,
    POST_LINJE_3    VARCHAR2(30) NULL,
    POST_LAND       VARCHAR2(4)  NULL
);

---------------------------------------------------
-- F O R E I G N   K E Y   C O N S T R A I N T S --
---------------------------------------------------
ALTER TABLE T_MIDLERTIDIG_ADRESSE
    ADD CONSTRAINT MIDLERTIDIG_ADRESSE_FK FOREIGN KEY (PERSON_ID) REFERENCES T_PERSON (PERSON_ID);

-----------------------
-- S E Q U E N C E S --
-----------------------
CREATE SEQUENCE T_MIDLERTIDIG_ADRESSE_SEQ
    START WITH 1;