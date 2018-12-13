package no.nav.tps.forvalteren.service.command.testdata;

import static no.nav.tps.forvalteren.service.command.testdata.utils.ExtractDataFromTpsServiceRoutineResponse.trekkUtIdenterMedStatusIkkeFunnetFraResponse;
import static no.nav.tps.forvalteren.service.command.testdata.utils.TpsRequestParameterCreator.opprettParametereForM201TpsRequest;
import static org.assertj.core.util.Sets.newHashSet;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;
import no.nav.tps.forvalteren.domain.service.tps.ResponseStatus;
import no.nav.tps.forvalteren.domain.service.tps.servicerutiner.requests.TpsRequestContext;
import no.nav.tps.forvalteren.domain.service.tps.servicerutiner.requests.TpsServiceRoutineRequest;
import no.nav.tps.forvalteren.domain.service.tps.servicerutiner.response.TpsServiceRoutineResponse;
import no.nav.tps.forvalteren.service.command.exceptions.TpsfTechnicalException;
import no.nav.tps.forvalteren.service.command.tps.servicerutiner.TpsRequestSender;
import no.nav.tps.forvalteren.service.command.tps.servicerutiner.utils.RsTpsRequestMappingUtils;
import no.nav.tps.forvalteren.service.user.UserContextHolder;

@Slf4j
@Service
public class FiltrerPaaIdenterTilgjengeligIMiljo {
    
    public static final int MAX_ANTALL_IDENTER_PER_REQUEST = 80; // Service routine M201 maximum
    
    private static final String TPS_SYSTEM_ERROR_CODE = "12";
    
    @Autowired
    private UserContextHolder userContextHolder;
    
    @Autowired
    private TpsRequestSender tpsRequestSender;
    
    @Autowired
    private RsTpsRequestMappingUtils mappingUtils;
    
    public Set<String> filtrer(Collection<String> identer, Set<String> environments) {
        if (identer.size() <= MAX_ANTALL_IDENTER_PER_REQUEST) {
            return filtrerPaaIdenter(identer, environments);
            
        } else {
            Set<String> tilgjengeligeIdenter = new HashSet<>();
            List<String> identerListe = new ArrayList<>(identer);
            int batchStart = 0;
            while (batchStart < identer.size()) {
                tilgjengeligeIdenter.addAll(hentEnBatchTilgjengeligeIdenter(batchStart, identerListe, environments));
                batchStart = batchStart + MAX_ANTALL_IDENTER_PER_REQUEST;
            }
            return tilgjengeligeIdenter;
        }
    }
    
    private Set<String> hentEnBatchTilgjengeligeIdenter(int batchStart, List<String> identer, Set<String> environments) {
        int batchStop = (identer.size() <= batchStart + MAX_ANTALL_IDENTER_PER_REQUEST)
                ? identer.size() : (batchStart + MAX_ANTALL_IDENTER_PER_REQUEST);

        return filtrerPaaIdenter(identer.subList(batchStart, batchStop), environments);
    }

    private Set<String> filtrerPaaIdenter(Collection<String> identer, Set<String> environments) {
        
        Map<String, Object> tpsRequestParameters = opprettParametereForM201TpsRequest(identer, "A0");
        
        TpsRequestContext context = new TpsRequestContext();
        context.setUser(userContextHolder.getUser());
        
        Set<String> tilgjengeligeIdenterAlleMiljoer = newHashSet((Collection<String>) tpsRequestParameters.get("fnr"));
        
        for (String miljoe : environments) {
            context.setEnvironment(miljoe);
            
            TpsServiceRoutineRequest tpsServiceRoutineRequest = mappingUtils.convertToTpsServiceRoutineRequest(String.valueOf(tpsRequestParameters
                    .get("serviceRutinenavn")), tpsRequestParameters);
            TpsServiceRoutineResponse tpsResponse = tpsRequestSender.sendTpsRequest(tpsServiceRoutineRequest, context);
            
            checkForTpsSystemfeil(tpsResponse, miljoe);
            
            Set<String> tilgjengeligeIdenterFraEtBestemtMiljoe = trekkUtIdenterMedStatusIkkeFunnetFraResponse(tpsResponse);

            tilgjengeligeIdenterAlleMiljoer.retainAll(tilgjengeligeIdenterFraEtBestemtMiljoe);
            if (tilgjengeligeIdenterAlleMiljoer.isEmpty()) {
                log.info("Ledige identer ikke funnet i {}", miljoe);
                break;
            }
        }
        return tilgjengeligeIdenterAlleMiljoer;
    }
    
    private void checkForTpsSystemfeil(TpsServiceRoutineResponse response, String miljoe) {
        if (response.getXml().isEmpty()) {
            log.error("Request mot TPS i miljoe {} fikk timeout.  Sjekk av tilgjengelighet på ident i miljoe feilet.", miljoe);
        }
        LinkedHashMap rep = (LinkedHashMap) response.getResponse();
        ResponseStatus status = (ResponseStatus) rep.get("status");
        
        if (TPS_SYSTEM_ERROR_CODE.equals(status.getKode())) {
            log.error("TPS returnerte SYSTEM ERROR");
            throw new TpsfTechnicalException("TPS returnerte SYSTEM ERROR");
        }
    }
}
