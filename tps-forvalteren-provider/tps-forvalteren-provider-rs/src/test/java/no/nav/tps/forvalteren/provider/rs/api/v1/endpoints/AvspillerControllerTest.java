package no.nav.tps.forvalteren.provider.rs.api.v1.endpoints;

import static java.lang.Long.valueOf;
import static java.time.LocalDateTime.parse;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Collections;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import ma.glasnost.orika.MapperFacade;
import no.nav.tps.forvalteren.common.java.message.MessageProvider;
import no.nav.tps.forvalteren.consumer.rs.environments.FasitApiConsumer;
import no.nav.tps.forvalteren.consumer.rs.environments.dao.FasitResource;
import no.nav.tps.forvalteren.consumer.rs.environments.resourcetypes.FasitPropertyTypes;
import no.nav.tps.forvalteren.domain.jpa.TpsAvspiller;
import no.nav.tps.forvalteren.domain.rs.Meldingsformat;
import no.nav.tps.forvalteren.domain.rs.RsAvspillerRequest;
import no.nav.tps.forvalteren.domain.rs.skd.RsTpsAvspiller;
import no.nav.tps.forvalteren.service.command.avspiller.AvspillerDaoService;
import no.nav.tps.forvalteren.service.command.avspiller.AvspillerService;
import no.nav.tps.forvalteren.service.command.exceptions.NotFoundException;

@RunWith(MockitoJUnitRunner.class)
public class AvspillerControllerTest {

    private static final String MILJOE = "u5";
    private static final String PERIODE = "2018-01-01T00:00:00$2019-01-01T23:59:59$30";
    private static final Meldingsformat MELDINGSFORMAT = Meldingsformat.AJOURHOLDSMELDING;
    private static final String MELDINGSTYPER = "foedselsmelding,doedsmelding";
    private static final String KILDER = "SKD,TPSF";
    private static final String IDENTER = "123,456";
    private static final String BUFFERPARAMS = "1$200";
    private static final String QUEUE = "MyOwnTestQueue";
    private static final String QUEUE_NOT_FOUND = "avspiller.request.queue.check";
    private static final String QUEUE_DONT_EXIST = "Queue does not exist";
    private static final String SKD_MELDING = "TPS.ENDRINGS.MELDING";
    private static final Long BESTILLING_ID = 1L;
    private static final String MESSAGE_NUMBER = "123456";

    @Mock
    private FasitApiConsumer fasitApiConsumer;

    @Mock
    private AvspillerService avspillerService;

    @Mock
    private AvspillerDaoService avspillerDaoService;

    @Mock
    private MapperFacade mapperFacade;

    @Mock
    private MessageProvider messageProvider;

    @InjectMocks
    private AvspillerController avspillerController;

    @Test
    public void getTyperOgKilder_verifyMappingOk() {
        ArgumentCaptor<RsAvspillerRequest> request = ArgumentCaptor.forClass(RsAvspillerRequest.class);

        avspillerController.getTyperOgKilder(MILJOE, PERIODE, MELDINGSFORMAT);

        verify(avspillerService).getTyperOgKilder(request.capture());
        assertThat(request.getValue().getMiljoeFra(), is(equalTo(MILJOE)));
        assertThat(request.getValue().getDatoFra(), is(equalTo(parse(PERIODE.split("\\$")[0]))));
        assertThat(request.getValue().getDatoTil(), is(equalTo(parse(PERIODE.split("\\$")[1]))));
        assertThat(request.getValue().getTimeout(), is(equalTo(valueOf(PERIODE.split("\\$")[2]))));
        assertThat(request.getValue().getFormat(), is(equalTo(MELDINGSFORMAT)));
    }

    @Test
    public void getMeldinger_verifyMappingOk() {
        ArgumentCaptor<RsAvspillerRequest> request = ArgumentCaptor.forClass(RsAvspillerRequest.class);

        avspillerController.getMeldinger(MILJOE, PERIODE, MELDINGSFORMAT, MELDINGSTYPER, KILDER, IDENTER, BUFFERPARAMS);

        verify(avspillerService).getMeldinger(request.capture());
        assertThat(request.getValue().getMiljoeFra(), is(equalTo(MILJOE)));
        assertThat(request.getValue().getDatoFra(), is(equalTo(parse(PERIODE.split("\\$")[0]))));
        assertThat(request.getValue().getDatoTil(), is(equalTo(parse(PERIODE.split("\\$")[1]))));
        assertThat(request.getValue().getTimeout(), is(equalTo(valueOf(PERIODE.split("\\$")[2]))));
        assertThat(request.getValue().getFormat(), is(equalTo(MELDINGSFORMAT)));
        assertThat(request.getValue().getTyper(), containsInAnyOrder(MELDINGSTYPER.split(",")));
        assertThat(request.getValue().getKilder(), containsInAnyOrder(KILDER.split(",")));
        assertThat(request.getValue().getIdenter(), containsInAnyOrder(IDENTER.split(",")));
        assertThat(request.getValue().getPageNumber(), is(equalTo(valueOf(BUFFERPARAMS.split("\\$")[0]))));
        assertThat(request.getValue().getBufferSize(), is(equalTo(valueOf(BUFFERPARAMS.split("\\$")[1]))));
    }

    @Test
    public void sendTilTps_OwnQueue_Ok() {

        when(messageProvider.get(QUEUE_NOT_FOUND, QUEUE, MILJOE)).thenReturn(QUEUE_DONT_EXIST);
        when(avspillerService.isValid(MILJOE, QUEUE)).thenReturn(true);

        avspillerController.sendTilTps(RsAvspillerRequest.builder()
                .ownQueue(true)
                .miljoeFra(MILJOE)
                .miljoeTil(MILJOE)
                .queue(QUEUE)
                .build());

        verify(avspillerDaoService).save(any(RsAvspillerRequest.class));
        verify(avspillerService).isValid(MILJOE, QUEUE);
        verify(messageProvider, never()).get(QUEUE_NOT_FOUND, QUEUE, MILJOE);
        verify(avspillerService).sendTilTps(any(RsAvspillerRequest.class), any(TpsAvspiller.class));
    }

    @Test(expected = NotFoundException.class)
    public void sendTilTps_OwnQueue_DontExist() {

        when(messageProvider.get(QUEUE_NOT_FOUND, QUEUE, MILJOE)).thenReturn(QUEUE_DONT_EXIST);
        when(avspillerService.isValid(MILJOE, QUEUE)).thenReturn(false);

        avspillerController.sendTilTps(RsAvspillerRequest.builder()
                .ownQueue(true)
                .miljoeFra(MILJOE)
                .miljoeTil(MILJOE)
                .queue(QUEUE)
                .build());

        verify(avspillerDaoService).save(any(RsAvspillerRequest.class));
        verify(avspillerService).isValid(MILJOE, QUEUE);
    }

    @Test
    public void sendTilTps_FasitQueue_Ok() {

        when(messageProvider.get(QUEUE_NOT_FOUND, QUEUE, MILJOE)).thenReturn(QUEUE_DONT_EXIST);
        when(avspillerService.isValid(MILJOE, QUEUE)).thenReturn(true);

        avspillerController.sendTilTps(RsAvspillerRequest.builder()
                .ownQueue(false)
                .miljoeFra(MILJOE)
                .miljoeTil(MILJOE)
                .queue(QUEUE)
                .build());

        verify(avspillerDaoService).save(any(RsAvspillerRequest.class));
        verify(avspillerService, never()).isValid(MILJOE, QUEUE);
        verify(messageProvider, never()).get(QUEUE_NOT_FOUND, QUEUE, MILJOE);
        verify(avspillerService).sendTilTps(any(RsAvspillerRequest.class), any(TpsAvspiller.class));
    }

    @Test
    public void getMeldingskoer_Ok() {

        when(fasitApiConsumer.getResourcesByAliasAndTypeAndEnvironment(SKD_MELDING, FasitPropertyTypes.QUEUE, MILJOE.substring(1)))
                .thenReturn(Collections.singletonList(new FasitResource()));

        avspillerController.getMeldingskoer(MILJOE, MELDINGSFORMAT);

        verify(fasitApiConsumer).getResourcesByAliasAndTypeAndEnvironment(anyString(), eq(FasitPropertyTypes.QUEUE), anyString());
    }

    @Test
    public void getStatuser_Ok() {

        avspillerController.getStatuser(BESTILLING_ID);

        verify(avspillerDaoService).getStatus(BESTILLING_ID);
        verify(mapperFacade).map(any(TpsAvspiller.class), eq(RsTpsAvspiller.class));
    }

    @Test
    public void showRequest_Ok() {

        when(avspillerService.showRequest(MILJOE, MELDINGSFORMAT, MESSAGE_NUMBER)).thenReturn("Message from TPS");

        avspillerController.showRequest(MILJOE, MELDINGSFORMAT, MESSAGE_NUMBER);

        verify(avspillerService).showRequest(MILJOE, MELDINGSFORMAT, MESSAGE_NUMBER);
    }

    @Test
    public void cancelSendTilTps_Ok() {

        avspillerController.cancelSendTilTps(BESTILLING_ID);

        verify(avspillerDaoService).cancelRequest(BESTILLING_ID);
        verify(mapperFacade).map(any(TpsAvspiller.class), eq(RsTpsAvspiller.class));
    }
}