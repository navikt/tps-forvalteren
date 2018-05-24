package no.nav.tps.forvalteren.service.command.exceptions;

public class IllegalFoedselsMeldingException extends IllegalArgumentException{
    public IllegalFoedselsMeldingException(String message) {
        super(message);
    }
}
