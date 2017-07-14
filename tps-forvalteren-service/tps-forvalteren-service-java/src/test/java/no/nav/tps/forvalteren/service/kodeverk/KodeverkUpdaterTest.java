package no.nav.tps.forvalteren.service.kodeverk;

import no.nav.tps.forvalteren.consumer.ws.kodeverk.KodeverkConsumer;
import no.nav.tps.forvalteren.domain.ws.kodeverk.Kode;
import no.nav.tps.forvalteren.domain.ws.kodeverk.Kodeverk;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


@RunWith(MockitoJUnitRunner.class)
public class KodeverkUpdaterTest {

    private List<Kode> koder = new ArrayList<>();

    private Kodeverk kodeverkMock;

    @Mock
    private KodeverkCache kodeverkCacheMock;

    @Mock
    private KodeverkConsumer kodeverkConsumerMock;

    @InjectMocks
    private KodeverkUpdater kodeverkUpdater;

    @Test
    public void hvisKodeverkBlirHentetSaaClearesCacheOgNyeKommunekoderBlirSatt () {
        kodeverkMock = mock(Kodeverk.class);

        Kode kodeTest = new Kode();
        koder.add(kodeTest);

        when(kodeverkMock.getKoder()).thenReturn(koder);
        when(kodeverkConsumerMock.hentKodeverk(anyString())).thenReturn(kodeverkMock);

        kodeverkUpdater.updateTpsfKodeverkCache();

        verify(kodeverkCacheMock).clearCache();
        verify(kodeverkCacheMock).setKodeverkKommuneKoder(koder);
    }

    @Test
    public void hvisKodeverkIKKEKanBliHentetSaaClearesIkkeCache () {
        Kode kodeTest = new Kode();
        koder.add(kodeTest);

        when(kodeverkConsumerMock.hentKodeverk(anyString())).thenReturn(null);

        kodeverkUpdater.updateTpsfKodeverkCache();

        verify(kodeverkCacheMock, never()).clearCache();
        verify(kodeverkCacheMock, never()).setKodeverkKommuneKoder(any());
    }


}