package no.nav.tps.forvalteren.domain.service.tps.skdmelding.parameters;

public class DoedsmeldingAnnulleringSkdParamtere implements SkdParametersCreator {

    public static SkdParametersCreator doedsmeldingAnnulleringParameterCreator() {
        return new DoedsmeldingAnnulleringSkdParamtere();
    }
    
}
