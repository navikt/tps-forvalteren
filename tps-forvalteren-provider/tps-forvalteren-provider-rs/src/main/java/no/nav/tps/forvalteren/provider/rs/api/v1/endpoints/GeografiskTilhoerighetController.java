package no.nav.tps.forvalteren.provider.rs.api.v1.endpoints;

import static no.nav.tps.forvalteren.provider.rs.config.ProviderConstants.OPERATION;
import static no.nav.tps.forvalteren.provider.rs.config.ProviderConstants.RESTSERVICE;
import static no.nav.tps.forvalteren.service.command.tps.servicerutiner.utils.ServiceroutineEnum.AKSJONSKODE;

import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import no.nav.freg.metrics.annotations.Metrics;
import no.nav.freg.spring.boot.starters.log.exceptions.LogExceptions;
import no.nav.tps.forvalteren.domain.service.tps.servicerutiner.response.TpsServiceRoutineResponse;
import no.nav.tps.forvalteren.provider.rs.security.logging.BaseProvider;
import no.nav.tps.forvalteren.service.command.tps.servicerutiner.TpsServiceRoutineService;

@RestController
@RequestMapping(value = "api/v1/gt")
@PreAuthorize("hasAnyRole('ROLE_TPSF_LES')")
public class GeografiskTilhoerighetController extends BaseProvider {

    private static final String REST_SERVICE_NAME = "gt";
    private static final String METHOD_KERNINFO = "getKerninfo";
    private static final String METHOD_ADRHIST = "getAdrhist";
    private static final String METHOD_ADRLINJHIST = "getAdrlinjhist";
    private static final String METHOD_SOAIHIST = "getSoaihist";
    private static final String KJERNEINFO_SERVICE_ROUTINE = "FS03-FDNUMMER-KERNINFO-O";
    private static final String ADRESSEHISTORIKK_SERVICE_ROUTINE = "FS03-FDNUMMER-ADRHISTO-O";
    private static final String ADRESSELINJEHISTORIKK_SERVICE_ROUTINE = "FS03-FDNUMMER-ADLIHIST-O";
    private static final String SOAIHISTORIKK_SERVICE_ROUTINE = "FS03-FDNUMMER-SOAIHIST-O";
    private static final String LOG_SPORING = "%s.%s";

    @Autowired
    private TpsServiceRoutineService tpsServiceRoutineService;

    @LogExceptions
    @Metrics(value = "provider", tags = { @Metrics.Tag(key = RESTSERVICE, value = REST_SERVICE_NAME), @Metrics.Tag(key = OPERATION, value = METHOD_KERNINFO) })
    @RequestMapping(value = "/kerninfo", method = RequestMethod.GET)
    public TpsServiceRoutineResponse getKerninfo(@RequestParam Map<String, Object> tpsRequestParameters) {
        tpsRequestParameters.put(AKSJONSKODE.getName(), "B0");
        loggSporing(String.format(LOG_SPORING, REST_SERVICE_NAME, METHOD_KERNINFO), tpsRequestParameters);
        return tpsServiceRoutineService.execute(KJERNEINFO_SERVICE_ROUTINE, tpsRequestParameters, true);
    }

    @LogExceptions
    @Metrics(value = "provider", tags = { @Metrics.Tag(key = RESTSERVICE, value = REST_SERVICE_NAME), @Metrics.Tag(key = OPERATION, value = METHOD_ADRHIST) })
    @RequestMapping(value = "/adrhist", method = RequestMethod.GET)
    public TpsServiceRoutineResponse getAdrhist(@RequestParam Map<String, Object> tpsRequestParameters) {
        tpsRequestParameters.put(AKSJONSKODE.getName(), "B0");
        loggSporing(String.format(LOG_SPORING, REST_SERVICE_NAME, METHOD_ADRHIST), tpsRequestParameters);
        return tpsServiceRoutineService.execute(ADRESSEHISTORIKK_SERVICE_ROUTINE, tpsRequestParameters, true);
    }

    @LogExceptions
    @Metrics(value = "provider", tags = { @Metrics.Tag(key = RESTSERVICE, value = REST_SERVICE_NAME), @Metrics.Tag(key = OPERATION, value = METHOD_ADRLINJHIST) })
    @RequestMapping(value = "/adrlinjhist", method = RequestMethod.GET)
    public TpsServiceRoutineResponse getAdrlinjhist(@RequestParam Map<String, Object> tpsRequestParameters) {
        tpsRequestParameters.put(AKSJONSKODE.getName(), "A0");
        tpsRequestParameters.put("adresseType", "ALLE");
        loggSporing(String.format(LOG_SPORING, REST_SERVICE_NAME, METHOD_ADRLINJHIST), tpsRequestParameters);
        return tpsServiceRoutineService.execute(ADRESSELINJEHISTORIKK_SERVICE_ROUTINE, tpsRequestParameters, true);
    }

    @LogExceptions
    @Metrics(value = "provider", tags = { @Metrics.Tag(key = RESTSERVICE, value = REST_SERVICE_NAME), @Metrics.Tag(key = OPERATION, value = METHOD_SOAIHIST) })
    @RequestMapping(value = "/soaihist", method = RequestMethod.GET)
    public TpsServiceRoutineResponse getSoaihist(@RequestParam Map<String, Object> tpsRequestParameters) {
        tpsRequestParameters.put(AKSJONSKODE.getName(), "A0");
        tpsRequestParameters.put("infoType", "ALLE");
        tpsRequestParameters.put("buffNr", "1");
        loggSporing(String.format(LOG_SPORING, REST_SERVICE_NAME, METHOD_SOAIHIST), tpsRequestParameters);
        return tpsServiceRoutineService.execute(SOAIHISTORIKK_SERVICE_ROUTINE, tpsRequestParameters, true);
    }
}