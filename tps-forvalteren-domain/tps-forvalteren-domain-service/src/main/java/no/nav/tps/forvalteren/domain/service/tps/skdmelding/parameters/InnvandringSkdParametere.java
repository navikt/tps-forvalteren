package no.nav.tps.forvalteren.domain.service.tps.skdmelding.parameters;

public class InnvandringSkdParametere implements SkdParametersCreator {

    public static SkdParametersCreator innvandringParameterCreator() {
        return new InnvandringSkdParametere();
    }
}
