package no.nav.tps.forvalteren.domain.test.provider;

import no.nav.tps.forvalteren.domain.test.builder.GateadresseBuilder;

import java.time.LocalDateTime;

import static no.nav.tps.forvalteren.domain.test.provider.PersonProvider.aMalePerson;

public class GateadresseProvider {

    public static GateadresseBuilder standardGateadresse() {
        return GateadresseBuilder.aGateadresse()
        .withAdresse("Sannergata")
        .withFlyttedato(LocalDateTime.now())
        .withGatekode("12345")
        .withHusnummer("2")
        .withKommunenr("1111")
        .withPostnr("0372")
        .withPerson(aMalePerson().id(1L).build());
    }
}
