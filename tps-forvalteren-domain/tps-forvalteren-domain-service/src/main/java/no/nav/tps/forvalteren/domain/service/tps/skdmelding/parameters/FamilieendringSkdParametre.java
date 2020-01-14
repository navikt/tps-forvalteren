package no.nav.tps.forvalteren.domain.service.tps.skdmelding.parameters;

public class FamilieendringSkdParametre implements SkdParametersCreator {

    public static SkdParametersCreator familieendringSkdParametreCreator() {
        return new FamilieendringSkdParametre();
    }
}
