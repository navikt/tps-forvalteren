---------------------------
-- A L T E R   T A B L E --
---------------------------
ALTER TABLE t_gateadresse
    ADD (tilleggsadresse VARCHAR2(50));

ALTER TABLE t_matrikkeladresse
    ADD (tilleggsadresse VARCHAR2(50));