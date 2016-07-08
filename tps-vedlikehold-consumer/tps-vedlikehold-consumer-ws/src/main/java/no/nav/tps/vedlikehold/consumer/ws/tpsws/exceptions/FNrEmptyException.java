package no.nav.tps.vedlikehold.consumer.ws.tpsws.exceptions;

/**
 * @author Tobias Hansen (Visma Consulting AS).
 */
public class FNrEmptyException extends IllegalArgumentException {

    public FNrEmptyException() {
        super("Method was called without fNr");
    }
}