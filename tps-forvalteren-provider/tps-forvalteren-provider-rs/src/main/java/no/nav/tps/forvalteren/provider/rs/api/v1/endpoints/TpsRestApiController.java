package no.nav.tps.forvalteren.provider.rs.api.v1.endpoints;

import com.fasterxml.jackson.databind.ObjectMapper;
import no.nav.tps.forvalteren.domain.rs.restTPS.RsEndreRelasjon;
import no.nav.tps.forvalteren.domain.service.tps.servicerutiner.response.TpsServiceRoutineResponse;
import no.nav.tps.forvalteren.service.command.tps.servicerutiner.TpsResponseToJsonHandler;
import springfox.documentation.annotations.ApiIgnore;

import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import static no.nav.tps.forvalteren.domain.service.tps.servicerutiner.definition.resolvers.navmeldinger.EndreRelasjon.ENDRE_RELASJON;

@RestController()
@RequestMapping(value = "/api/tps")
public class TpsRestApiController {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private ServiceController serviceController;

    @Autowired
    private TpsResponseToJsonHandler tpsResponseToJsonHandler;

    //    public Map fetchKjerneinfoPaaPersonS610(@RequestParam(required = false) Map<String, Object> tpsRequestParameters) {
    @GetMapping("/kjerneinfo")
    public Map fetchKjerneinfoPaaPersonS610(
            @RequestParam("fnr") String fnr,
            @RequestParam("aksjonsKode") String aksjonsKode,
            @RequestParam("environment") String environment,
            @ApiIgnore @RequestParam(required = false) Map<String, Object> collectionOfQueryParams
    ) {
        TpsServiceRoutineResponse res = serviceController.getService(collectionOfQueryParams, "FS03-FDNUMMER-KERNINFO-O");
        return tpsResponseToJsonHandler.execute(res);
    }

    @GetMapping("/inn-og-utvandring")
    public Map fetchInnOgUtvandringPaaPersonS016(
            @RequestParam("aksjonsDato") String aksjonsDato,
            @RequestParam("aksjonsKode") String aksjonsKode,
            @RequestParam("buffNr") String buffNr,
            @RequestParam("environment") String environment,
            @RequestParam("fnr") String fnr,
            @RequestParam("infoType") String infoType,
            @ApiIgnore @RequestParam(required = false) Map<String, Object> collectionOfQueryParams
    ) {
        TpsServiceRoutineResponse res = serviceController.getService(collectionOfQueryParams, "FS03-FDNUMMER-SOAIHIST-O");
        return tpsResponseToJsonHandler.execute(res);
    }

    @GetMapping("/person-kort-inkl-gt-data")
    public Map fetchKortPersondataS300(
            @RequestParam("fnr") String fnr,
            @RequestParam("aksjonsKode") String aksjonsKode,
            @RequestParam("environment") String environment,
            @ApiIgnore @RequestParam(required = false) Map<String, Object> collectionOfQueryParams
    ) {
        TpsServiceRoutineResponse res = serviceController.getService(collectionOfQueryParams, "FS03-FDNUMMER-PIPEDATA-O");
        return tpsResponseToJsonHandler.execute(res);
    }

    @GetMapping("/gdpr-data")
    public Map fetchGDPprDataPaaPersonS301(
            @RequestParam("fnr") String fnr,
            @RequestParam("aksjonsKode") String aksjonsKode,
            @RequestParam("environment") String environment,
            @ApiIgnore @RequestParam(required = false) Map<String, Object> collectionOfQueryParams
    ) {
        TpsServiceRoutineResponse res = serviceController.getService(collectionOfQueryParams, "FS03-FDNUMMER-GDPRDATA-O");
        return tpsResponseToJsonHandler.execute(res);
    }

    @PutMapping("/relasjon")
    public Map endreRelasjon(@RequestBody RsEndreRelasjon req) {
        TpsServiceRoutineResponse res = serviceController.getService(objectMapper.convertValue(req, Map.class), ENDRE_RELASJON);
        return tpsResponseToJsonHandler.execute(res);
    }

}
