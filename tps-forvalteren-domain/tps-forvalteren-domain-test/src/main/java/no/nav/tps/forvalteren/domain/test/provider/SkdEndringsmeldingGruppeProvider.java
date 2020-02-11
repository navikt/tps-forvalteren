package no.nav.tps.forvalteren.domain.test.provider;

import no.nav.tps.forvalteren.domain.jpa.SkdEndringsmeldingGruppe;

public class SkdEndringsmeldingGruppeProvider {

    public static SkdEndringsmeldingGruppe.SkdEndringsmeldingGruppeBuilder aSkdEndringsmeldingGruppe() {
        return SkdEndringsmeldingGruppe.builder()
                .navn("Meldingsgruppe 1")
                .beskrivelse("Mange meldinger");
    }

}
