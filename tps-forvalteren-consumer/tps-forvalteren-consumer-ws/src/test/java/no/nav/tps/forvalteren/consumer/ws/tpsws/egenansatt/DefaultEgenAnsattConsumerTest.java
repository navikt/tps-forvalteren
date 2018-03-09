package no.nav.tps.forvalteren.consumer.ws.tpsws.egenansatt;

import no.nav.tjeneste.pip.egenansatt.v1.binding.EgenAnsattV1;
import no.nav.tjeneste.pip.egenansatt.v1.meldinger.HentErEgenAnsattEllerIFamilieMedEgenAnsattRequest;
import no.nav.tjeneste.pip.egenansatt.v1.meldinger.HentErEgenAnsattEllerIFamilieMedEgenAnsattResponse;

import javax.xml.ws.soap.SOAPFaultException;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


@RunWith(MockitoJUnitRunner.class)
public class DefaultEgenAnsattConsumerTest {
    private static final String THE_DATABASE_DOES_NOT_ANSWER_ERROR = "Database connection error";
    private static final String SOAP_Fault_Error                   = "Soap error";
    private static final String TEST_FNR                           = "11223344556";

    @InjectMocks
    private DefaultEgenAnsattConsumer egenAnsattConsumer;

    @Mock
    private EgenAnsattV1 egenAnsatt;

    @Mock
    SOAPFaultException soapFaultException;

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Test
    public void isEgenAnsattReturnsFalseWhenCalledWithEmptyString() {
        when(soapFaultException.getMessage()).thenReturn(DefaultEgenAnsattConsumer.EMPTY_FNR_TPSWS_ERROR);
        when(egenAnsatt.hentErEgenAnsattEllerIFamilieMedEgenAnsatt(any(HentErEgenAnsattEllerIFamilieMedEgenAnsattRequest.class)))
                .thenThrow(soapFaultException);

        boolean result = egenAnsattConsumer.isEgenAnsatt("");

        assertThat(result, is(false));
    }

    @Test
    public void isEgenAnsattReturnsFalseWhenCalledWithNull() {
        boolean result = egenAnsattConsumer.isEgenAnsatt(null);

        assertThat(result, is(false));
    }

    @Test
    public void isEgenAnsattReturnsFalseWhenCalledWithInvalidFnr() {
        when(soapFaultException.getMessage()).thenReturn(DefaultEgenAnsattConsumer.INVALID_FNR_TPSWS_ERROR);
        when(egenAnsatt.hentErEgenAnsattEllerIFamilieMedEgenAnsatt(any(HentErEgenAnsattEllerIFamilieMedEgenAnsattRequest.class)))
                .thenThrow(soapFaultException);

        boolean result = egenAnsattConsumer.isEgenAnsatt("-1");

        assertThat(result, is(false));
    }

    @Test
    public void pingReturnsTrueWhenErEgenAnsattRespondsNormally() throws Exception {
        when(egenAnsatt.hentErEgenAnsattEllerIFamilieMedEgenAnsatt(any(HentErEgenAnsattEllerIFamilieMedEgenAnsattRequest.class)))
                .thenReturn(new HentErEgenAnsattEllerIFamilieMedEgenAnsattResponse());

        boolean result = egenAnsattConsumer.ping();

        assertThat(result, is(true));
    }

    @Test
    public void pingThrowsExceptionWhenIsEgenAnsattThrowsException() throws Exception {
        RuntimeException thrownException = new RuntimeException(THE_DATABASE_DOES_NOT_ANSWER_ERROR);

        when(egenAnsatt.hentErEgenAnsattEllerIFamilieMedEgenAnsatt(any(HentErEgenAnsattEllerIFamilieMedEgenAnsattRequest.class)))
                .thenThrow(thrownException);

        expectedException.expect(RuntimeException.class);
        expectedException.expectMessage(THE_DATABASE_DOES_NOT_ANSWER_ERROR);

        egenAnsattConsumer.ping();
    }

    @Test
    public void pingReturnsTrueWhenIsEgenAnsattThrowsPersonNotFoundException() throws Exception {
        when(soapFaultException.getMessage()).thenReturn(DefaultEgenAnsattConsumer.PERSON_NOT_FOUND_TPSWS_ERROR);
        when(egenAnsatt.hentErEgenAnsattEllerIFamilieMedEgenAnsatt(any(HentErEgenAnsattEllerIFamilieMedEgenAnsattRequest.class)))
                .thenThrow(soapFaultException);

        boolean result = egenAnsattConsumer.ping();

        assertThat(result, is(true));
    }

    @Test
    public void pingThrowsExceptionWhenIsEgenAnsattThrowsUncaughtSOAPFaultExceptionNotContaining() throws Exception {
        when(soapFaultException.getMessage()).thenReturn(SOAP_Fault_Error);
        when(egenAnsatt.hentErEgenAnsattEllerIFamilieMedEgenAnsatt(any(HentErEgenAnsattEllerIFamilieMedEgenAnsattRequest.class)))
                .thenThrow(soapFaultException);

        expectedException.expect(SOAPFaultException.class);

        egenAnsattConsumer.ping();
    }

    @Test
    public void hentDiskresjonskodeRequestIsSentWithCorrectFNr() throws Exception {
        when(egenAnsatt.hentErEgenAnsattEllerIFamilieMedEgenAnsatt(any(HentErEgenAnsattEllerIFamilieMedEgenAnsattRequest.class)))
                .thenReturn(new HentErEgenAnsattEllerIFamilieMedEgenAnsattResponse());

        egenAnsattConsumer.isEgenAnsatt(TEST_FNR);

        ArgumentCaptor<HentErEgenAnsattEllerIFamilieMedEgenAnsattRequest> captor = ArgumentCaptor.forClass(HentErEgenAnsattEllerIFamilieMedEgenAnsattRequest.class);
        verify(egenAnsatt).hentErEgenAnsattEllerIFamilieMedEgenAnsatt(captor.capture());

        assertThat(captor.getValue().getIdent(), is(equalTo(TEST_FNR)));
    }
}