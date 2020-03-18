package no.nav.tps.forvalteren.domain.jpa;

import java.util.HashMap;
import java.util.Map;

public enum Sivilstatus {

    UGIFT("1", "UGIF"),
    GIFT("2", "GIFT"),
    ENKE_ELLER_ENKEMANN("3", "ENKE"),
    SKILT("4", "SKIL"),
    SEPARERT("5", "SEPR"),
    REGISTRERT_PARTNER("6", "REPA"),
    SEPARERT_PARTNER("7", "SEPA"),
    SKILT_PARTNER("8", "SKPA"),
    GJENLEVENDE_PARTNER("9", "GJPA"),
    SAMBOER("1", "SAMB");

    private static Map<String, Sivilstatus> map = new HashMap<>();

    static {
        for (Sivilstatus sivilstatus : Sivilstatus.values()) {
            map.put(sivilstatus.kodeverkskode, sivilstatus);
        }
    }

    private final String kode;

    private final String kodeverkskode;

    Sivilstatus(final String sivilstandKode, String kodeverkkode) {
        kode = sivilstandKode;
        this.kodeverkskode = kodeverkkode;
    }

    public String getRelasjonTypeKode() {
        return kode;
    }

    public String getKodeverkskode() {
        return kodeverkskode;
    }

    public static Sivilstatus fetchSivilstand(String sivilstandKode) {
        return values()[Integer.valueOf(sivilstandKode) - 1];
    }

    public static Sivilstatus lookup(String kodeverkkode) {
        return map.getOrDefault(kodeverkkode, UGIFT);
    }

    public static boolean exists(String kodeverkkode) {
        return map.containsKey(kodeverkkode);
    }
}
