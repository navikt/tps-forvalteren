package no.nav.tps.forvalteren.config;

import no.nav.tps.forvalteren.service.command.tps.skdmelding.skdparam.utils.LandkodeEncoder;

import java.util.HashMap;
import java.util.Map;
import org.springframework.stereotype.Service;

/*
    Generert fra "Landkoder TPS FELLES og TSS 20170209.xlsx" - "TPS Felles"
 */
@Service
@SuppressWarnings("checkstyle:com.puppycrawl.tools.checkstyle.checks.metrics.JavaNCSSCheck")
public class TestLandkodeEncoder extends LandkodeEncoder {

    private static final Map<String, String> landkoderMap = new HashMap<>();

    static {
        landkoderMap.put("SWE", "106");
    }

    @Override
    public String getRandomLandTla() {
        return (String) landkoderMap.keySet().toArray()[0];
    }
}
