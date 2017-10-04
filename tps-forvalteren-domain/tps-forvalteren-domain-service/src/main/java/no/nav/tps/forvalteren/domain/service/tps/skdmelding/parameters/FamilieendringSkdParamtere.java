package no.nav.tps.forvalteren.domain.service.tps.skdmelding.parameters;

public class FamilieendringSkdParamtere implements SkdParametersCreator {

    public static SkdParametersCreator familieendringSkdParamterCreator() {
        return new FamilieendringSkdParamtere();
    }
}
