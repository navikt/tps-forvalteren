package no.nav.tps.forvalteren.domain.test.provider;

import no.nav.tps.forvalteren.domain.jpa.SkdEndringsmeldingGruppe;

public class SkdEndringsmeldingGruppeProvider {

    public static SkdEndringsmeldingGruppe.SkdEndringsmeldingGruppeBuilder aGruppe() {
        return SkdEndringsmeldingGruppe.builder()
                .navn("Meldingsgruppe 1")
                .beskrivelse("Mange meldinger");
    }

}
