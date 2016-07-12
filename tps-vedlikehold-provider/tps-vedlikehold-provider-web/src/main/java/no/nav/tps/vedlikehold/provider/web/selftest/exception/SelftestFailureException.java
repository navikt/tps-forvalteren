package no.nav.tps.vedlikehold.provider.web.selftest.exception;

/**
 * @author Kristian Kyvik (Visma Consulting AS).
 */
public class SelftestFailureException extends RuntimeException {
    public SelftestFailureException(String message) {
        super(message);
    }
}
