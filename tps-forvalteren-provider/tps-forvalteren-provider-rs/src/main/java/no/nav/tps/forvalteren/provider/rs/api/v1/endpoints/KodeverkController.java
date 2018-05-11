package no.nav.tps.forvalteren.provider.rs.api.v1.endpoints;

import no.nav.tps.forvalteren.domain.ws.kodeverk.Kode;
import no.nav.tps.forvalteren.service.kodeverk.KodeverkCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value = "api/v1/kodeverk")
@ConditionalOnProperty(prefix = "tps.forvalteren", name = "production-mode", havingValue = "false")
public class KodeverkController {

    @Autowired
    private KodeverkCache kodeverkCache;

    @GetMapping(value = "/knr")
    public List<Kode> getKodeverkKommuner() {
        return kodeverkCache.getKodeverkKommunerKoder();
    }

    @GetMapping(value = "/postnummer")
    public List<Kode> getKodeverkPostnummer() {
        return kodeverkCache.getKodeverkPostnummerKoder();
    }

    @GetMapping(value = "/landkoder")
    public List<Kode> getKodeverkLandkoder() {
        return kodeverkCache.getKodeverkLandkoder();
    }

}
