package no.nav.tps.forvalteren.domain.service.tps.skdmelding.parameters;

public class VigselSkdParametere implements SkdParametersCreator {

    public static SkdParametersCreator vigselParameterCreator() {
        return new VigselSkdParametere();
    }
}

