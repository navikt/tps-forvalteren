---------------------------
-- A L T E R   T A B L E --
---------------------------
ALTER TABLE t_person
    ADD (
        telefon_landskode_1 VARCHAR2(6),
        telefonnummer_1 VARCHAR2(20),
        telefon_landskode_2 VARCHAR2(6),
        telefonnummer_2 VARCHAR2(20)
        );