package no.nav.tps.forvalteren.skdavspilleren.common.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import no.nav.tps.forvalteren.service.command.exceptions.NotFoundException;

@ResponseStatus(code = HttpStatus.NO_CONTENT)
public class AvspillerDataNotFoundException extends NotFoundException {
    
    public AvspillerDataNotFoundException(String message) {
        super(message);
    }
}
