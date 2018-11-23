package no.nav.tps.forvalteren.service.command.tps.skdmelding.skdparam.utils;

public final class NullcheckUtil {

    private NullcheckUtil() {}

    public static <T> T nullcheckSetDefaultValue(T value, T defaultValue) {
        return value != null ? value : defaultValue;
    }
}
