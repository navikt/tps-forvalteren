package no.nav.tps.vedlikehold.provider.rs.api.v1.selftest.models;

import org.junit.Test;
import static no.nav.tps.vedlikehold.provider.rs.api.v1.selftest.models.SelftestResult.Status.FEILET;
import static no.nav.tps.vedlikehold.provider.rs.api.v1.selftest.models.SelftestResult.Status.OK;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

/**
 * @author Kristian Kyvik (Visma Consulting AS).
 */
public class SelftestResultTest {

    @Test
    public void constructorWithOneParameterInitializesNameAndOkStatus() {
        SelftestResult result = new SelftestResult("Result name");

        assertThat(result.getName(), is(equalTo("Result name")));
        assertThat(result.getStatus(), is(OK));
        assertThat(result.getResponseTime(), is(0L));
        assertThat(result.getErrorMessage(), is(equalTo("")));
    }

    @Test
    public void constructorWithOneParameterInitializesNameAndFeiletStatus() {
        SelftestResult result = new SelftestResult("Result name", "Error message");

        assertThat(result.getName(), is(equalTo("Result name")));
        assertThat(result.getStatus(), is(FEILET));
        assertThat(result.getResponseTime(), is(0L));
        assertThat(result.getErrorMessage(), is(equalTo("Error message")));
    }

    @Test
    public void setsResponseTime() {
        SelftestResult result = new SelftestResult("Result name");

        result.setResponseTime(1337L);

        assertThat(result.getResponseTime(), is(1337L));
    }
}