package no.nav.tps.forvalteren.domain.service.tps.authorisation.strategies;

import com.fasterxml.jackson.annotation.JsonIgnoreType;

@JsonIgnoreType
public class ReadServiceRutineAuthorisation implements ServiceRutineAuthorisationStrategy {
    public static ReadServiceRutineAuthorisation readAuthorisation(){
        return new ReadServiceRutineAuthorisation();
    }
}
