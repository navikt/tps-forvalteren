package no.nav.tps.forvalteren.consumer.ws.kodeverk;

import ma.glasnost.orika.MapperFacade;
import no.nav.tjeneste.virksomhet.kodeverk.v2.HentKodeverkHentKodeverkKodeverkIkkeFunnet;
import no.nav.tjeneste.virksomhet.kodeverk.v2.KodeverkPortType;
import no.nav.tjeneste.virksomhet.kodeverk.v2.informasjon.Kodeverk;
import no.nav.tjeneste.virksomhet.kodeverk.v2.meldinger.FinnKodeverkListeRequest;
import no.nav.tjeneste.virksomhet.kodeverk.v2.meldinger.FinnKodeverkListeResponse;
import no.nav.tjeneste.virksomhet.kodeverk.v2.meldinger.HentKodeverkRequest;
import no.nav.tjeneste.virksomhet.kodeverk.v2.meldinger.HentKodeverkResponse;
import no.nav.tps.forvalteren.common.java.message.MessageProvider;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.List;

import static no.nav.tps.forvalteren.common.java.message.MessageConstants.KODEVERK_NOT_FOUND_KEY;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsSame.sameInstance;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class KodeverkConsumerTest {
    private static final String NAVN = "navn";

    @Mock
    private KodeverkPortType kodeverkWsMock;

    @Mock
    private MapperFacade mapperMock;

    @Mock
    private MessageProvider messageProviderMock;

    @Mock
    private Kodeverk externalKodeverkMock;

    @Mock
    private no.nav.tps.forvalteren.domain.ws.kodeverk.Kodeverk internalKodeverkMock;

    @Mock
    private List<no.nav.tjeneste.virksomhet.kodeverk.v2.informasjon.finnkodeverkliste.Kodeverk> listeKodeverkMock;

    @InjectMocks
    private KodeverkConsumer consumer;

    @Before
    public void before() throws Exception {
        HentKodeverkResponse hentResponse = mock(HentKodeverkResponse.class);
        when(kodeverkWsMock.hentKodeverk(any(HentKodeverkRequest.class))).thenReturn(hentResponse);
        when(hentResponse.getKodeverk()).thenReturn(externalKodeverkMock);
        when(mapperMock.map(externalKodeverkMock, no.nav.tps.forvalteren.domain.ws.kodeverk.Kodeverk.class)).thenReturn(internalKodeverkMock);

        FinnKodeverkListeResponse finnResponse = mock(FinnKodeverkListeResponse.class);
        when(finnResponse.getKodeverkListe()).thenReturn(listeKodeverkMock);
        when(kodeverkWsMock.finnKodeverkListe(any(FinnKodeverkListeRequest.class))).thenReturn(finnResponse);
    }

    @Test
    public void hentKodeverkCallsWsWithKodeverkNavn() throws Exception {
        consumer.hentKodeverk(NAVN);

        ArgumentCaptor<HentKodeverkRequest> captor = ArgumentCaptor.forClass(HentKodeverkRequest.class);
        verify(kodeverkWsMock).hentKodeverk(captor.capture());

        assertThat(captor.getValue().getNavn(), is(NAVN));
        assertThat(captor.getValue().getSpraak(), is(nullValue()));
        assertThat(captor.getValue().getVersjonsnummer(), is(nullValue()));
    }

    @Test
    public void hentKodeverkReturnsMappedResult() {
        no.nav.tps.forvalteren.domain.ws.kodeverk.Kodeverk result = consumer.hentKodeverk(NAVN);

        verify(mapperMock).map(externalKodeverkMock, no.nav.tps.forvalteren.domain.ws.kodeverk.Kodeverk.class);
        assertThat(result, is(sameInstance(internalKodeverkMock)));
    }

    @Test
    public void hentKodeverkReturnsNullWhenKodeverkIsNotFound() throws Exception {
        when(kodeverkWsMock.hentKodeverk(any(HentKodeverkRequest.class))).thenThrow(new HentKodeverkHentKodeverkKodeverkIkkeFunnet(null, null));

        no.nav.tps.forvalteren.domain.ws.kodeverk.Kodeverk result = consumer.hentKodeverk(NAVN);

        assertThat(result, is(nullValue()));
    }

    @Test
    public void hentKodeverkCallsMessageProviderWhenKodeverkIsNotFound() throws Exception {
        when(kodeverkWsMock.hentKodeverk(any(HentKodeverkRequest.class))).thenThrow(new HentKodeverkHentKodeverkKodeverkIkkeFunnet(null, null));

        consumer.hentKodeverk(NAVN);

        verify(messageProviderMock).get(KODEVERK_NOT_FOUND_KEY, NAVN);
    }

    @Test
    public void pingCallsPingOnWs() {
        consumer.ping();

        verify(kodeverkWsMock).ping();
    }
}