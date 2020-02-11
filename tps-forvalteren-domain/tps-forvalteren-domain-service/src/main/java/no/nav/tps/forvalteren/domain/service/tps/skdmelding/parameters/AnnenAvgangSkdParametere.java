package no.nav.tps.forvalteren.domain.service.tps.skdmelding.parameters;

public class AnnenAvgangSkdParametere implements SkdParametersCreator{

    public static SkdParametersCreator annenAvgangParameterCreator() {
        return new AnnenAvgangSkdParametere();
    }
}
