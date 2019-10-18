package no.nav.tps.forvalteren.domain.service;

import java.util.HashMap;
import java.util.Map;

public enum Sivilstand {

    UGIFT("1", "UGIF"),
    GIFT("2", "GIFT"),
    ENKE_ELLER_ENKEMANN("3", "ENKE"),
    SKILT("4", "SKIL"),
    SEPARERT("5", "SEPR"),
    REGISTRERT_PARTNER("6", "REPA"),
    SEPARERT_PARTNER("7", "SEPA"),
    SKILT_PARTNER("8", "SKPA"),
    GJENLEVENDE_PARTNER("9", "GJPA");

    private static Map<String, Sivilstand> map = new HashMap<>();

    static {
        for (Sivilstand sivilstand : Sivilstand.values()) {
            map.put(sivilstand.kodeverkskode, sivilstand);
        }
    }

    private final String kode;

    private final String kodeverkskode;

    Sivilstand(final String sivilstandKode, String kodeverkkode) {
        kode = sivilstandKode;
        this.kodeverkskode = kodeverkkode;
    }

    public String getRelasjonTypeKode() {
        return kode;
    }

    public String getKodeverkskode() {
        return kodeverkskode;
    }

    public static Sivilstand fetchSivilstand(String sivilstandKode) {
        return values()[Integer.valueOf(sivilstandKode) - 1];
    }

    public static Sivilstand lookup(String kodeverkkode) {
        return map.getOrDefault(kodeverkkode, UGIFT);
    }

    public static boolean exists(String kodeverkkode) {
        return map.containsKey(kodeverkkode);
    }
}
