package no.nav.tps.vedlikehold.consumer.ws.tpsws.egenansatt;

/**
 * @author Tobias Hansen (Visma Consulting AS).
 */
public interface EgenAnsattConsumer {
    boolean ping();

    boolean isEgenAnsatt(String fnr);
}
