package no.nav.tps.forvalteren.domain.service.tps.skdmelding.parameters;

public class VergemaalSkdParametere implements SkdParametersCreator {

    public static SkdParametersCreator vergemaalParameterCreator() {
        return new VergemaalSkdParametere();
    }
}
