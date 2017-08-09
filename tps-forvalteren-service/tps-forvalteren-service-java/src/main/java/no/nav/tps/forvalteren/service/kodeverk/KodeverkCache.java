package no.nav.tps.forvalteren.service.kodeverk;

import lombok.Getter;
import no.nav.tps.forvalteren.domain.ws.kodeverk.Kode;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
@Component
public class KodeverkCache {

    private Map<String, Kode> kodeverkKommunerMap = new HashMap<>();
    private List<Kode> kodeverkKommunerKoder = new ArrayList<>();

    private Map<String, Kode> kodeverkPostnummerMap = new HashMap<>();
    private List<Kode> kodeverkPostnummerKoder = new ArrayList<>();

    public void clearKommuneCache(){
        kodeverkKommunerMap.clear();
        kodeverkKommunerKoder.clear();
    }

    public void setKodeverkKommuneKoder(List<Kode> kommunerKoder){
        kodeverkKommunerKoder = kommunerKoder;
    }

    public void clearPostnummerCache(){
        kodeverkPostnummerMap.clear();
        kodeverkPostnummerKoder.clear();
    }

    public void setKodeverkPostnummerKoder(List<Kode> postnummerKoder){
        kodeverkPostnummerKoder = postnummerKoder;
    }
}
