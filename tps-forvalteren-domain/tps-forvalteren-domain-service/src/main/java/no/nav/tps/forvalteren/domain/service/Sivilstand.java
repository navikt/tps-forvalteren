package no.nav.tps.forvalteren.domain.service;

import java.util.HashMap;
import java.util.Map;

public enum  Sivilstand {

    UOPPGITT(0),
    UGIFT(1),
    GIFT(2),
    ENKE_ELLER_ENKEMANN(3),
    SKILT(4),
    SEPARERT(5),
    REGISTRERT_PARTNER(6),
    SEPARERT_PARTNER(7),
    SKILT_PARTNER(8),
    GJENLEVENDE_PARTNER(9);

    private final int kode;

    private static Map<Integer, Sivilstand> map = new HashMap<>();

    Sivilstand(final int sivilstandKode){
        kode = sivilstandKode;
    }

    public int getRelasjonTypeKode() {
        return kode;
    }

    static {
        for (Sivilstand sivilstand : Sivilstand.values()) {
            map.put(sivilstand.kode, sivilstand);
        }
    }

    public static Sivilstand valueOf(int kode) {
        return map.get(kode);
    }
}
