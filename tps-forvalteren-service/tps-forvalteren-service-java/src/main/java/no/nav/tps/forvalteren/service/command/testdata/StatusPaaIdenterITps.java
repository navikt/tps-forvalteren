package no.nav.tps.forvalteren.service.command.testdata;

import static java.lang.String.valueOf;
import static no.nav.tps.forvalteren.service.command.testdata.utils.ExtractDataFromTpsServiceRoutineResponse.trekkUtIdenterMedStatusFunnetFraResponse;
import static no.nav.tps.forvalteren.service.command.testdata.utils.TpsRequestParameterCreator.opprettParametereForM201TpsRequest;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

        Set<String> environmentsToCheck =
                filterEnvironmentsOnDeployedEnvironment.execute(getEnvironments.getEnvironmentsFromFasit("tpsws"));
        Map<String, Object> tpsRequestParameters = opprettParametereForM201TpsRequest(identer, "A0");
        TpsServiceRoutineRequest tpsServiceRoutineRequest =
                mappingUtils.convertToTpsServiceRoutineRequest(valueOf(tpsRequestParameters.get("serviceRutinenavn")), tpsRequestParameters);

        Map<String, Set<String>> identerPerMiljoe = new HashMap();
        identer.forEach(ident -> identerPerMiljoe.put(ident, new TreeSet<>()));

        environmentsToCheck.forEach(env -> {

            TpsServiceRoutineResponse tpsResponse = tpsRequestSender.sendTpsRequest(tpsServiceRoutineRequest,
                    new TpsRequestContext(userContextHolder.getUser(), env));
            trekkUtIdenterMedStatusFunnetFraResponse(tpsResponse).forEach(ident -> {
                        Set miljoer = identerPerMiljoe.get(ident);
                        miljoer.add(env);
                        identerPerMiljoe.put(ident, miljoer);
                    }
            );
        });

        return RsTpsStatusPaaIdenterResponse.builder()
                .statusPaaIdenter(
                        identerPerMiljoe.entrySet().stream().map((entry) ->
                                TpsStatusPaaIdent.builder()
                                        .ident(entry.getKey())
                                        .env(new ArrayList(entry.getValue()))
                                        .build()).collect(Collectors.toList()))
                .build();
    }
}

