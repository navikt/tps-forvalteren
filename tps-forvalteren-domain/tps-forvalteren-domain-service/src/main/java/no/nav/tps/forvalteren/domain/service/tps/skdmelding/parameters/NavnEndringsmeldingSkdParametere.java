package no.nav.tps.forvalteren.domain.service.tps.skdmelding.parameters;

public class NavnEndringsmeldingSkdParametere implements SkdParametersCreator {

    public static SkdParametersCreator navnEndringsmeldingParameterCreator() {
        return new NavnEndringsmeldingSkdParametere();
    }
}
