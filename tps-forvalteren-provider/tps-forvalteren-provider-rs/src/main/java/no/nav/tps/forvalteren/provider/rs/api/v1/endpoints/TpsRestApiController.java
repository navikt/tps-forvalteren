package no.nav.tps.forvalteren.provider.rs.api.v1.endpoints;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.annotations.ApiParam;
import no.nav.tps.forvalteren.domain.rs.restTPS.RsEndreRelasjon;
import no.nav.tps.forvalteren.domain.service.tps.servicerutiner.response.TpsServiceRoutineResponse;
import no.nav.tps.forvalteren.service.command.tps.servicerutiner.TpsResponseToJsonHandler;

import java.util.HashMap;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import static no.nav.tps.forvalteren.domain.service.tps.servicerutiner.definition.resolvers.navmeldinger.EndreRelasjon.ENDRE_RELASJON;

@RestController
@RequestMapping(value = "/api/tps")
public class TpsRestApiController {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private ServiceroutineController serviceroutineController;

    @Autowired
    private TpsResponseToJsonHandler tpsResponseToJsonHandler;

    @GetMapping("/kjerneinfo")
    public Map fetchKjerneinfoPaaPersonS610(
            @RequestParam("fnr") String fnr,
            @RequestParam(value = "aksjonsKode", defaultValue = "A0") String aksjonsKode,
            @RequestParam("environment") String environment
    ) {
        Map<String, Object> collectionOfQueryParams = extractParams(fnr, aksjonsKode, "", environment);
        TpsServiceRoutineResponse res = serviceroutineController.executeServiceRoutine(collectionOfQueryParams, "FS03-FDNUMMER-KERNINFO-O");
        return tpsResponseToJsonHandler.execute(res);
    }

    @GetMapping("/kontaktinformasjon")
    public Map fetchKontaktinformasjonPaaPersonS600(
            @RequestParam("fnr") String fnr,
            @RequestParam(value = "aksjonsKode", defaultValue = "A0") String aksjonsKode,
            @RequestParam("environment") String environment
    ) {
        Map<String, Object> collectionOfQueryParams = extractParams(fnr, aksjonsKode, "", environment);
        TpsServiceRoutineResponse res = serviceroutineController.executeServiceRoutine(collectionOfQueryParams, "FS03-FDNUMMER-KONTINFO-O");
        return tpsResponseToJsonHandler.execute(res);
    }

    @GetMapping("/inn-og-utvandring")
    public Map fetchInnOgUtvandringPaaPersonS016(
            @RequestParam(value = "aksjonsDato") String aksjonsDato,
            @RequestParam(value = "aksjonsKode", defaultValue = "A0") String aksjonsKode,
            @RequestParam("buffNr") String buffNr,
            @RequestParam("environment") String environment,
            @RequestParam("fnr") String fnr,
            @RequestParam("infoType") String infoType
    ) {
        Map<String, Object> collectionOfQueryParams = extractParams(fnr, aksjonsKode, aksjonsDato, environment);
        collectionOfQueryParams.put("buffNr", buffNr);
        collectionOfQueryParams.put("infoType", infoType);
        TpsServiceRoutineResponse res = serviceroutineController.executeServiceRoutine(collectionOfQueryParams, "FS03-FDNUMMER-SOAIHIST-O");
        return tpsResponseToJsonHandler.execute(res);
    }

    @GetMapping("/person-kort-inkl-gt-data")
    public Map fetchKortPersondataS300(
            @RequestParam("fnr") String fnr,
            @RequestParam(value = "aksjonsKode", defaultValue = "A0") String aksjonsKode,
            @RequestParam("environment") String environment
    ) {
        Map<String, Object> collectionOfQueryParams = extractParams(fnr, aksjonsKode, "", environment);
        TpsServiceRoutineResponse res = serviceroutineController.executeServiceRoutine(collectionOfQueryParams, "FS03-FDNUMMER-PIPEDATA-O");
        return tpsResponseToJsonHandler.execute(res);
    }

    @GetMapping("/gdpr-data")
    public Map fetchGDPprDataPaaPersonS301(
            @RequestParam("fnr") String fnr,
            @RequestParam(value = "aksjonsKode", defaultValue = "A0") String aksjonsKode,
            @RequestParam("environment") String environment
    ) {
        Map<String, Object> collectionOfQueryParams = extractParams(fnr, aksjonsKode, "", environment);
        TpsServiceRoutineResponse res = serviceroutineController.executeServiceRoutine(collectionOfQueryParams, "FS03-FDNUMMER-GDPRDATA-O");
        return tpsResponseToJsonHandler.execute(res);
    }

    @GetMapping("/personstatus-og-adresse-person")
    public Map fetchPersonstatusOgAdresseS018(
            @RequestParam("fnr") String fnr,
            @RequestParam(value = "aksjonsKode", defaultValue = "A0") String aksjonsKode,
            @RequestParam(value = "aksjonsDato", required = false) String aksjonsDato,
            @RequestParam(value = "adresseType", defaultValue = "ALLE") String adresseType,
            @RequestParam("environment") String environment
    ) {
        Map<String, Object> collectionOfQueryParams = extractParams(fnr, aksjonsKode, aksjonsDato, environment);
        collectionOfQueryParams.put("adresseType", adresseType);
        TpsServiceRoutineResponse res = serviceroutineController.executeServiceRoutine(collectionOfQueryParams, "FS03-FDNUMMER-PADRHIST-O");
        return tpsResponseToJsonHandler.execute(res);
    }

    @GetMapping("/adresse-historikk-linjeadresse")
    public Map fetchAdresseHistorikkLinjeS015(
            @RequestParam("fnr") String fnr,
            @RequestParam(value = "aksjonsKode", defaultValue = "A0") String aksjonsKode,
            @RequestParam(value = "aksjonsDato", required = false) String aksjonsDato,
            @RequestParam(value = "adresseType", defaultValue = "ALLE") String adresseType,
            @RequestParam("environment") String environment
    ) {
        Map<String, Object> collectionOfQueryParams = extractParams(fnr, aksjonsKode, aksjonsDato, environment);
        collectionOfQueryParams.put("adresseType", adresseType);
        TpsServiceRoutineResponse res = serviceroutineController.executeServiceRoutine(collectionOfQueryParams, "FS03-FDNUMMER-ADLIHIST-O");
        return tpsResponseToJsonHandler.execute(res);
    }

    @GetMapping("/adresse-historikk-bosted")
    public Map fetchAdresseHistorikkBostedS010(
            @RequestParam("fnr") String fnr,
            @RequestParam(value = "aksjonsKode", defaultValue = "A0", required = false) String aksjonsKode,
            @RequestParam(value = "aksjonsDato", required = false) String aksjonsDato,
            @RequestParam("environment") String environment
    ) {
        Map<String, Object> collectionOfQueryParams = extractParams(fnr, aksjonsKode, aksjonsDato, environment);
        TpsServiceRoutineResponse res = serviceroutineController.executeServiceRoutine(collectionOfQueryParams, "FS03-FDNUMMER-ADRHISTO-O");
        return tpsResponseToJsonHandler.execute(res);
    }

    @GetMapping("/personopplysninger")
    public Map fetchPersonopplysningerS004(
            @RequestParam("fnr") String fnr,
            @RequestParam(value = "aksjonsKode", defaultValue = "A0", required = false) String aksjonsKode,
            @RequestParam(value = "aksjonsDato", required = false) String aksjonsDato,
            @RequestParam("environment") String environment
    ) {
        Map<String, Object> collectionOfQueryParams = extractParams(fnr, aksjonsKode, aksjonsDato, environment);
        TpsServiceRoutineResponse res = serviceroutineController.executeServiceRoutine(collectionOfQueryParams, "FS03-FDNUMMER-PERSDATA-O");
        return tpsResponseToJsonHandler.execute(res);
    }

    @GetMapping("/fnr-dnr-historikk")
    public Map fetchFnrDnrHistrikkS011(
            @RequestParam("fnr") String fnr,
            @RequestParam(value = "aksjonsKode", defaultValue = "A0", required = false) String aksjonsKode,
            @RequestParam(value = "aksjonsDato", required = false) String aksjonsDato,
            @RequestParam("environment") String environment
    ) {
        Map<String, Object> collectionOfQueryParams = extractParams(fnr, aksjonsKode, aksjonsDato, environment);
        TpsServiceRoutineResponse res = serviceroutineController.executeServiceRoutine(collectionOfQueryParams, "FS03-FDNUMMER-FNRHISTO-O");
        return tpsResponseToJsonHandler.execute(res);
    }

    @GetMapping("/vergemaal")
    public Map fetchPersonopplysningerS137(
            @RequestParam("fnr") String fnr,
            @RequestParam(value = "aksjonsKode", defaultValue = "A0", required = false) String aksjonsKode,
            @RequestParam("environment") String environment
    ) {
        Map<String, Object> collectionOfQueryParams = extractParams(fnr, aksjonsKode, "", environment);
        TpsServiceRoutineResponse res = serviceroutineController.executeServiceRoutine(collectionOfQueryParams, "FS03-FDNUMMER-VERGMAAL-O");
        return tpsResponseToJsonHandler.execute(res);
    }

    // FS03-NAADRSOK-PERSDATA-O?aksjonsDato=2018-08-01&aksjonsKode=A0&buffNr=1&environment=u6&etternavn=Thomassen&fornavn=Jonas&sortering=Navn

    @GetMapping("/sok-persondata")
    public Map fetchSokPersondataS050(
            @RequestParam(value = "aksjonsDato", required = false) String aksjonsDato,
            @RequestParam(value = "aksjonsKode", defaultValue = "A0", required = false) String aksjonsKode,
            @RequestParam(value = "buffNr", defaultValue = "1", required = false) String buffNr,
            @RequestParam(value = "etternavn", required = false) String etternavn,
            @RequestParam(value = "fornavn", required = false) String fornavn,
            @RequestParam(value = "sortering", defaultValue = "Navn") String sortering,
            @RequestParam("environment") String environment
    ) {
        Map<String, Object> collectionOfQueryParams = new HashMap<>();
        collectionOfQueryParams.put("aksjonsKode", aksjonsKode);
        collectionOfQueryParams.put("aksjonsDato", aksjonsDato);
        collectionOfQueryParams.put("environment", environment);
        collectionOfQueryParams.put("etternavn", etternavn);
        collectionOfQueryParams.put("fornavn", fornavn);
        collectionOfQueryParams.put("buffNr", buffNr);
        collectionOfQueryParams.put("sortering", sortering);
        TpsServiceRoutineResponse res = serviceroutineController.executeServiceRoutine(collectionOfQueryParams, "FS03-NAADRSOK-PERSDATA-O");
        return tpsResponseToJsonHandler.execute(res);
    }

    @GetMapping("/personrelasjoner")
    public Map fetchPersonrelasjonerS005(
            @RequestParam("fnr") String fnr,
            @RequestParam(value = "aksjonsKode", defaultValue = "A0", required = false) String aksjonsKode,
            @ApiParam(value = "Format: 'yyyy-mm-dd'. Må være etter fødselsdato. Gylig relasjoner på denne datoen.") @RequestParam(value = "aksjonsDato", required = false) String aksjonsDato,
            @RequestParam("environment") String environment
    ) {
        Map<String, Object> collectionOfQueryParams = extractParams(fnr, aksjonsKode, aksjonsDato, environment);
        TpsServiceRoutineResponse res = serviceroutineController.executeServiceRoutine(collectionOfQueryParams, "FS03-FDNUMMER-PERSRELA-O");
        return tpsResponseToJsonHandler.execute(res);
    }

    @PutMapping("/relasjon")
    public Map endreRelasjon(@RequestBody RsEndreRelasjon req) {
        TpsServiceRoutineResponse res = serviceroutineController.executeServiceRoutine(objectMapper.convertValue(req, Map.class), ENDRE_RELASJON);
        return tpsResponseToJsonHandler.execute(res);
    }

    private Map<String, Object> extractParams(String fnr, String aksjonsKode, String aksjonsDato, String environment){
        Map<String, Object> collectionOfQueryParams = new HashMap<>();
        collectionOfQueryParams.put("aksjonsKode", aksjonsKode);
        collectionOfQueryParams.put("aksjonsDato", aksjonsDato);
        collectionOfQueryParams.put("environment", environment);
        collectionOfQueryParams.put("fnr", fnr);

        return collectionOfQueryParams;
    }
}
