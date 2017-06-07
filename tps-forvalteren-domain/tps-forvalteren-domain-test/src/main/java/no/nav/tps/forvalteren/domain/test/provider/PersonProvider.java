package no.nav.tps.forvalteren.domain.test.provider;

import no.nav.tps.forvalteren.domain.jpa.Person;

public class PersonProvider {

    public static Person.PersonBuilder aMalePerson() {
        return Person.builder()
                .ident("12345678910")
                .identtype("FNR")
                .kjonn('M')
                .fornavn("Ola")
                .mellomnavn("0")
                .etternavn("Nordmann");
    }

    public static Person.PersonBuilder aFemalePerson() {
        return Person.builder()
                .ident("22245678910")
                .identtype("FNR")
                .kjonn('K')
                .fornavn("Kari")
                .mellomnavn("0")
                .etternavn("Nordmann");
    }
}
