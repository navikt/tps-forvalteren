package no.nav.tps.forvalteren.domain.service.tps.skdmelding.parameters;

public class InnvandringUpdateSkdParametere implements SkdParametersCreator {

    public static SkdParametersCreator innvandringUpdateParameterCreator() {
        return new InnvandringUpdateSkdParametere();
    }
}
