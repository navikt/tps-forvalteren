package no.nav.tps.forvalteren.service.command.tps.skdmelding.skdparam.utils;

public final class NullcheckUtil {

    private NullcheckUtil() {}

    public static Object nullcheckSetDefaultValue(Object value, Object defaultValue) {
        return value != null ? value : defaultValue;
    }
}
