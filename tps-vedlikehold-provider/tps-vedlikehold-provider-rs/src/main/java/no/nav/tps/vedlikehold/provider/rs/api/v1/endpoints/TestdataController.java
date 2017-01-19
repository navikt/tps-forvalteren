package no.nav.tps.vedlikehold.provider.rs.api.v1.endpoints;

//import no.nav.tps.vedlikehold.domain.rs.testdata.RsTestDataRequest;

import com.fasterxml.jackson.databind.JsonNode;
import no.nav.tps.vedlikehold.domain.service.command.tps.servicerutiner.requests.TpsRequestContext;
import no.nav.tps.vedlikehold.domain.service.command.tps.servicerutiner.requests.TpsServiceRoutineRequest;
import no.nav.tps.vedlikehold.domain.service.command.tps.servicerutiner.response.TpsServiceRoutineResponse;
import no.nav.tps.vedlikehold.provider.rs.api.v1.endpoints.utils.RsTpsRequestMappingUtils;
import no.nav.tps.vedlikehold.provider.rs.security.user.UserContextHolder;
import no.nav.tps.vedlikehold.service.command.testdata.FiktiveIdenterGenerator;
import no.nav.tps.vedlikehold.service.command.testdata.SkdMeldingFormatter;
import no.nav.tps.vedlikehold.service.command.tps.TpsRequestService;
import no.nav.tps.vedlikehold.service.command.tps.servicerutiner.TpsRequestSender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.jms.JMSException;
import java.util.Map;

/**
 * Created by Rizwan Ali Ahmed, Visma Consulting AS.
 */
@RestController
@RequestMapping(value = "api/v1")
public class TestdataController {

    @Autowired
    private UserContextHolder userContextHolder;

    @Autowired
    private SkdMeldingFormatter skdMeldingFormatter;

    @Autowired
    private TpsRequestService tpsRequestService;

    @Autowired
    private RsTpsRequestMappingUtils mappingUtils;

    @Autowired
    private FiktiveIdenterGenerator fiktiveIdenterGenerator;

    @Autowired
    private TpsRequestSender tpsRequestSender;

    //TODO Husk å sette på CSFR
    @RequestMapping(value = "/testdata/skdcreate", method = RequestMethod.POST)
    public String createTestData(@RequestParam(required = false) Map<String, String> skdMeldingParameters){

        String skdMelding = skdMeldingFormatter.convertToSkdMeldingInnhold(skdMeldingParameters);

        //TODO Settes av  param etter testing. Eller body?
        TpsRequestContext context = new TpsRequestContext();
        context.setEnvironment("t4");

        try {
            tpsRequestService.executeSkdMeldingRequest(skdMelding, context );
        } catch (JMSException e) {
            e.printStackTrace();    //TODO Gjør om til logger
        }

        return skdMelding;
    }

    @RequestMapping(value = "/testdata/fodsel", method = RequestMethod.GET)
    public TpsServiceRoutineResponse getFodselsnummer(@RequestParam(required = false) Map<String, Object> tpsRequestParameters){
        tpsRequestParameters.put("serviceRutinenavn","FS03-FDNUMMER-FNRHISTO-M");

        //Testdata generer Fodselsnummer array. //TODO Skal generes av fiktiveIdenterGeneratoren når den er ferdig.
        String[] fnrs = {"07019233152", "07018833152", "07018933152","13058841522"};
        tpsRequestParameters.put("fnr", fnrs);
        tpsRequestParameters.put("antallFnr", fnrs.length);
        tpsRequestParameters.put("buffNr", 1);  //TODO Må kanskje loope alikvel? Da det kun er 24 res per resultat.

        JsonNode body = mappingUtils.convert(tpsRequestParameters, JsonNode.class);

        TpsRequestContext context = new TpsRequestContext();
        context.setUser(userContextHolder.getUser());
        context.setEnvironment("t4");

        TpsServiceRoutineRequest tpsServiceRoutineRequest = mappingUtils.convertToTpsServiceRoutineRequest("FS03-FDNUMMER-FNRHISTO-M", body);
        return tpsRequestSender.sendTpsRequest(tpsServiceRoutineRequest, context);


    }
}
