package no.nav.tps.forvalteren.service.kodeverk;

import lombok.Getter;
import no.nav.tps.forvalteren.domain.ws.kodeverk.Kode;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Getter
@Component
public class KodeverkCache {

    Map<String, Kode> kodeverkKommuner = new HashMap<>();

    public void clearCache(){
        kodeverkKommuner.clear();
    }
}
