package no.nav.tps.forvalteren.service.command.tps.skdmelding.skdparam.utils;

public final class NullCheckSetDefault {

    private NullCheckSetDefault() {}

    public static Object nullCheckSetDefaultValue(Object value, Object defaultValue) {
        return value != null ? value : defaultValue;
    }
}
