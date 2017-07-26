package no.nav.tps.forvalteren.domain.service;

import java.util.HashMap;
import java.util.Map;

public enum RelasjonType {

    UGIFT(1),
    GIFT(2),
    ENKE_ELLER_ENKEMANN(3),
    SKILT(4),
    SEPARERT(5),
    REGISTRERT_PARTNER(6);

    private final int kode;

    private static Map<Integer, RelasjonType> map = new HashMap<>();

    RelasjonType(final int relasjonsKode){
        kode = relasjonsKode;
    }

    public int getRelasjonTypeKode() {
        return kode;
    }

    static {
        for (RelasjonType relasjonType : RelasjonType.values()) {
            map.put(relasjonType.kode, relasjonType);
        }
    }

    public static RelasjonType valueOf(int kode) {
        return map.get(kode);
    }
}
