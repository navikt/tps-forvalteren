-------------------------------
-- C R E A T E   T A B L E S --
-------------------------------
DROP TABLE T_VERGEMAAL;

CREATE TABLE T_VERGEMAAL
(
    ID              NUMBER(9) PRIMARY KEY,
    PERSON_ID       NUMBER(9) NOT NULL,
    EMBETE          VARCHAR2(4),
    SAK_TYPE        VARCHAR2(3),
    VEDTAK_DATO     DATE,
    VERGE_PERSON_ID NUMBER(9) NOT NULL,
    MANDAT_TYPE      VARCHAR2(3),
    CONSTRAINT FK_VERGEMAAL_PERSON FOREIGN KEY (PERSON_ID) REFERENCES T_PERSON (PERSON_ID),
    CONSTRAINT FK_VERGEMAAL_PERSON_2 FOREIGN KEY (VERGE_PERSON_ID) REFERENCES T_PERSON (PERSON_ID)
);