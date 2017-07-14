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

    Map<String, Kode> kodeverkKommunerMap = new HashMap<>();
    List<Kode> kodeverkKommunerKoder = new ArrayList<>();

    public void clearCache(){
        kodeverkKommunerMap.clear();
        kodeverkKommunerKoder.clear();
    }

    public void setKodeverkKommuneKoder(List<Kode> kommunerKoder){
        kodeverkKommunerKoder = kommunerKoder;
    }
}
