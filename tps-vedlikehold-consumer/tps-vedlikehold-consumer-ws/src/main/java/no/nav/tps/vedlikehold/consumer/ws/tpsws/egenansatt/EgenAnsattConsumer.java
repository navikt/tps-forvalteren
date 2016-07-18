package no.nav.tps.vedlikehold.consumer.ws.tpsws.egenansatt;

/**
 * @author Tobias Hansen (Visma Consulting AS).
 */
public interface EgenAnsattConsumer {
    boolean ping() throws Exception;

    boolean isEgenAnsatt(String fnr);
}
