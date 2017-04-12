package no.nav.tps.vedlikehold.domain.service.tps.authorisation.strategies;

import org.codehaus.jackson.annotate.JsonIgnoreType;


@JsonIgnoreType
public class ReadServiceRutineAuthorisation implements ServiceRutineAuthorisationStrategy {
    public static ReadServiceRutineAuthorisation readAuthorisation(){
        return new ReadServiceRutineAuthorisation();
    }
}
