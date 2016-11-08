package no.nav.tps.vedlikehold.consumer.ws.tpsws.egenansatt;

import no.nav.tps.vedlikehold.consumer.ws.tpsws.AuthorisationStrategyConsumer;


public interface EgenAnsattConsumer extends AuthorisationStrategyConsumer{
    boolean isEgenAnsatt(String fnr);
}
