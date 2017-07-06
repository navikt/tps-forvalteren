package no.nav.tps.forvalteren.service.command.exceptions;

public class PersonNotFoundException extends NotFoundException{

    public PersonNotFoundException(String message) {
        super(message);
    }
}
