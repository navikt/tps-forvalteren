package no.nav.tps.forvalteren.service.command.testdata;

import no.nav.tps.forvalteren.domain.service.tps.servicerutiner.requests.TpsRequestContext;
import no.nav.tps.forvalteren.domain.service.tps.servicerutiner.requests.TpsServiceRoutineRequest;
import no.nav.tps.forvalteren.domain.service.tps.servicerutiner.response.TpsServiceRoutineResponse;
import no.nav.tps.forvalteren.service.command.tps.servicerutiner.TpsRequestSender;
import no.nav.tps.forvalteren.service.command.tps.servicerutiner.utils.RsTpsRequestMappingUtils;
import no.nav.tps.forvalteren.service.user.UserContextHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
public class FilterPaaIdenterTilgjengeligeIMiljo {

    private static final int HIGHEST_T_ENVIRONMENT = 13;
    private static final int LOWEST_T_ENVIRONMENT = 0;

    @Autowired
    private UserContextHolder userContextHolder;

    @Autowired
    private TpsRequestSender tpsRequestSender;

    @Autowired
    private RsTpsRequestMappingUtils mappingUtils;

    public List<String> filtrer(List<String> identer){

        Map<String, Object> tpsRequestParameters = opprettParametereForM201TpsRequest(identer);

        TpsRequestContext context = new TpsRequestContext();
        context.setUser(userContextHolder.getUser());

        return hentIdenterSomErTilgjengeligeIAlleMiljoer(tpsRequestParameters, context);
    }

    private List<String> hentIdenterSomErTilgjengeligeIAlleMiljoer(Map<String, Object> tpsRequestParameters, TpsRequestContext context){

        List<String> tilgjengeligeIdenterAlleMiljøer = new ArrayList<>();

        for(int i=LOWEST_T_ENVIRONMENT; i<HIGHEST_T_ENVIRONMENT; i++){
            if(i == 7){
                continue;       // The queue manager channel 'T7_TPSWS' for this env does not exist.
            }
            context.setEnvironment("t"+i);

            TpsServiceRoutineRequest tpsServiceRoutineRequest = mappingUtils.convertToTpsServiceRoutineRequest(String.valueOf(tpsRequestParameters.get("serviceRutinenavn")), tpsRequestParameters);
            TpsServiceRoutineResponse tpsResponse = tpsRequestSender.sendTpsRequest(tpsServiceRoutineRequest, context);

            if(kunneIkkeLeggeMeldingPåKø(tpsResponse)){
                continue;
            }

            List<String> tilgjengeligeIdenterFraEtBestemtMiljø = trekkUtIdenterFraResponse(tpsResponse);

            if(i == 0){
                tilgjengeligeIdenterAlleMiljøer.addAll(tilgjengeligeIdenterFraEtBestemtMiljø);
            } else {
                tilgjengeligeIdenterAlleMiljøer.retainAll(tilgjengeligeIdenterFraEtBestemtMiljø);
            }
        }
        return tilgjengeligeIdenterAlleMiljøer;
    }

    private boolean kunneIkkeLeggeMeldingPåKø(TpsServiceRoutineResponse response){
        return response.getXml().isEmpty();
    }

    private Map<String,Object> opprettParametereForM201TpsRequest(List<String> identer){
        Map<String, Object> tpsRequestParameters = new HashMap<>();
        tpsRequestParameters.put("serviceRutinenavn","FS03-FDLISTER-DISKNAVN-M");
        tpsRequestParameters.put("fnr", identer);
        tpsRequestParameters.put("antallFnr", identer.size());
        tpsRequestParameters.put("aksjonsKode","A0");
        return tpsRequestParameters;
    }

    private List<String> trekkUtIdenterFraResponse(TpsServiceRoutineResponse tpsResponse){
        LinkedHashMap responseMap = (LinkedHashMap)tpsResponse.getResponse();
        List<String> tilgjengeligeIdenter = new ArrayList<>();

        int antallIdenter = (int) responseMap.get("antallTotalt");

        for(int i = 1; i< antallIdenter+1; i++){
            LinkedHashMap data = (LinkedHashMap) responseMap.get("data"+i);
            tilgjengeligeIdenter.add(String.valueOf(data.get("fnr")));
        }

        return tilgjengeligeIdenter;
    }
}
