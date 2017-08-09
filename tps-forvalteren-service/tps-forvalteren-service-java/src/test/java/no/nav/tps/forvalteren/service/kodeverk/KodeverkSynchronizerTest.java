package no.nav.tps.forvalteren.service.kodeverk;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class KodeverkSynchronizerTest {

    @Mock
    private KodeverkUpdater updaterMock;

    @InjectMocks
    private KodeverkSynchronizer synchronizer;

    @Test
    public void onApplicationEventCallsUpdateKodeverkOnKodeverkUpdater() {
        synchronizer.onApplicationEvent(null);

        verify(updaterMock).updateTpsfKodeverkCache();
    }

    @Test
    public void onApplicationEventCallsUpdateKodeverkOnKodeverkUpdaterOnlyOnce() {
        synchronizer.onApplicationEvent(null);
        synchronizer.onApplicationEvent(null);

        verify(updaterMock, times(1)).updateTpsfKodeverkCache();
    }

    @Test
    public void updateKodeverkCallsUpdateKodeverkInKodeverkUpdater() {
        synchronizer.updateKodeverk();

        verify(updaterMock).updateTpsfKodeverkCache();
    }

    @Test
    public void updateKodeverkCatchesException() {
        doThrow(new IllegalArgumentException()).when(updaterMock).updateTpsfKodeverkCache();

        synchronizer.updateKodeverk();
    }
}