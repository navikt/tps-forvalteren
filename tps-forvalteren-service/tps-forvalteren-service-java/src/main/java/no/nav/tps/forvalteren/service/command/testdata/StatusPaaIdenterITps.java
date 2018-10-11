package no.nav.tps.forvalteren.service.command.testdata;

import static no.nav.tps.forvalteren.service.command.testdata.utils.ExtractDataFromTpsServiceRoutineResponse.trekkUtIdenterMedStatusIkkeFunnetFraResponse;
import static no.nav.tps.forvalteren.service.command.testdata.utils.TpsRequestParameterCreator.opprettParametereForM201TpsRequest;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.google.common.collect.Lists;

import no.nav.tps.forvalteren.domain.rs.RsTpsStatusPaaIdenterResponse;
import no.nav.tps.forvalteren.domain.rs.TpsStatusPaaIdent;
import no.nav.tps.forvalteren.domain.service.tps.servicerutiner.requests.TpsRequestContext;
import no.nav.tps.forvalteren.domain.service.tps.servicerutiner.requests.TpsServiceRoutineRequest;
import no.nav.tps.forvalteren.domain.service.tps.servicerutiner.response.TpsServiceRoutineResponse;
import no.nav.tps.forvalteren.service.command.FilterEnvironmentsOnDeployedEnvironment;
import no.nav.tps.forvalteren.service.command.tps.servicerutiner.TpsRequestSender;
import no.nav.tps.forvalteren.service.command.tps.servicerutiner.utils.RsTpsRequestMappingUtils;
import no.nav.tps.forvalteren.service.command.tpsconfig.GetEnvironments;
import no.nav.tps.forvalteren.service.user.UserContextHolder;

/**
 * Denne tjenesten returnerer hvilke miljøer som de ulike fødselsnumrene eksisterer i.
 */
@Service
public class StatusPaaIdenterITps {

    @Autowired
    private GetEnvironments getEnvironments;

    @Autowired
    private FilterEnvironmentsOnDeployedEnvironment filterEnvironmentsOnDeployedEnvironment;
    @Autowired
    private UserContextHolder userContextHolder;
    @Autowired
    private TpsRequestSender tpsRequestSender;

    @Autowired
    private RsTpsRequestMappingUtils mappingUtils;

    public RsTpsStatusPaaIdenterResponse hentStatusPaaIdenterIAlleMiljoer(List<String> identer) {
        RsTpsStatusPaaIdenterResponse tpsStatusPaaIdenterResponse = opprettResponse(identer);
        settMiljoerDerIdenteneEksisterer(tpsStatusPaaIdenterResponse, identer);
        tpsStatusPaaIdenterResponse.getStatusPaaIdenter()
                .forEach(tpsStatusPaaIdent -> Collections.sort(tpsStatusPaaIdent.getEnv()));
        return tpsStatusPaaIdenterResponse;
    }

    private RsTpsStatusPaaIdenterResponse opprettResponse(List<String> identer) {

        List<TpsStatusPaaIdent> tpsStatusPaaIdentList = Lists.newArrayListWithExpectedSize(identer.size());

        for (String ident : identer) {
            TpsStatusPaaIdent statusPaaIdent = new TpsStatusPaaIdent();
            statusPaaIdent.setIdent(ident);
            tpsStatusPaaIdentList.add(statusPaaIdent);
        }
        return new RsTpsStatusPaaIdenterResponse(tpsStatusPaaIdentList);
    }

    private void settMiljoerDerIdenteneEksisterer(RsTpsStatusPaaIdenterResponse tpsStatusPaaIdenterResponse, List<String> identer) {
        Set<String> environmentsToCheck =
                filterEnvironmentsOnDeployedEnvironment.execute(getEnvironments.getEnvironmentsFromFasit("tpsws"));
        Map<String, Object> tpsRequestParameters = opprettParametereForM201TpsRequest(identer, "A0");

        for (String env : environmentsToCheck) {
            List<String> identerIkkeIMiljoet = finnesIdenteneIMiljoet(env, tpsRequestParameters);
            ArrayList<String> identerIMiljoet = new ArrayList<>(identer);
            identerIMiljoet.removeAll(identerIkkeIMiljoet);
            tpsStatusPaaIdenterResponse.addEnvToTheseIdents(env, identerIMiljoet);
        }
    }

    private List<String> finnesIdenteneIMiljoet(String env, Map<String, Object> tpsRequestParameters) {

        TpsServiceRoutineRequest tpsServiceRoutineRequest = mappingUtils.convertToTpsServiceRoutineRequest(String.valueOf(tpsRequestParameters
                .get("serviceRutinenavn")), tpsRequestParameters);
        TpsServiceRoutineResponse tpsResponse = tpsRequestSender.sendTpsRequest(tpsServiceRoutineRequest,
                new TpsRequestContext(userContextHolder.getUser(), env));

        return new ArrayList<>(trekkUtIdenterMedStatusIkkeFunnetFraResponse(tpsResponse));
    }
}

