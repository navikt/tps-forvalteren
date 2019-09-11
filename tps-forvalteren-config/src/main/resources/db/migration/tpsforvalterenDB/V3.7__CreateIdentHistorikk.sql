-------------------------------
-- C R E A T E   T A B L E S --
-------------------------------
CREATE TABLE T_IDENT_HISTORIKK
(
    ID                   NUMBER(9) NOT NULL,
    PERSON_ID            NUMBER(9) NOT NULL,
    HISTORIC_IDENT_ORDER NUMBER(3) NOT NULL,
    HISTORIC_PERSON_ID   NUMBER(9) NOT NULL,
    OPPRETTET_DATO       DATE,
    OPPRETTET_AV         VARCHAR2(20),
    ENDRET_DATO          DATE,
    ENDRET_AV            VARCHAR2(20)
);

---------------------------------------------------
-- P R I M A R Y   K E Y   C O N S T R A I N T S --
---------------------------------------------------
ALTER TABLE T_IDENT_HISTORIKK
    ADD CONSTRAINT T_IDENT_HISTORIKK_PK PRIMARY KEY (ID);

---------------------------------------------------
-- F O R E I G N   K E Y   C O N S T R A I N T S --
---------------------------------------------------
ALTER TABLE T_IDENT_HISTORIKK
    ADD CONSTRAINT IDENT_HISTORIKK_FK FOREIGN KEY (PERSON_ID) REFERENCES T_PERSON (PERSON_ID);
ALTER TABLE T_IDENT_HISTORIKK
    ADD CONSTRAINT IDENT_HISTORIKK_FK2 FOREIGN KEY (HISTORIC_PERSON_ID) REFERENCES T_PERSON (PERSON_ID);

-----------------------------------------
-- U N I Q U E   C O N S T R A I N T S --
-----------------------------------------
ALTER TABLE T_IDENT_HISTORIKK
    ADD CONSTRAINT IDENT_HISTORIKK_UQ UNIQUE (ID, HISTORIC_IDENT_ORDER);

-----------------------
-- S E Q U E N C E S --
-----------------------
CREATE SEQUENCE T_IDENT_HISTORIKK_SEQ START WITH 100000000;