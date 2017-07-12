package no.nav.tps.forvalteren.domain.test.provider;

import no.nav.tps.forvalteren.domain.test.builder.MatrikkeladresseBuilder;

import java.time.LocalDateTime;

import static no.nav.tps.forvalteren.domain.test.provider.PersonProvider.aMalePerson;

public class MatrikkeladresseProvider {

    public static MatrikkeladresseBuilder standardMatrikkeladresse() {
        return MatrikkeladresseBuilder.aMatrikkeladresse()
        .withBruksnr("1234")
        .withFestenr("0000")
        .withFlyttedato(LocalDateTime.now())
        .withGardsnr("55555")
        .withKommunenr("7654")
        .withMellomnavn("Tilfedig gard")
        .withUndernr("333")
        .withPostnr("1337")
        .withPerson(aMalePerson().id(1L).build());
    }
}
