package no.nav.tps.vedlikehold.consumer.ws.tpsws.egenansatt;

/**
 * @author Tobias Hansen (Visma Consulting AS).
 */
public interface EgenAnsattConsumer {
    boolean isEgenAnsatt(String fnr);

    boolean ping() throws Exception;
}
