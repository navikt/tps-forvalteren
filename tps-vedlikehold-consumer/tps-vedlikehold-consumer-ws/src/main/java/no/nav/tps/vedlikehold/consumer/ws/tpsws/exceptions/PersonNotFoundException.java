package no.nav.tps.vedlikehold.consumer.ws.tpsws.exceptions;

/**
 * @author Tobias Hansen (Visma Consulting AS).
 */
public class PersonNotFoundException extends RuntimeException {

    public PersonNotFoundException(String fnr, Throwable cause) {
        super("Person with fNr ("+fnr+") not found", cause);
    }
}
