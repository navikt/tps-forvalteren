package no.nav.tps.forvalteren.domain.service.tps.skdmelding.parameters;

public class UtvandringSkdParametere implements SkdParametersCreator{

    public static SkdParametersCreator utvandringsParameterCreator() {
        return new UtvandringSkdParametere();
    }
}
