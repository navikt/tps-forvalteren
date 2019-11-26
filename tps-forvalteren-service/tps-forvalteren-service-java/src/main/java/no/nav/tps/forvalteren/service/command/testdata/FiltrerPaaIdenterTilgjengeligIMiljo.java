package no.nav.tps.forvalteren.service.command.testdata;

import static no.nav.tps.forvalteren.service.command.testdata.opprett.OpprettPersonerOgSjekkMiljoeService.PROD_ENV;
import static no.nav.tps.forvalteren.service.command.testdata.utils.ExtractDataFromTpsServiceRoutineResponse.trekkUtIdenterMedStatusFunnetFraResponse;
import static no.nav.tps.forvalteren.service.command.testdata.utils.TpsRequestParameterCreator.opprettParametereForM201TpsRequest;
import static org.assertj.core.util.Sets.newHashSet;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
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
                batchStart += MAX_ANTALL_IDENTER_PER_REQUEST;
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

        Map<String, Object> tpsRequestParameters = opprettParametereForM201TpsRequest(identer, environments.contains(PROD_ENV) ? "A2" : "A0");

        TpsRequestContext context = new TpsRequestContext();
        context.setUser(userContextHolder.getUser());

        Set<String> tilgjengeligeIdenterAlleMiljoer = newHashSet((Collection<String>) tpsRequestParameters.get("fnr"));

        if (environments.contains(PROD_ENV)) {
            environments.remove(PROD_ENV);
            environments.add("q2");
        }
        for (String miljoe : environments) {
            context.setEnvironment(miljoe);

            TpsServiceRoutineRequest tpsServiceRoutineRequest = mappingUtils.convertToTpsServiceRoutineRequest(String.valueOf(tpsRequestParameters
                    .get("serviceRutinenavn")), tpsRequestParameters);

            TpsServiceRoutineResponse tpsResponse = null;
            int loopCount = 3;
            do {
                tpsResponse = tpsRequestSender.sendTpsRequest(tpsServiceRoutineRequest, context);
                loopCount--;
            } while (loopCount != 0 && tpsResponse.getXml().isEmpty());

            checkForTpsSystemfeil(tpsResponse, miljoe);

            if (!tpsResponse.getXml().isEmpty()) {
                tilgjengeligeIdenterAlleMiljoer.removeAll(trekkUtIdenterMedStatusFunnetFraResponse(tpsResponse));
            }

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

        ResponseStatus status = (ResponseStatus) ((Map) response.getResponse()).get("status");

        if (TPS_SYSTEM_ERROR_CODE.equals(status.getKode())) {
            log.error("TPS returnerte SYSTEM ERROR (kode=12) i miljø={}, melding={}, utfyllende melding={}",
                    miljoe, status.getMelding(), status.getUtfyllendeMelding());
        }
    }
}
