package no.nav.tps.forvalteren.domain.test.provider;

import static java.time.LocalDateTime.now;

import no.nav.tps.forvalteren.domain.jpa.Person;

public final class PersonProvider {

    private PersonProvider() {
    }

    public static Person.PersonBuilder aMalePerson() {
        return Person.builder()
                .ident("12125678910")
                .identtype("FNR")
                .kjonn("M")
                .fornavn("Ola")
                .mellomnavn("0")
                .etternavn("Nordmann")
                .regdato(now())
                .opprettetDato(now())
                .opprettetAv("a123456");
    }

    public static Person.PersonBuilder aFemalePerson() {
        return Person.builder()
                .ident("22045678910")
                .identtype("FNR")
                .kjonn("K")
                .fornavn("Kari")
                .mellomnavn("0")
                .etternavn("Nordmann")
                .regdato(now())
                .opprettetDato(now())
                .opprettetAv("b234567");
    }

    public static Person.PersonBuilder aChildPerson() {
        return Person.builder()
                .ident("22041278910")
                .identtype("FNR")
                .kjonn("K")
                .fornavn("Helene")
                .etternavn("Nordmann")
                .regdato(now())
                .opprettetDato(now())
                .opprettetAv("b234567");
    }
}