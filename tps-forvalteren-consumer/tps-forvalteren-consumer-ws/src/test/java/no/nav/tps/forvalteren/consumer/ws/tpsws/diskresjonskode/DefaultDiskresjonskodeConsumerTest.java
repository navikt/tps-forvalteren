package no.nav.tps.forvalteren.consumer.ws.tpsws.diskresjonskode;

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

import no.nav.tjeneste.pip.diskresjonskode.DiskresjonskodePortType;
import no.nav.tjeneste.pip.diskresjonskode.meldinger.HentDiskresjonskodeRequest;
import no.nav.tjeneste.pip.diskresjonskode.meldinger.HentDiskresjonskodeResponse;

@RunWith(MockitoJUnitRunner.class)
public class DefaultDiskresjonskodeConsumerTest {

    private static final String THE_DATABASE_DOES_NOT_ANSWER_ERROR = "Databasen svarer ikke";
    private static final String SOAP_FAULT_ERROR                   = "Soap error";
    private static final String NO_MATCHES_FOUND_TPSWS_ERROR = "Ingen forekomster funnet";
    private static final String INVALID_FNR_TPSWS_ERROR = "FØDSELSNUMMER INNGITT ER UGYLDIG";

    //Test users
    private static final String TEST_FNR            = "11223344556";

    @Mock
    private DiskresjonskodePortType diskresjonskodePortType;

    @Mock
    private SOAPFaultException soapFaultException;

    @InjectMocks
    private DefaultDiskresjonskodeConsumer diskresjonskodeConsumer;

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Test
    public void pingReturnsTrueWhenDiskresjonskodeRespondsNormally() throws Exception {
        when(diskresjonskodePortType.hentDiskresjonskode(any(HentDiskresjonskodeRequest.class))).thenReturn(new HentDiskresjonskodeResponse());

        boolean result = diskresjonskodeConsumer.ping();

        assertThat(result, is(equalTo(true)));
    }

    @Test
    public void pingThrowsExceptionWhenGetDiskresjonskodeThrowsException() throws Exception {
        RuntimeException thrownException = new RuntimeException(THE_DATABASE_DOES_NOT_ANSWER_ERROR);

        when(diskresjonskodePortType.hentDiskresjonskode(any(HentDiskresjonskodeRequest.class))).thenThrow(thrownException);

        expectedException.expect(RuntimeException.class);
        expectedException.expectMessage(THE_DATABASE_DOES_NOT_ANSWER_ERROR);

        diskresjonskodeConsumer.ping();
    }

    @Test
    public void pingThrowsExceptionWhenIsEgenAnsattThrowsUncaughtSOAPFaultException() throws Exception {
        when(soapFaultException.getMessage()).thenReturn(SOAP_FAULT_ERROR);
        when(diskresjonskodePortType.hentDiskresjonskode(any(HentDiskresjonskodeRequest.class)))
                .thenThrow(soapFaultException);

        expectedException.expect(SOAPFaultException.class);

        diskresjonskodeConsumer.ping();
    }

    @Test
    public void getDiskresjonskodeRequestIsSentWithCorrectFNr() {
        diskresjonskodeConsumer.getDiskresjonskodeResponse(TEST_FNR);

        ArgumentCaptor<HentDiskresjonskodeRequest> captor = ArgumentCaptor.forClass(HentDiskresjonskodeRequest.class);
        verify(diskresjonskodePortType).hentDiskresjonskode(captor.capture());

        assertThat(captor.getValue().getIdent(), is(equalTo(TEST_FNR)));
    }

    @Test
    public void getDiskresjonskodeReturnsWithoutDiskresjonskodeWhenHentDiskresjonskodeThrowsInvalidFnrError() throws Exception {
        when(soapFaultException.getMessage()).thenReturn(INVALID_FNR_TPSWS_ERROR);
        when(diskresjonskodePortType.hentDiskresjonskode(
                any(HentDiskresjonskodeRequest.class)))
                .thenThrow(soapFaultException);

        String result = diskresjonskodeConsumer.getDiskresjonskodeResponse("0").getDiskresjonskode();

        assertThat(result, is(equalTo("")));
    }

    @Test
    public void getDiskresjonskodeReturnsWithoutDiskresjonskodeWhenHentDiskresjonskodeThrowsNoMatchFoundError() throws Exception {
        when(soapFaultException.getMessage()).thenReturn(NO_MATCHES_FOUND_TPSWS_ERROR);
        when(diskresjonskodePortType.hentDiskresjonskode(any(HentDiskresjonskodeRequest.class)))
                .thenThrow(soapFaultException);

        String result = diskresjonskodeConsumer.getDiskresjonskodeResponse("11111111111").getDiskresjonskode();

        assertThat(result, is(equalTo("")));
    }
}
