-----------------------------
-- A L T E R   T A B L E S --
-----------------------------
ALTER TABLE T_PERSON
    MODIFY (
        IDENTTYPE VARCHAR2(4)
        );

UPDATE T_PERSON SET IDENTTYPE = 'BOST' WHERE IDENTTYPE = 'BNR';
COMMIT;