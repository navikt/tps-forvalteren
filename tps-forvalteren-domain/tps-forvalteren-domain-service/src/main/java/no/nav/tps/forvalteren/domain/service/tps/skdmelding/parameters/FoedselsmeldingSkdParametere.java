package no.nav.tps.forvalteren.domain.service.tps.skdmelding.parameters;

public class FoedselsmeldingSkdParametere implements SkdParametersCreator {

    public static SkdParametersCreator foedselsmeldingParameterCreator() {
        return new FoedselsmeldingSkdParametere();
    }
}
