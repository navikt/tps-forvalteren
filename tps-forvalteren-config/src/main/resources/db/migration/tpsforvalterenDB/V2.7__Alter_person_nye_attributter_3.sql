-----------------------------
-- A L T E R   T A B L E S --
-----------------------------
ALTER TABLE T_PERSON
  RENAME COLUMN MAALFORM TO SPRAK_KODE;

ALTER TABLE T_PERSON
  ADD DATO_SPRAK TIMESTAMP;