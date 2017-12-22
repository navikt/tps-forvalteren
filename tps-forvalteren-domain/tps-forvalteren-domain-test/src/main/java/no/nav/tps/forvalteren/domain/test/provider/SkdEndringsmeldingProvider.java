package no.nav.tps.forvalteren.domain.test.provider;

import static no.nav.tps.forvalteren.domain.test.provider.SkdEndringsmeldingGruppeProvider.aSkdEndringsmeldingGruppe;

import no.nav.tps.forvalteren.domain.jpa.SkdEndringsmelding;

public class SkdEndringsmeldingProvider {

    public static SkdEndringsmelding.SkdEndringsmeldingBuilder aSkdEndringsmelding() {
        return SkdEndringsmelding.builder()
                .endringsmelding("Melding")
                .gruppe(aSkdEndringsmeldingGruppe().build());
    }

}
