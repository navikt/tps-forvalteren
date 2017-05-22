package no.nav.tps.forvalteren.domain.test.provider;

import no.nav.tps.forvalteren.domain.test.builder.PersonBuilder;

import static no.nav.tps.forvalteren.domain.test.builder.PersonBuilder.aPerson;

public class PersonProvider {

    public static PersonBuilder aMalePerson() {
        return aPerson()
                .withIdent("12345678910")
                .withIdenttype("FNR")
                .withKjonn('M')
                .withFornavn("Ola")
                .withMellomnavn("0")
                .withEtternavn("Nordmann")
                .withKortnavn("Ola O Nordmann");
    }

    public static PersonBuilder aFemalePerson() {
        return aPerson()
                .withIdent("22245678910")
                .withIdenttype("FNR")
                .withKjonn('K')
                .withFornavn("Kari")
                .withMellomnavn("0")
                .withEtternavn("Nordmann")
                .withKortnavn("Kari O Nordmann");
    }
}
