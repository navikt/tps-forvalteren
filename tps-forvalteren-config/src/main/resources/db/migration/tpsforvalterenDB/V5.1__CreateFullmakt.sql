-------------------------------
-- C R E A T E   T A B L E S --
-------------------------------
CREATE TABLE T_FULLMAKT
(
    ID                   NUMBER(9) PRIMARY KEY,
    PERSON_ID            NUMBER(9) NOT NULL,
    OMRAADER             VARCHAR2(512) NOT NULL,
    KILDE                VARCHAR2(100) NOT NULL,
    GYLDIG_FOM           DATE NOT NULL,
    GYLDIG_TOM           DATE NOT NULL,
    FULLMEKTIG_PERSON_ID NUMBER(9) NOT NULL,
    CONSTRAINT FK_FULLMEKTIG_PERSON FOREIGN KEY (PERSON_ID) REFERENCES T_PERSON (PERSON_ID)
);