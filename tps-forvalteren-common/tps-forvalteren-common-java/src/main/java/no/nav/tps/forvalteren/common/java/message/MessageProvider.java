package no.nav.tps.forvalteren.common.java.message;


public interface MessageProvider {
    String get(String messageKey);

    String get(String messageKey, Object... arguments);
}
