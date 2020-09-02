package no.nav.tps.forvalteren.provider.rs.api.v1.endpoints;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import no.nav.tps.forvalteren.common.java.logging.LogExceptions;
import no.nav.tps.forvalteren.domain.rs.kodeverk.Kode;
import no.nav.tps.forvalteren.service.kodeverk.KodeverkCache;

@RestController
@RequestMapping(value = "api/v1/kodeverk")
@ConditionalOnProperty(prefix = "tps.forvalteren", name = "production.mode", havingValue = "false")
public class KodeverkController {

    @Autowired
    private KodeverkCache kodeverkCache;

    @LogExceptions
    @GetMapping(value = "/knr")
    public List<Kode> getKodeverkKommuner() {
        return kodeverkCache.getKodeverkKommunerKoder();
    }

    @LogExceptions
    @GetMapping(value = "/postnummer")
    public List<Kode> getKodeverkPostnummer() {
        return kodeverkCache.getKodeverkPostnummerKoder();
    }

    @LogExceptions
    @GetMapping(value = "/landkoder")
    public List<Kode> getKodeverkLandkoder() {
        return kodeverkCache.getKodeverkLandkoder();
    }

}
