package no.nav.tps.forvalteren.service.command.testdata;

import no.nav.tps.forvalteren.domain.service.tps.ResponseStatus;
import no.nav.tps.forvalteren.domain.service.tps.servicerutiner.requests.TpsRequestContext;
import no.nav.tps.forvalteren.domain.service.tps.servicerutiner.requests.TpsServiceRoutineRequest;
import no.nav.tps.forvalteren.domain.service.tps.servicerutiner.response.TpsServiceRoutineResponse;
import no.nav.tps.forvalteren.service.command.tps.servicerutiner.TpsRequestSender;
import no.nav.tps.forvalteren.service.command.tps.servicerutiner.utils.RsTpsRequestMappingUtils;
import no.nav.tps.forvalteren.service.user.UserContextHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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
public class FilterPaaIdenterTilgjengeligeIMiljo {

    private static final int MAX_ANTALL_IDENTER_TIL_REQUEST_M201 = 80;

    //TODO Finne ut hva man skal gjoere med disse...
    // The queue manager channel 'T7_TPSWS' for this env does not exist.  Tatt ut t13 fordi randomly ikke virket.
    // Q7 er også noe feil med.. Q9 også... Qx også
    private static final String[] T_ENVIRONMENTS = {"t0","t1","t2","t3","t4","t5","t6","t7","t8","t9","t10","t11","t12"};
    private static final String[] Q_ENVIRONMENTS = {"q0","q1","q2","q3","q4","q5","q6","q8"};
    private static final String[] U_ENVIRONMENTS = {"u5", "u6"};

    @Value("${environment.class}")
    private String deployedEnvironment;

    @Autowired
    private UserContextHolder userContextHolder;

    @Autowired
    private TpsRequestSender tpsRequestSender;

    @Autowired
    private RsTpsRequestMappingUtils mappingUtils;

    public Set<String> filtrer(Collection<String> identer){
        if(!(identer.size() > MAX_ANTALL_IDENTER_TIL_REQUEST_M201)){

            return filtrerPaaIdenter(identer);

        } else {

            Set<String> tilgjengeligeIdenter = new HashSet<>();
            List<String> identerListe = new ArrayList<>(identer);
            int batchStart = 0;
            int batchStop;
            while(batchStart < identer.size()){
                batchStop = (identer.size() <= batchStart+MAX_ANTALL_IDENTER_TIL_REQUEST_M201)
                            ? identer.size() : (batchStart+MAX_ANTALL_IDENTER_TIL_REQUEST_M201);

                Set<String> tilgjengeligeIdenterFraEnJobb = filtrerPaaIdenter(identerListe.subList(batchStart, batchStop));
                tilgjengeligeIdenter.addAll(tilgjengeligeIdenterFraEnJobb);
                batchStart = batchStart + MAX_ANTALL_IDENTER_TIL_REQUEST_M201;
            }
            return tilgjengeligeIdenter;
        }
    }

    private Set<String> filtrerPaaIdenter(Collection<String> identer){

        Map<String, Object> tpsRequestParameters = opprettParametereForM201TpsRequest(identer);

        TpsRequestContext context = new TpsRequestContext();
        context.setUser(userContextHolder.getUser());

        return hentIdenterSomErTilgjengeligeIAlleMiljoer(tpsRequestParameters, context);
    }

    private Set<String> hentIdenterSomErTilgjengeligeIAlleMiljoer(Map<String, Object> tpsRequestParameters, TpsRequestContext context){

        Set<String> tilgjengeligeIdenterAlleMiljoer = new HashSet<>((Collection<String>)tpsRequestParameters.get("fnr"));

        if("q".equalsIgnoreCase(deployedEnvironment)){
            hentOgTaVarePaaIdenterTilgjengeligIEtBestemtMiloe(tilgjengeligeIdenterAlleMiljoer, Q_ENVIRONMENTS, tpsRequestParameters, context);
        }

        hentOgTaVarePaaIdenterTilgjengeligIEtBestemtMiloe(tilgjengeligeIdenterAlleMiljoer, U_ENVIRONMENTS, tpsRequestParameters, context);
        hentOgTaVarePaaIdenterTilgjengeligIEtBestemtMiloe(tilgjengeligeIdenterAlleMiljoer, T_ENVIRONMENTS, tpsRequestParameters, context);

        return tilgjengeligeIdenterAlleMiljoer;
    }

    private void hentOgTaVarePaaIdenterTilgjengeligIEtBestemtMiloe(Set<String> tilgjengligIdenter, String[] miljoerAaSjekke, Map<String, Object> tpsRequestParameters, TpsRequestContext context){
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
        if("12".equals(status.getKode())){
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
