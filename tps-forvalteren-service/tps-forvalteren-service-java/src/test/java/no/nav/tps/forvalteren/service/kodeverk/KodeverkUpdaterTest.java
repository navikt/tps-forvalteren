package no.nav.tps.forvalteren.service.kodeverk;

import no.nav.tps.forvalteren.consumer.ws.kodeverk.KodeverkConsumer;
import no.nav.tps.forvalteren.domain.ws.kodeverk.Kode;
import no.nav.tps.forvalteren.domain.ws.kodeverk.Kodeverk;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InOrder;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.junit.Assert.assertThat;
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
        
        InOrder inOrder = Mockito.inOrder(kodeverkCacheMock);
        inOrder.verify(kodeverkCacheMock).clearKommuneCache();
        inOrder.verify(kodeverkCacheMock).setKodeverkKommuneKoder(koder);
        inOrder.verify(kodeverkCacheMock).clearPostnummerCache();
        inOrder.verify(kodeverkCacheMock).setKodeverkPostnummerKoder(koder);
        inOrder.verify(kodeverkCacheMock).clearLandkoderCache();
        inOrder.verify(kodeverkCacheMock).setKodeverkLandkoder(koder);
    }

    @Test
    public void hvisKodeverkIKKEKanBliHentetSaaClearesIkkeCache () {
        Kode kodeTest = new Kode();
        koder.add(kodeTest);

        when(kodeverkConsumerMock.hentKodeverk(anyString())).thenReturn(null);

        kodeverkUpdater.updateTpsfKodeverkCache();
        
        verify(kodeverkCacheMock, never()).clearKommuneCache();
        verify(kodeverkCacheMock, never()).setKodeverkKommuneKoder(any());
        verify(kodeverkCacheMock, never()).clearPostnummerCache();
        verify(kodeverkCacheMock, never()).setKodeverkPostnummerKoder(any());
    }


}