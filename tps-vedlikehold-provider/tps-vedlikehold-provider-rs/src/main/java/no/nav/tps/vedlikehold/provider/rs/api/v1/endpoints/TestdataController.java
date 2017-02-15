package no.nav.tps.vedlikehold.provider.rs.api.v1.endpoints;

//import no.nav.tps.vedlikehold.domain.rs.testdata.RsTestDataRequest;

import no.nav.tps.vedlikehold.domain.service.tps.servicerutiner.requests.TpsRequestContext;
import no.nav.tps.vedlikehold.domain.service.tps.servicerutiner.response.TpsServiceRoutineResponse;
import no.nav.tps.vedlikehold.domain.service.tps.testdata.TestDataRequest;
import no.nav.tps.vedlikehold.provider.rs.security.user.UserContextHolder;
import no.nav.tps.vedlikehold.service.command.testdata.SkdMeldingFormatter;
import no.nav.tps.vedlikehold.service.command.testdata.TPSFodselsnummerFetcher;
import no.nav.tps.vedlikehold.service.command.testdata.TestDataRequestMapper;
import no.nav.tps.vedlikehold.service.command.testdata.testperson.TestPersonCreator;
import no.nav.tps.vedlikehold.service.command.testdata.testperson.TestPersonFetcher;
import no.nav.tps.vedlikehold.service.command.tps.TpsRequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.jms.JMSException;
import java.util.Arrays;
import java.util.List;
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
    private TPSFodselsnummerFetcher TPSFodselsnummerFetcher;

    @Autowired
    private TestDataRequestMapper testDataRequestMapper;

    @Autowired
    private TestPersonCreator testPersonCreator;

    @Autowired
    private TestPersonFetcher testPersonFetcher;

    //TODO Husk å sette på CSFR
    @RequestMapping(value = "/testdata/skdcreate", method = RequestMethod.POST)
    public String createTestData(@RequestParam(required = false) Map<String, String> skdMeldingParameters){

        String skdMelding = skdMeldingFormatter.convertToSkdMeldingInnhold(skdMeldingParameters);

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
    public List<TpsServiceRoutineResponse> hentUbrukteFodselsnummerFraTPS(@RequestParam(required = false) Map<String, Object> tpsRequestParameters){
        TestDataRequest testDataRequest = testDataRequestMapper.mapParametersToTestDataRequest(tpsRequestParameters);
        TpsRequestContext tpsRequestContext = getTpsRequestContext(tpsRequestParameters);
        return TPSFodselsnummerFetcher.hentUbrukteFodselsnummereFraTPS(tpsRequestParameters, tpsRequestContext,testDataRequest);
    }


    @RequestMapping(value = "/testdata/sattfodsel", method = RequestMethod.GET)
    public TpsServiceRoutineResponse sjekkMotTPSOgReturnerDeUbrukteFodselsnummerene(@RequestParam(required = false) Map<String, Object> tpsRequestParameters){
        String fodselsnummer =  tpsRequestParameters.get("fodselsnummer").toString();
        TpsRequestContext tpsRequestContext = getTpsRequestContext(tpsRequestParameters);
        return TPSFodselsnummerFetcher.sjekkTilgjengelighetAvFodselsnummere(tpsRequestParameters, tpsRequestContext, Arrays.asList(fodselsnummer.split(";")));
    }

    private TpsRequestContext getTpsRequestContext(Map<String, Object> tpsRequestParameters){
        TpsRequestContext context = new TpsRequestContext();
        context.setUser(userContextHolder.getUser());
        context.setEnvironment(tpsRequestParameters.get("environment").toString());
        return context;
    }

}
