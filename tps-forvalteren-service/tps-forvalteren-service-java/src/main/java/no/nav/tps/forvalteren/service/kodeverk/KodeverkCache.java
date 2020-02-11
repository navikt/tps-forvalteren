package no.nav.tps.forvalteren.service.kodeverk;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Component;

import lombok.Getter;
import no.nav.tps.forvalteren.domain.ws.kodeverk.Kode;

@Getter
@Component
public class KodeverkCache {
    
    private Map<String, Kode> kodeverkKommunerMap = new HashMap<>();
    private List<Kode> kodeverkKommunerKoder = new ArrayList<>();
    
    private Map<String, Kode> kodeverkPostnummerMap = new HashMap<>();
    private List<Kode> kodeverkPostnummerKoder = new ArrayList<>();
    
    private Map<String, Kode> kodeverkLandkoderMap = new HashMap<>();
    private List<Kode> kodeverkLandkoder = new ArrayList<>();
    
    public void clearKommuneCache() {
        kodeverkKommunerMap.clear();
        kodeverkKommunerKoder.clear();
    }
    
    public void setKodeverkKommuneKoder(List<Kode> kommunerKoder) {
        kodeverkKommunerKoder = kommunerKoder;
    }
    
    public void clearPostnummerCache() {
        kodeverkPostnummerMap.clear();
        kodeverkPostnummerKoder.clear();
    }
    
    public void setKodeverkPostnummerKoder(List<Kode> postnummerKoder) {
        kodeverkPostnummerKoder = postnummerKoder;
    }
    
    public void clearLandkoderCache() {
        kodeverkLandkoderMap.clear();
        kodeverkLandkoder.clear();
    }
    
    public void setKodeverkLandkoder(List<Kode> kodeverkLandkoder) {
        this.kodeverkLandkoder = kodeverkLandkoder;
    }
}
