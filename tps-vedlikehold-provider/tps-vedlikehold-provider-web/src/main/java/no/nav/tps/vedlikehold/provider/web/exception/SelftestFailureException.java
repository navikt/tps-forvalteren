package no.nav.tps.vedlikehold.provider.web.exception;

/**
 * @author Kristian Kyvik (Visma Consulting AS).
 */
public class SelftestFailureException extends RuntimeException {
    public SelftestFailureException(String message) {
        super(message);
    }
}
