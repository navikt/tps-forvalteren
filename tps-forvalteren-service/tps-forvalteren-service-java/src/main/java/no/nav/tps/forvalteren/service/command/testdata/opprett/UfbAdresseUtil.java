package no.nav.tps.forvalteren.service.command.testdata.opprett;

import static java.util.Objects.nonNull;

import no.nav.tps.forvalteren.domain.jpa.Adresse;
import no.nav.tps.forvalteren.domain.jpa.Gateadresse;

public final class UfbAdresseUtil {

    public static final String UTEN_FAST_BOSTED = "UTEN FAST BOSTED";
    private static final String KOMMUNENR = "0301";

    private UfbAdresseUtil() {
    }

    public static Adresse createAdresseUfb(String kommunenr) {
        Gateadresse gateadresse = Gateadresse.builder()
                .adresse(UTEN_FAST_BOSTED)
                .build();
        gateadresse.setKommunenr(nonNull(kommunenr) ? kommunenr : KOMMUNENR);

        return gateadresse;
    }
}
