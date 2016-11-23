package no.nav.tps.vedlikehold.service.command.exceptions;

/**
 * @author Kristian Kyvik (Visma Consulting AS).
 */
public class SelftestFailureException extends RuntimeException {
    public SelftestFailureException(String message) {
        super(message);
    }
}
