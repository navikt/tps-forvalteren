package no.nav.tps.vedlikehold.domain.service.tps.authorisation.strategies;

import org.codehaus.jackson.annotate.JsonIgnoreType;


@JsonIgnoreType
public class DiskresjonskodeServiceRutineAuthorisation implements ServiceRutineAuthorisationStrategy {
    public static DiskresjonskodeServiceRutineAuthorisation diskresjonskodeAuthorisation(){
        return new DiskresjonskodeServiceRutineAuthorisation();
    }
}