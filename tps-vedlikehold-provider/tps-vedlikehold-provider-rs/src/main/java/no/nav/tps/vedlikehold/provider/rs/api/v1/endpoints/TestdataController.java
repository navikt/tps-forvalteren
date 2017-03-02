package no.nav.tps.vedlikehold.provider.rs.api.v1.endpoints;

//import no.nav.tps.vedlikehold.domain.rs.testdata.RsTestDataRequest;

import no.nav.tps.vedlikehold.domain.repository.jpa.repoes.TPSKTestPersonRepository;
import no.nav.tps.vedlikehold.domain.service.jpa.TPSKTestPerson;
import no.nav.tps.vedlikehold.domain.service.tps.servicerutiner.requests.TpsRequestContext;
import no.nav.tps.vedlikehold.domain.service.tps.servicerutiner.response.TpsServiceRoutineResponse;
import no.nav.tps.vedlikehold.domain.service.tps.servicerutiner.response.TpsTestdatapersonerResponse;
import no.nav.tps.vedlikehold.domain.service.tps.testdata.TestDataRequest;
import no.nav.tps.vedlikehold.provider.rs.security.user.UserContextHolder;
import no.nav.tps.vedlikehold.service.command.testdata.SkdMeldingFormatter;
import no.nav.tps.vedlikehold.service.command.testdata.TPSFodselsnummerFetcher;
import no.nav.tps.vedlikehold.service.command.testdata.TestDataRequestMapper;
import no.nav.tps.vedlikehold.service.command.testdata.testperson.TestPersonCreator;
import no.nav.tps.vedlikehold.service.command.testdata.testperson.TestPersonFetcher;
import no.nav.tps.vedlikehold.service.command.tps.TpsRequestService;
import no.nav.tps.vedlikehold.service.command.tps.servicerutiner.utils.TpsTestdatapersonerResponseMappingUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.jms.JMSException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * Created by Rizwan Ali Ahmed, Visma Consulting AS.
 */
@RestController
@RequestMapping(value = "api/v1")
public class TestdataController {

    //TODO For rask testing
    private static final String name1 = "TestPeter";
    private static final String name2 = "TestRetep";
    private static final String slektsnavn1= "TestSlekt";
    private static final String slektsnavn2= "TestSlekt2";
    private static final String personnummer1 = "12345";
    private static final String personnummer2 = "54321";
    private static final String fodselsnummer = "070192";
    private static final String fodselsnummer2 = "070199";


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
    private TPSKTestPersonRepository tpskTestPersonRepository;

    @Autowired
    private TestPersonCreator testPersonCreator;

    @Autowired
    private TestPersonFetcher testPersonFetcher;

    @Autowired
    private TpsTestdatapersonerResponseMappingUtils testdatapersonerResponseMappingUtils;

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

    //TODO Lag et TPSTEstDataResponse for A wrappe listen
    @RequestMapping(value = "/testdata/testpersoner", method = RequestMethod.GET)
    public TpsTestdatapersonerResponse loadTestpersonerTilhorendeEnPerson(@RequestParam(required = false) Map<String, Object> tpsRequestParameters) throws IOException {

        //TODO For testing mot frontend.
        TPSKTestPerson person1 = new TPSKTestPerson();
        person1.setFornavn(name1);
        person1.setSlektsnavn(slektsnavn1);
        person1.setPersonnummer(personnummer1);
        person1.setFodselsnummer(fodselsnummer);

        TPSKTestPerson person2 = new TPSKTestPerson();
        person2.setFornavn(name2);
        person2.setSlektsnavn(slektsnavn2);
        person2.setPersonnummer(personnummer2);
        person2.setFodselsnummer(fodselsnummer2);

        return testdatapersonerResponseMappingUtils.convertToTpsServiceRutineResponse(new ArrayList<>(Arrays.asList(person1,person2)));
    }

    @RequestMapping(value = "/testdata/testpersoner", method = RequestMethod.POST)
    public TpsTestdatapersonerResponse updateTestpersons(@RequestParam(required = false) Map<String, Object> param){

        return null;
    }

    @RequestMapping(value = "/testdata/testpersoner/{testpersonId}", method = RequestMethod.PUT)
    public TpsTestdatapersonerResponse updateTestpersons(@PathVariable Integer id){

        return null;
    }

    private TpsRequestContext getTpsRequestContext(Map<String, Object> tpsRequestParameters){
        TpsRequestContext context = new TpsRequestContext();
        context.setUser(userContextHolder.getUser());
        context.setEnvironment(tpsRequestParameters.get("environment").toString());
        return context;
    }

}
