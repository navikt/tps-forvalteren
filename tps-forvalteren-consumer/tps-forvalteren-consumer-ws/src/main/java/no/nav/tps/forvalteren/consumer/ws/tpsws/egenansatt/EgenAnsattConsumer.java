package no.nav.tps.forvalteren.consumer.ws.tpsws.egenansatt;

import no.nav.tps.forvalteren.consumer.ws.tpsws.AuthorisationStrategyConsumer;


public interface EgenAnsattConsumer extends AuthorisationStrategyConsumer {
    boolean isEgenAnsatt(String fnr);
}
