package no.nav.tps.vedlikehold.provider.rs.api.v1.selftest;

import no.nav.tps.vedlikehold.common.java.message.MessageProvider;
import no.nav.tps.vedlikehold.provider.rs.api.v1.selftest.models.SelftestResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StopWatch;

import static no.nav.tps.vedlikehold.common.java.message.MessageConstants.SELFTEST_UNKNOWN_ERROR_KEY;


/**
 * @author Kristian Kyvik (Visma Consulting AS).
 */
public abstract class SubSystemSelftest implements Selftest {

    private static final Logger LOGGER = LoggerFactory.getLogger(SubSystemSelftest.class);

    @Autowired
    private MessageProvider messageProvider;

    @Override
    public final SelftestResult perform() {
        StopWatch stopWatch = new StopWatch(getSubSystemName() + " selftest");

        stopWatch.start();
        SelftestResult result = performSelftest();
        stopWatch.stop();
        result.setResponseTime(stopWatch.getLastTaskTimeMillis());

        return result;
    }

    /**
     * Returns the name of the sub-system that is to be displayed on the selftest page. This method must be
     * implemented by subclasses, and should not return {@code null} or an empty string.
     *
     * @return The name of the sub-system that this selftest is for.
     */
    protected abstract String getSubSystemName();

    /**
     * Performs the check that must be done to verify the sub-system. This method must be implemented by
     * subclasses. It should generally either return {@code true} or, in the case of an error, throw a
     * runtime exception. A return value of {@code false} is supported if necessary, however. Exceptions
     * thrown should contain a helpful message, as it will be displayed on the selftest page.
     *
     * @return {@code true} If the check completed without any problems. {@code false} if the check encountered
     * an unexpected and unknown error.
     */
    protected abstract boolean performCheck() throws Exception;

    private SelftestResult performSelftest() {

        try {
            Boolean selftestSucceeded = performCheck();

            SelftestResult result;

            if (selftestSucceeded) {
                result = new SelftestResult(getSubSystemName());
            } else {
                result = createSelftestResultForUnknownError();

                LOGGER.error("Selftest of '{}' failed with an unknown error: {}", getSubSystemName(), result.getErrorMessage());
            }

            return result;
        } catch (Exception exception) {
            LOGGER.error("Selftest og '{}' failed with exception: {}", exception);

            return createSelftestResultForException(exception);
        }
    }

    private SelftestResult createSelftestResultForUnknownError() {
        String errorMessage = messageProvider.get(SELFTEST_UNKNOWN_ERROR_KEY);
        return new SelftestResult(getSubSystemName(), errorMessage);
    }

    private SelftestResult createSelftestResultForException(Exception cause) {
        String errorMessage = cause.getClass().getCanonicalName() + ": " + cause.getMessage();
        return new SelftestResult(getSubSystemName(), errorMessage);
    }
}
