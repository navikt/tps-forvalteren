package no.nav.tps.forvalteren.service.command.testdata;

import no.nav.tps.forvalteren.domain.service.tps.ResponseStatus;
import no.nav.tps.forvalteren.domain.service.tps.servicerutiner.requests.TpsRequestContext;
import no.nav.tps.forvalteren.domain.service.tps.servicerutiner.requests.TpsServiceRoutineRequest;
import no.nav.tps.forvalteren.domain.service.tps.servicerutiner.response.TpsServiceRoutineResponse;
import no.nav.tps.forvalteren.service.command.FilterEnvironmentsOnDeployedEnvironment;
import no.nav.tps.forvalteren.service.command.tps.servicerutiner.TpsRequestSender;
import no.nav.tps.forvalteren.service.command.tps.servicerutiner.utils.RsTpsRequestMappingUtils;
import no.nav.tps.forvalteren.service.command.vera.GetEnvironments;
import no.nav.tps.forvalteren.service.user.UserContextHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Service
public class FiltrerPaaIdenterTilgjengeligeIMiljo {

    private static final int MAX_ANTALL_IDENTER_TIL_REQUEST_M201 = 80;

    private static final String TPS_SYSTEM_ERROR_CODE = "12";

    @Autowired
    private UserContextHolder userContextHolder;

    @Autowired
    private TpsRequestSender tpsRequestSender;

    @Autowired
    private RsTpsRequestMappingUtils mappingUtils;


    @Autowired
    private FilterEnvironmentsOnDeployedEnvironment filterEnvironmentsOnDeployedEnvironment;

    public Set<String> filtrer(Collection<String> identer, Set<String> environments){
        if(identer.size() <= MAX_ANTALL_IDENTER_TIL_REQUEST_M201){
            return filtrerPaaIdenter(identer, environments);

        } else {
            Set<String> tilgjengeligeIdenter = new HashSet<>();
            List<String> identerListe = new ArrayList<>(identer);
            int batchStart = 0;
            while(batchStart < identer.size()){
                tilgjengeligeIdenter.addAll(hentEnBatchTilgjengeligeIdenter(batchStart, identerListe, environments));
                batchStart = batchStart + MAX_ANTALL_IDENTER_TIL_REQUEST_M201;
            }
            return tilgjengeligeIdenter;
        }
    }

    private Set<String> hentEnBatchTilgjengeligeIdenter(int batchStart, List<String> identer, Set<String> environments){
        int batchStop = (identer.size() <= batchStart+MAX_ANTALL_IDENTER_TIL_REQUEST_M201)
                ? identer.size() : (batchStart+MAX_ANTALL_IDENTER_TIL_REQUEST_M201);

        return filtrerPaaIdenter(identer.subList(batchStart, batchStop), environments);
    }

    private Set<String> filtrerPaaIdenter(Collection<String> identer,Set<String> environments){

        Map<String, Object> tpsRequestParameters = opprettParametereForM201TpsRequest(identer);

        TpsRequestContext context = new TpsRequestContext();
        context.setUser(userContextHolder.getUser());

        return hentIdenterSomErTilgjengeligeIAlleMiljoer(tpsRequestParameters, context, environments);
    }

    private Set<String> hentIdenterSomErTilgjengeligeIAlleMiljoer(Map<String, Object> tpsRequestParameters, TpsRequestContext context, Set<String> environments){
        Set<String> tilgjengeligeIdenterAlleMiljoer = new HashSet<>((Collection<String>)tpsRequestParameters.get("fnr"));

        //Set<String> environments = getEnvironmentsCommand.getEnvironmentsFromVera("tpsws");
        Set<String> environmentsToCheck = filterEnvironmentsOnDeployedEnvironment.execute(environments);

        filtrerOgTaVarePaaIdenterTilgjengeligIMiljoer(tilgjengeligeIdenterAlleMiljoer, environmentsToCheck, tpsRequestParameters, context);

        return tilgjengeligeIdenterAlleMiljoer;
    }

    private void filtrerOgTaVarePaaIdenterTilgjengeligIMiljoer(Set<String> tilgjengligIdenter, Set<String> miljoerAaSjekke, Map<String, Object> tpsRequestParameters, TpsRequestContext context){
        for(String miljoe : miljoerAaSjekke){
            context.setEnvironment(miljoe);

            TpsServiceRoutineRequest tpsServiceRoutineRequest = mappingUtils.convertToTpsServiceRoutineRequest(String.valueOf(tpsRequestParameters.get("serviceRutinenavn")), tpsRequestParameters);
            TpsServiceRoutineResponse tpsResponse = tpsRequestSender.sendTpsRequest(tpsServiceRoutineRequest, context);

            if(kunneIkkeLeggeMeldingPaaKoe(tpsResponse)){
                continue;       //TODO Gjoer hva hvis man ikke faar lagt paa koe??? Bare hopper over for naa.
            }

            Set<String> tilgjengeligeIdenterFraEtBestemtMiljoe = trekkUtIdenterFraResponse(tpsResponse);

            tilgjengligIdenter.retainAll(tilgjengeligeIdenterFraEtBestemtMiljoe);
        }
    }

    private boolean kunneIkkeLeggeMeldingPaaKoe(TpsServiceRoutineResponse response){
        if(response.getXml().isEmpty()){
            return true;
        }
        LinkedHashMap rep = (LinkedHashMap) response.getResponse();
        ResponseStatus status = (ResponseStatus) rep.get("status");

        if(TPS_SYSTEM_ERROR_CODE.equals(status.getKode())){
            return true;
        }
        return false;
    }

    private Map<String,Object> opprettParametereForM201TpsRequest(Collection<String> identer){
        Map<String, Object> tpsRequestParameters = new HashMap<>();
        tpsRequestParameters.put("serviceRutinenavn","FS03-FDLISTER-DISKNAVN-M");
        tpsRequestParameters.put("fnr", identer);
        tpsRequestParameters.put("antallFnr", identer.size());
        tpsRequestParameters.put("aksjonsKode","A0");
        return tpsRequestParameters;
    }

    private Set<String> trekkUtIdenterFraResponse(TpsServiceRoutineResponse tpsResponse){
        LinkedHashMap responseMap = (LinkedHashMap)tpsResponse.getResponse();
        Set<String> tilgjengeligeIdenter = new HashSet<>();

        int antallIdenter = (int) responseMap.get("antallTotalt");

        for(int i = 1; i< antallIdenter+1; i++){
            LinkedHashMap data = (LinkedHashMap) responseMap.get("data"+i);
            tilgjengeligeIdenter.add(String.valueOf(data.get("fnr")));
        }

        return tilgjengeligeIdenter;
    }
}
