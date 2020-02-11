package no.nav.tps.forvalteren.domain.service.tps.skdmelding.parameters;

public class EkteskapSkdParametere implements SkdParametersCreator {

    public static SkdParametersCreator ekteskapParameterCreator() {
        return new EkteskapSkdParametere();
    }
}

