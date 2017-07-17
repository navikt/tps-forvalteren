package no.nav.tps.forvalteren.service.kodeverk;

import no.nav.tps.forvalteren.domain.ws.kodeverk.Kode;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;

public class KodeverkCacheTest {

    @Test
    public void testAtClearCacheTommerBaadeMapOgListeOverKommunekoder(){

        KodeverkCache kodeverkCache = new KodeverkCache();

        List<Kode> listeKode = new ArrayList<>();
        listeKode.add(new Kode());
        kodeverkCache.setKodeverkKommuneKoder(listeKode);

        kodeverkCache.getKodeverkKommunerMap().put("test", new Kode());

        assertThat(kodeverkCache.getKodeverkKommunerKoder(), hasSize(1));
        assertNotNull(kodeverkCache.getKodeverkKommunerMap().get("test"));

        kodeverkCache.clearCache();

        assertThat(kodeverkCache.getKodeverkKommunerKoder(), hasSize(0));
        assertNull(kodeverkCache.getKodeverkKommunerMap().get("test"));
    }

}