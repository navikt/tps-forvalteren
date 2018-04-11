package no.nav.tps.forvalteren.domain.service.tps.skdmelding.parameters;

public class InnvandringCreateSkdParametere implements SkdParametersCreator {

    public static SkdParametersCreator innvandringParameterCreator() {
        return new InnvandringCreateSkdParametere();
    }
}
