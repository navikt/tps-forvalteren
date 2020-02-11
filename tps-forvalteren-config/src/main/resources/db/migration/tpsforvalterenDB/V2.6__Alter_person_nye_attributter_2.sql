-----------------------------
-- M I G R A T E   D A T A --
-----------------------------

UPDATE T_PERSON
SET SIVILSTAND = NULL
WHERE SIVILSTAND = '0';

COMMIT;