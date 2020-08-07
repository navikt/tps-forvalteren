package no.nav.tps.forvalteren.service.command.exceptions;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import no.nav.tps.forvalteren.domain.service.tps.ResponseStatus;

@RunWith(JUnit4.class)
public class ExceptionTpsServiceRutineMessageCreatorTest {

    private static final String STATUS = "08";
    private static final String MELDING = "En feil er oppstått";
    private static final String UTFYLLENDE_MELDING = "Utfyllende feilmelding";

    @Test(expected = IllegalAccessException.class)
    public void cannotInstantiatePrivate() throws Exception {

        Class clazz = Class.forName(
                "no.nav.tps.forvalteren.service.command.exceptions.ExceptionTpsServiceRutineMessageCreator");
        clazz.getDeclaredConstructor().newInstance();
    }

    @Test
    public void executeOk() throws Exception {

        String status = ExceptionTpsServiceRutineMessageCreator.execute(buildResponseStatus());

        System.out.println(status);
        assertThat(status, containsString("{Kode=" + STATUS + " - ERROR}"));
        assertThat(status, containsString("{Melding=" + MELDING + "}"));
        assertThat(status, containsString("{UtfyllendeMelding=" + UTFYLLENDE_MELDING + "}"));
    }

    private ResponseStatus buildResponseStatus() {
        ResponseStatus responseStatus = new ResponseStatus();
        responseStatus.setKode(STATUS);
        responseStatus.setMelding(MELDING);
        responseStatus.setUtfyllendeMelding(UTFYLLENDE_MELDING);

        return responseStatus;
    }
}