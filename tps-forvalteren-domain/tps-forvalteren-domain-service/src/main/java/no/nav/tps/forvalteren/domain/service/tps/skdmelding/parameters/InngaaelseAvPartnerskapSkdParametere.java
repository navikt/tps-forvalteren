package no.nav.tps.forvalteren.domain.service.tps.skdmelding.parameters;

public class InngaaelseAvPartnerskapSkdParametere implements SkdParametersCreator{

    public static SkdParametersCreator partnerskapParameterCreator() {
        return new InnvandringSkdParametere();
    }
}
