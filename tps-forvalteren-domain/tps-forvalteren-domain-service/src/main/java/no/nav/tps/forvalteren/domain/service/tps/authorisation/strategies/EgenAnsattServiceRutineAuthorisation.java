package no.nav.tps.forvalteren.domain.service.tps.authorisation.strategies;

import com.fasterxml.jackson.annotation.JsonIgnoreType;

@JsonIgnoreType
public class EgenAnsattServiceRutineAuthorisation implements ServiceRutineAuthorisationStrategy {
    public static EgenAnsattServiceRutineAuthorisation egenAnsattAuthorisation() {
        return new EgenAnsattServiceRutineAuthorisation();
    }
}

