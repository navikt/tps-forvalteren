package no.nav.tps.forvalteren.domain.test.provider;

import no.nav.tps.forvalteren.domain.jpa.Gruppe;

public class GruppeProvider {

    public static Gruppe.GruppeBuilder aGruppe() {
        return Gruppe.builder()
                .navn("Testgruppe 1")
                .beskrivelse("Testdata for testgruppe 1");
    }
}
