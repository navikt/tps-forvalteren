package no.nav.tps.forvalteren.consumer.ws.tpsws.egenansatt;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import javax.xml.ws.soap.SOAPFaultException;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import no.nav.tjeneste.pip.egen.ansatt.v1.EgenAnsattV1;
import no.nav.tjeneste.pip.egen.ansatt.v1.WSHentErEgenAnsattEllerIFamilieMedEgenAnsattRequest;
import no.nav.tjeneste.pip.egen.ansatt.v1.WSHentErEgenAnsattEllerIFamilieMedEgenAnsattResponse;


@RunWith(MockitoJUnitRunner.class)
public class DefaultEgenAnsattConsumerTest {

    private static final String THE_DATABASE_DOES_NOT_ANSWER_ERROR = "Database connection error";
    private static final String SOAP_Fault_Error                   = "Soap error";
    private static final String TEST_FNR                           = "11223344556";
    private static final String PERSON_NOT_FOUND_TPSWS_ERROR = "PERSON IKKE FUNNET";
    private static final String INVALID_FNR_TPSWS_ERROR = "FØDSELSNUMMER INNGITT ER UGYLDIG";
    private static final String EMPTY_FNR_TPSWS_ERROR = "FNR MÅ FYLLES UT";

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
        when(soapFaultException.getMessage()).thenReturn(EMPTY_FNR_TPSWS_ERROR);
        when(egenAnsatt.hentErEgenAnsattEllerIFamilieMedEgenAnsatt(any(WSHentErEgenAnsattEllerIFamilieMedEgenAnsattRequest.class)))
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
        when(soapFaultException.getMessage()).thenReturn(INVALID_FNR_TPSWS_ERROR);
        when(egenAnsatt.hentErEgenAnsattEllerIFamilieMedEgenAnsatt(any(WSHentErEgenAnsattEllerIFamilieMedEgenAnsattRequest.class)))
                .thenThrow(soapFaultException);

        boolean result = egenAnsattConsumer.isEgenAnsatt("-1");

        assertThat(result, is(false));
    }

    @Test
    public void pingReturnsTrueWhenErEgenAnsattRespondsNormally() throws Exception {
        when(egenAnsatt.hentErEgenAnsattEllerIFamilieMedEgenAnsatt(any(WSHentErEgenAnsattEllerIFamilieMedEgenAnsattRequest.class)))
                .thenReturn(new WSHentErEgenAnsattEllerIFamilieMedEgenAnsattResponse());

        boolean result = egenAnsattConsumer.ping();

        assertThat(result, is(true));
    }

    @Test
    public void pingThrowsExceptionWhenIsEgenAnsattThrowsException() throws Exception {
        RuntimeException thrownException = new RuntimeException(THE_DATABASE_DOES_NOT_ANSWER_ERROR);

        when(egenAnsatt.hentErEgenAnsattEllerIFamilieMedEgenAnsatt(any(WSHentErEgenAnsattEllerIFamilieMedEgenAnsattRequest.class)))
                .thenThrow(thrownException);

        expectedException.expect(RuntimeException.class);
        expectedException.expectMessage(THE_DATABASE_DOES_NOT_ANSWER_ERROR);

        egenAnsattConsumer.ping();
    }

    @Test
    public void pingReturnsTrueWhenIsEgenAnsattThrowsPersonNotFoundException() throws Exception {
        when(soapFaultException.getMessage()).thenReturn(PERSON_NOT_FOUND_TPSWS_ERROR);
        when(egenAnsatt.hentErEgenAnsattEllerIFamilieMedEgenAnsatt(any(WSHentErEgenAnsattEllerIFamilieMedEgenAnsattRequest.class)))
                .thenThrow(soapFaultException);

        boolean result = egenAnsattConsumer.ping();

        assertThat(result, is(true));
    }

    @Test
    public void pingThrowsExceptionWhenIsEgenAnsattThrowsUncaughtSOAPFaultExceptionNotContaining() throws Exception {
        when(soapFaultException.getMessage()).thenReturn(SOAP_Fault_Error);
        when(egenAnsatt.hentErEgenAnsattEllerIFamilieMedEgenAnsatt(any(WSHentErEgenAnsattEllerIFamilieMedEgenAnsattRequest.class)))
                .thenThrow(soapFaultException);

        expectedException.expect(SOAPFaultException.class);

        egenAnsattConsumer.ping();
    }

    @Test
    public void hentDiskresjonskodeRequestIsSentWithCorrectFNr() throws Exception {
        when(egenAnsatt.hentErEgenAnsattEllerIFamilieMedEgenAnsatt(any(WSHentErEgenAnsattEllerIFamilieMedEgenAnsattRequest.class)))
                .thenReturn(new WSHentErEgenAnsattEllerIFamilieMedEgenAnsattResponse());

        egenAnsattConsumer.isEgenAnsatt(TEST_FNR);

        ArgumentCaptor<WSHentErEgenAnsattEllerIFamilieMedEgenAnsattRequest> captor = ArgumentCaptor.forClass(WSHentErEgenAnsattEllerIFamilieMedEgenAnsattRequest.class);
        verify(egenAnsatt).hentErEgenAnsattEllerIFamilieMedEgenAnsatt(captor.capture());

        assertThat(captor.getValue().getIdent(), is(equalTo(TEST_FNR)));
    }
}