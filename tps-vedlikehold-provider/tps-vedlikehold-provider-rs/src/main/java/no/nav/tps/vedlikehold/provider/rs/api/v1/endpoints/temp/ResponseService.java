package no.nav.tps.vedlikehold.provider.rs.api.v1.endpoints.temp;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

/**
 * Created by Øyvind Grimnes, Visma Consulting AS on 12.07.2016.
 */
public class ResponseService {

    public static ResponseEntity<?> badRequest(String message) {
        return responseEntity(null,message, HttpStatus.BAD_REQUEST);
    }

    public static ResponseEntity<?> unauthorized() {
        return responseEntity(null, null, HttpStatus.UNAUTHORIZED);
    }

    public static ResponseEntity<?> unauthorized(String message) {
        return responseEntity(null, message, HttpStatus.UNAUTHORIZED);
    }

    public static ResponseEntity<?> ok(Object content) {
        return responseEntity(content, null, HttpStatus.OK);
    }

    private static ResponseEntity<?> responseEntity(Object content, String message, HttpStatus status) {

        if (status == HttpStatus.OK) {
            return new ResponseEntity<>(content, status);
        }

        /* Unsuccessful request */
        //TODO: Should return an object that will be parsed to JSON by Spring

        String formattedMessage = status.getReasonPhrase();

        if (message != null && !message.isEmpty()) {
            formattedMessage = status.getReasonPhrase() + ": " + message;
        }

        return new ResponseEntity<>(formattedMessage, status);
    }
}
