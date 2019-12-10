package no.nav.tps.forvalteren.domain.test.provider;

import static java.util.Arrays.asList;

import java.time.LocalDateTime;

import no.nav.tps.forvalteren.domain.jpa.Person;
import no.nav.tps.forvalteren.domain.jpa.Statsborgerskap;

public final class PersonProvider {

    private PersonProvider() {}

    public static Person.PersonBuilder aMalePerson() {
        return Person.builder()
                .ident("12125678910")
                .identtype("FNR")
                .kjonn("M")
                .fornavn("Ola")
                .mellomnavn("0")
                .etternavn("Nordmann")
                .statsborgerskap(asList(Statsborgerskap.builder().statsborgerskap("000").build()))
                .regdato(LocalDateTime.now())
                .opprettetDato(LocalDateTime.now())
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
                .statsborgerskap(asList(Statsborgerskap.builder().statsborgerskap("000").build()))
                .regdato(LocalDateTime.now())
                .opprettetDato(LocalDateTime.now())
                .opprettetAv("b234567");
    }
}
