package no.nav.tps.vedlikehold.service.command.testdata;

import com.fasterxml.jackson.databind.JsonNode;
import no.nav.tps.vedlikehold.domain.service.tps.servicerutiner.requests.TpsRequestContext;
import no.nav.tps.vedlikehold.domain.service.tps.servicerutiner.requests.TpsServiceRoutineRequest;
import no.nav.tps.vedlikehold.domain.service.tps.servicerutiner.response.TpsServiceRoutineResponse;
import no.nav.tps.vedlikehold.domain.service.tps.testdata.TestDataRequest;
import no.nav.tps.vedlikehold.service.command.tps.servicerutiner.TpsRequestSender;
import no.nav.tps.vedlikehold.service.command.tps.servicerutiner.utils.RsTpsRequestMappingUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by Peter Fløgstad on 23.01.2017.
 */

@Service
public class TPSFodselsnummerFetcher {

    private static final String SERVICE_NAVN_SJEKK_FNR = "FS03-FDLISTER-DISKNAVN-M";
    private static final String PARAM_NAVN_AKSJONSKODE = "aksjonsKode";
    private static final String PARAM_NAVN_RUTINENAVN = "serviceRutinenavn";
    private static final String PARAM_NAVN_ANTALL_FNR_ONSKET = "antall";
    private static final String PARAM_NAVN_FODSELSNUMMER = "fnr";
    private static final String PARAM_NAVN_ANTALL_FNR = "antallFnr";
    private static final int MAX_TPS_FETCHES = 10;

    @Autowired
    private RsTpsRequestMappingUtils mappingUtils;

    @Autowired
    private FiktiveIdenterGenerator fiktiveIdenterGenerator;

    @Autowired
    private TpsRequestSender tpsRequestSender;

    public List<TpsServiceRoutineResponse> hentUbrukteFodselsnummereFraTPS(Map<String, Object> tpsRequestParameters, TpsRequestContext context, TestDataRequest testDataRequest){

        setAksjonskodeOgServicerutineNavnTilParameterene(tpsRequestParameters);

        int antallLedigeFnrOnsket = Integer.parseInt(tpsRequestParameters.get(PARAM_NAVN_ANTALL_FNR_ONSKET).toString());
        int antallLedigeFnrFunnet = 0;
        int antallKallTilTps = 0;
        HashSet<String> testedeFodselsnummer = new HashSet<>();
        ArrayList<TpsServiceRoutineResponse> tpsResponses = new ArrayList<>();

        //TODO Denne bor gjøres penere kanskje.. 10 forsok er bare satt helt random fordi tenkte at den måtte stoppe og loope en gang hvis ikke fant noe..
        while(antallLedigeFnrFunnet < antallLedigeFnrOnsket && antallKallTilTps < MAX_TPS_FETCHES) {
            List<String> fodselsnummere = fiktiveIdenterGenerator.genererFiktiveIdenter(testDataRequest);
            fodselsnummere = fodselsnummere.stream().filter(fnr -> !testedeFodselsnummer.contains(fnr)).collect(Collectors.toList());
            testedeFodselsnummer.addAll(fodselsnummere);

            TpsServiceRoutineResponse tpsResponse = hentTilgjengeligIdenterFraTPS(tpsRequestParameters, context, fodselsnummere);
            tpsResponses.add(tpsResponse);

            LinkedHashMap response = (LinkedHashMap) tpsResponse.getResponse();
            ArrayList data = (ArrayList) response.get("data");
            antallLedigeFnrFunnet = antallLedigeFnrFunnet + data.size();
            antallKallTilTps++;
        }

        return tpsResponses;
    }

    public TpsServiceRoutineResponse sjekkTilgjengelighetAvFodselsnummere(Map<String, Object> tpsRequestParameters, TpsRequestContext context, List<String> fodselsnummere){
        return hentTilgjengeligIdenterFraTPS(tpsRequestParameters, context, fodselsnummere);
    }

    //TODO Hvis antall høyere enn 80? Maks på en fetch
    private TpsServiceRoutineResponse hentTilgjengeligIdenterFraTPS(Map<String,Object> tpsRequestParameters, TpsRequestContext context, List<String> fodselsnummere){
        setAksjonskodeOgServicerutineNavnTilParameterene(tpsRequestParameters);
        tpsRequestParameters.put(PARAM_NAVN_FODSELSNUMMER, fodselsnummere);
        tpsRequestParameters.put(PARAM_NAVN_ANTALL_FNR, fodselsnummere.size());

        JsonNode parameterNode = mappingUtils.convert(tpsRequestParameters, JsonNode.class);

        TpsServiceRoutineRequest tpsServiceRoutineRequest = mappingUtils.convertToTpsServiceRoutineRequest(SERVICE_NAVN_SJEKK_FNR, parameterNode);
        TpsServiceRoutineResponse tpsResponse = tpsRequestSender.sendTpsRequest(tpsServiceRoutineRequest, context);
        return tpsResponse;

    }

    private void setAksjonskodeOgServicerutineNavnTilParameterene(Map<String, Object> tpsRequestParameters){
        tpsRequestParameters.put(PARAM_NAVN_AKSJONSKODE, "A0");
        tpsRequestParameters.put(PARAM_NAVN_RUTINENAVN, SERVICE_NAVN_SJEKK_FNR);
    }
}
