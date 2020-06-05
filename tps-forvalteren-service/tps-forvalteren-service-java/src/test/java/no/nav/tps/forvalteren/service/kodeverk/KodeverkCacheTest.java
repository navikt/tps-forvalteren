package no.nav.tps.forvalteren.service.kodeverk;

import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;

import java.util.ArrayList;
import java.util.List;
import org.junit.Test;

import no.nav.tps.forvalteren.domain.rs.kodeverk.Kode;

public class KodeverkCacheTest {
    
    @Test
    public void testAtClearCacheTommerBaadeMapOgListeOverKommunekoder() {
        
        KodeverkCache kodeverkCache = new KodeverkCache();
        
        List<Kode> listeKode = new ArrayList<>();
        listeKode.add(new Kode());
        kodeverkCache.setKodeverkKommuneKoder(listeKode);
        
        kodeverkCache.getKodeverkKommunerMap().put("test", new Kode());
        
        assertThat(kodeverkCache.getKodeverkKommunerKoder(), hasSize(1));
        assertNotNull(kodeverkCache.getKodeverkKommunerMap().get("test"));
        
        kodeverkCache.clearKommuneCache();
        
        assertThat(kodeverkCache.getKodeverkKommunerKoder(), hasSize(0));
        assertNull(kodeverkCache.getKodeverkKommunerMap().get("test"));
    }
    
    @Test
    public void testAtClearPostnummerCacheTommerBaadeMapOgListeOverPostnummerkoder() {
        
        KodeverkCache kodeverkCache = new KodeverkCache();
        
        List<Kode> listeKode = new ArrayList<>();
        listeKode.add(new Kode());
        kodeverkCache.setKodeverkPostnummerKoder(listeKode);
        
        kodeverkCache.getKodeverkPostnummerMap().put("test", new Kode());
        
        assertThat(kodeverkCache.getKodeverkPostnummerKoder(), hasSize(1));
        assertNotNull(kodeverkCache.getKodeverkPostnummerMap().get("test"));
        
        kodeverkCache.clearPostnummerCache();
        
        assertThat(kodeverkCache.getKodeverkPostnummerKoder(), hasSize(0));
        assertNull(kodeverkCache.getKodeverkPostnummerMap().get("test"));
    }
    
    @Test
    public void testAtClearLandkoderCacheTommerBaadeMapOgListeOverLandkoder() {
        
        KodeverkCache kodeverkCache = new KodeverkCache();
        
        List<Kode> listeKode = new ArrayList<>();
        listeKode.add(new Kode());
        kodeverkCache.setKodeverkLandkoder(listeKode);
        
        kodeverkCache.getKodeverkLandkoderMap().put("test", new Kode());
        
        assertThat(kodeverkCache.getKodeverkLandkoder(), hasSize(1));
        assertNotNull(kodeverkCache.getKodeverkLandkoderMap().get("test"));
        
        kodeverkCache.clearLandkoderCache();
        
        assertThat(kodeverkCache.getKodeverkLandkoder(), hasSize(0));
        assertNull(kodeverkCache.getKodeverkLandkoderMap().get("test"));
    }
    
}