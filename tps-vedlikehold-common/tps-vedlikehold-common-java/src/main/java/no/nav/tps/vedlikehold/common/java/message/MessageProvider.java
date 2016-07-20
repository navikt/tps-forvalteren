package no.nav.tps.vedlikehold.common.java.message;

/**
 * @author Kristian Kyvik (Visma Consulting AS).
 */
public interface MessageProvider {
    String get(String messageKey);

    String get(String messageKey, Object... arguments);
}
