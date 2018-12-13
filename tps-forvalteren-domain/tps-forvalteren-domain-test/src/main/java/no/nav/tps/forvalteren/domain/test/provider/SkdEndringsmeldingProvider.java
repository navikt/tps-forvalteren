package no.nav.tps.forvalteren.domain.test.provider;

import static no.nav.tps.forvalteren.domain.test.provider.SkdEndringsmeldingGruppeProvider.aSkdEndringsmeldingGruppe;

import no.nav.tps.forvalteren.domain.jpa.SkdEndringsmelding;

public class SkdEndringsmeldingProvider {

    public static SkdEndringsmelding.SkdEndringsmeldingBuilder aSkdEndringsmelding() {
        return SkdEndringsmelding.builder()
                .endringsmelding("Melding")
                .foedselsnummer("11111111111")
                .aarsakskode("01")
                .transaksjonstype("1")
                .gruppe(aSkdEndringsmeldingGruppe().build());
    }

}
