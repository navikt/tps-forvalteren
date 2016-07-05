package no.nav.tps.vedlikehold.consumer.ws.tpsws.diskresjonskode;

import no.nav.tjeneste.pip.diskresjonskode.DiskresjonskodePortType;
import no.nav.tjeneste.pip.diskresjonskode.meldinger.HentDiskresjonskodeRequest;
import no.nav.tjeneste.pip.diskresjonskode.meldinger.HentDiskresjonskodeResponse;
import no.nav.tps.vedlikehold.consumer.ws.tpsws.exceptions.FNrEmptyException;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static com.sun.org.apache.xerces.internal.util.PropertyState.is;
import static no.nav.tps.vedlikehold.consumer.ws.tpsws.diskresjonskode.DefaultDiskresjonskodeConsumer.DISKRESJONSKODE_NOT_FOUND_ERROR;
import static no.nav.tps.vedlikehold.consumer.ws.tpsws.diskresjonskode.DefaultDiskresjonskodeConsumer.THE_DATABASE_DOES_NOT_ANSWER_ERROR;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * @author Tobias Hansen (Visma Consulting AS).
 */
@RunWith(MockitoJUnitRunner.class)
public class DefaultDiskresjonskodeConsumerTest {

    public static final String DISKRESJONSKODE_NOT_FOUND_ERROR = "Ingen forekomster funnet";

    private static final String TEST_FNR = "11223344556";

    @InjectMocks
    private DefaultDiskresjonskodeConsumer consumer;

    @Mock
    private DiskresjonskodePortType diskresjonskodePortType;

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Test
    public void pingReturnsTrueWhenDiskresjonskodeRespondsNormally() throws Exception {
        when(diskresjonskodePortType.hentDiskresjonskode(any(HentDiskresjonskodeRequest.class))).thenReturn(new HentDiskresjonskodeResponse());

        boolean result = consumer.ping();

        assertThat(result, is(equalTo(true)));
    }

    @Test
    public void pingReturnsTrueWhenDiskresjonskodeThrowsExceptionWithIngenForekomsterFunnetMessage() throws Exception {
        RuntimeException thrownException = new RuntimeException(DISKRESJONSKODE_NOT_FOUND_ERROR);
        when(diskresjonskodePortType.hentDiskresjonskode(any(HentDiskresjonskodeRequest.class))).thenThrow(thrownException);

        boolean result = consumer.ping();

        assertThat(result, is(equalTo(true)));
    }

    @Test
    public void pingThrowsExceptionWithoutIngenForekomsterFunnetMessage() throws Exception {
        RuntimeException thrownException = new RuntimeException(THE_DATABASE_DOES_NOT_ANSWER_ERROR);
        when(diskresjonskodePortType.hentDiskresjonskode(any(HentDiskresjonskodeRequest.class))).thenThrow(thrownException);

        expectedException.expect(RuntimeException.class);
        expectedException.expectMessage(THE_DATABASE_DOES_NOT_ANSWER_ERROR);

        consumer.ping();
    }

    @Test
    public void hentDiskresjonskodeRequestIsSentWithCorrectFNr() throws Exception {
        consumer.getDiskresjonskode(TEST_FNR);

        ArgumentCaptor<HentDiskresjonskodeRequest> captor = ArgumentCaptor.forClass(HentDiskresjonskodeRequest.class);
        verify(diskresjonskodePortType).hentDiskresjonskode(captor.capture());

        assertThat(captor.getValue().getIdent(), is(equalTo(TEST_FNR)));
    }

    @Test
    public void isEgenAnsattThrowsFNrEmptyExceptionCalledWithEmptyString() throws Exception {
        expectedException.expect(FNrEmptyException.class);

        consumer.getDiskresjonskode("");
    }

    @Test
    public void isEgenAnsattThrowsFNrEmptyExceptionCalledWithNull() throws Exception {
        expectedException.expect(FNrEmptyException.class);

        consumer.getDiskresjonskode(null);
    }
}
