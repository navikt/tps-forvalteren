package no.nav.tps.forvalteren.domain.service.tps.skdmelding.parameters;

public class DoedsmeldingSkdParametere implements SkdParametersCreator {

    public static SkdParametersCreator doedsmeldingParameterCreator() {
        return new DoedsmeldingSkdParametere();
    }
}
