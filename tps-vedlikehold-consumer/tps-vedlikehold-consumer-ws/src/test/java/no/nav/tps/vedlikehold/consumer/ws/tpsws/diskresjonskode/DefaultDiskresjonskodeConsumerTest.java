package no.nav.tps.vedlikehold.consumer.ws.tpsws.diskresjonskode;

import no.nav.tjeneste.pip.diskresjonskode.DiskresjonskodePortType;
import no.nav.tjeneste.pip.diskresjonskode.meldinger.HentDiskresjonskodeBolkRequest;
import no.nav.tjeneste.pip.diskresjonskode.meldinger.HentDiskresjonskodeRequest;
import no.nav.tjeneste.pip.diskresjonskode.meldinger.HentDiskresjonskodeResponse;
import no.nav.tps.vedlikehold.consumer.ws.tpsws.exceptions.FNrEmptyException;
import no.nav.tps.vedlikehold.consumer.ws.tpsws.exceptions.PersonNotFoundException;
import org.hamcrest.CoreMatchers;
import org.hamcrest.Matcher;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.everyItem;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.contains;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * @author Tobias Hansen (Visma Consulting AS).
 */
@RunWith(MockitoJUnitRunner.class)
public class DefaultDiskresjonskodeConsumerTest {

    public static final String DISKRESJONSKODE_NOT_FOUND_ERROR = "Ingen forekomster funnet";
    private static final String THE_DATABASE_DOES_NOT_ANSWER_ERROR = "Databasen svarer ikke";

    //Test users
    private static final String TEST_FNR = "11223344556";
    private static final List<String> TEST_FNR_LIST = Arrays.asList("11223344556", "99887766554");
    ;
    private static final List<String> EMPTY_TEST_FNR_LIST = Arrays.asList();
    ;

    @InjectMocks
    private DefaultDiskresjonskodeConsumer diskresjonskodeConsumer;

    @Mock
    private DiskresjonskodePortType diskresjonskodePortType;

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Test
    public void pingReturnsTrueWhenDiskresjonskodeRespondsNormally() throws Exception {
        when(diskresjonskodePortType.hentDiskresjonskode(any(HentDiskresjonskodeRequest.class))).thenReturn(new HentDiskresjonskodeResponse());

        boolean result = diskresjonskodeConsumer.ping();

        assertThat(result, is(equalTo(true)));
    }

    @Test
    public void pingReturnsTrueWhenDiskresjonskodeThrowsExceptionWithIngenForekomsterFunnetMessage() throws Exception {
        RuntimeException thrownException = new RuntimeException("Ingen forekomster funnet");
        when(diskresjonskodePortType.hentDiskresjonskode(any(HentDiskresjonskodeRequest.class))).thenThrow(thrownException);

        boolean result = diskresjonskodeConsumer.ping();

        assertThat(result, is(equalTo(true)));
    }

    @Test
    public void pingThrowsExceptionWithoutIngenForekomsterFunnetMessage() throws Exception {
        RuntimeException thrownException = new RuntimeException(THE_DATABASE_DOES_NOT_ANSWER_ERROR);
        when(diskresjonskodePortType.hentDiskresjonskode(any(HentDiskresjonskodeRequest.class))).thenThrow(thrownException);

        expectedException.expect(RuntimeException.class);
        expectedException.expectMessage(THE_DATABASE_DOES_NOT_ANSWER_ERROR);

        diskresjonskodeConsumer.ping();
    }

    @Test
    public void getDiskresjonskodeRequestIsSentWithCorrectFNr() throws Exception {
        diskresjonskodeConsumer.getDiskresjonskode(TEST_FNR);

        ArgumentCaptor<HentDiskresjonskodeRequest> captor = ArgumentCaptor.forClass(HentDiskresjonskodeRequest.class);
        verify(diskresjonskodePortType).hentDiskresjonskode(captor.capture());

        assertThat(captor.getValue().getIdent(), is(equalTo(TEST_FNR)));
    }

    @Test
    public void getDiskresjonskodeBolkRequestIsSentWithCorrectFNr() {
        diskresjonskodeConsumer.getDiskresjonskodeBolk(TEST_FNR_LIST);

        ArgumentCaptor<HentDiskresjonskodeBolkRequest> captor = ArgumentCaptor.forClass(HentDiskresjonskodeBolkRequest.class);
        verify(diskresjonskodePortType).hentDiskresjonskodeBolk(captor.capture());

        assertThat(captor.getValue().getIdentListe(), containsInAnyOrder(TEST_FNR_LIST.toArray(new String[TEST_FNR_LIST.size()])));
    }

    @Test
    public void getDiskresjonskodeThrowsFNrEmptyExceptionCalledWithEmptyString() throws Exception {
        expectedException.expect(FNrEmptyException.class);

        diskresjonskodeConsumer.getDiskresjonskode("");
    }

    @Test
    public void getDiskresjonskodeThrowsFNrEmptyExceptionCalledWithNull() throws Exception {
        expectedException.expect(FNrEmptyException.class);

        diskresjonskodeConsumer.getDiskresjonskode(null);
    }

    @Test
    public void getDiskresjonskodeBolkThrowsFNrEmptyExceptionCalledWithEmptyString() {
        expectedException.expect(FNrEmptyException.class);

        diskresjonskodeConsumer.getDiskresjonskodeBolk(EMPTY_TEST_FNR_LIST);
    }

    @Test
    public void getDiskresjonskodeBolkThrowsFNrEmptyExceptionCalledWithNull() throws java.lang.Exception {
        expectedException.expect(FNrEmptyException.class);

        diskresjonskodeConsumer.getDiskresjonskodeBolk(null);
    }

//    @Test
//    public void getDiskresjonskodeReturnsPersonNotFoundExceptionIfCalledWithFnrNotInDiskresjonsDatabasen() throws Exception {
//        RuntimeException thrownException = new PersonNotFoundException("00000000000", new Throwable());
//        when(diskresjonskodePortType.hentDiskresjonskode(any(HentDiskresjonskodeRequest.class))).thenThrow(thrownException);
//
//        expectedException.expect(PersonNotFoundException.class);
//
//        diskresjonskodeConsumer.getDiskresjonskode(TEST_FNR);
//    }
}
