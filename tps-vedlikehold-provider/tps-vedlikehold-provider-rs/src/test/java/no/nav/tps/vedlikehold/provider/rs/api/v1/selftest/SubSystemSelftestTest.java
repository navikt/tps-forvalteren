package no.nav.tps.vedlikehold.provider.rs.api.v1.selftest;

import no.nav.tps.vedlikehold.common.java.message.MessageProvider;
import no.nav.tps.vedlikehold.provider.rs.api.v1.selftest.models.SelftestResult;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static no.nav.tps.vedlikehold.common.java.message.MessageConstants.SELFTEST_UNKNOWN_ERROR_KEY;
import static no.nav.tps.vedlikehold.provider.rs.api.v1.selftest.models.SelftestResult.Status.FEILET;
import static no.nav.tps.vedlikehold.provider.rs.api.v1.selftest.models.SelftestResult.Status.OK;
import static org.hamcrest.CoreMatchers.both;
import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.number.OrderingComparison.greaterThan;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;


@RunWith(MockitoJUnitRunner.class)
public class SubSystemSelftestTest {

    private static final String TEST_SUBSYSTEM_NAME = "Test";

    @InjectMocks
    private SubsystemSelftestStub selftest;

    @Mock
    private MessageProvider messageProviderMock;

    @Test
    public void returnsSuccessfulResultSetWhenPerformCheckReturnsTrue() {
        selftest.mockedResult = true;

        SelftestResult result = selftest.perform();

        assertThat(result.getName(), is(equalTo(TEST_SUBSYSTEM_NAME)));
        assertThat(result.getStatus(), is(OK));
        assertThat(result.getResponseTime(), is(greaterThan(0L)));
        assertThat(result.getErrorMessage(), is(equalTo("")));
    }

    @Test
    public void returnsFailureResultSetWhenPerformCheckReturnsFalse() {
        selftest.mockedResult = false;
        when(messageProviderMock.get(SELFTEST_UNKNOWN_ERROR_KEY)).thenReturn("Unknown error");

        SelftestResult result = selftest.perform();

        assertThat(result.getName(), is(equalTo(TEST_SUBSYSTEM_NAME)));
        assertThat(result.getStatus(), is(FEILET));
        assertThat(result.getResponseTime(), is(greaterThan(0L)));
        assertThat(result.getErrorMessage(), is(equalTo("Unknown error")));
    }

    @Test
    public void returnsFailureResultSetWhenPerformCheckThrowsRuntimeException() {
        selftest.mockedException = new IllegalStateException("Error message");

        SelftestResult result = selftest.perform();

        assertThat(result.getName(), is(equalTo(TEST_SUBSYSTEM_NAME)));
        assertThat(result.getStatus(), is(FEILET));
        assertThat(result.getResponseTime(), is(greaterThan(0L)));
        assertThat(result.getErrorMessage(), both(containsString("IllegalStateException")).and(containsString("Error message")));
    }

    public static class SubsystemSelftestStub extends SubSystemSelftest {

        /**
         * Can be updated by the tests to mock the result returned by the selftest.
         */
        private boolean mockedResult = true;

        /**
         * If set to a non-null value, it overrides {@link #mockedResult} and causes the selftest to throw the specified runtime exception.
         */
        private RuntimeException mockedException = null;

        @Override
        protected String getSubSystemName() {
            return TEST_SUBSYSTEM_NAME;
        }

        @Override
        protected boolean performCheck() {
            try {
                // Pauses for at least 1 millisecond so we can test the response time too.
                Thread.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            if (mockedException == null) {
                return mockedResult;
            } else {
                throw mockedException;
            }
        }
    }
}
