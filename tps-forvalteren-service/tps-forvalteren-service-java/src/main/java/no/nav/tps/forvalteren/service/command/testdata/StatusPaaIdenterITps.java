package no.nav.tps.forvalteren.service.command.testdata;

import static java.lang.String.valueOf;
import static no.nav.tps.forvalteren.service.command.testdata.utils.ExtractDataFromTpsServiceRoutineResponse.trekkUtIdenterMedStatusFunnetFraResponse;
import static no.nav.tps.forvalteren.service.command.testdata.utils.TpsRequestParameterCreator.opprettParametereForM201TpsRequest;
import static org.apache.commons.lang3.BooleanUtils.isTrue;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import no.nav.tps.forvalteren.domain.rs.RsTpsStatusPaaIdenterResponse;
import no.nav.tps.forvalteren.domain.rs.TpsStatusPaaIdent;
import no.nav.tps.forvalteren.domain.service.tps.servicerutiner.requests.TpsRequestContext;
import no.nav.tps.forvalteren.domain.service.tps.servicerutiner.requests.TpsServiceRoutineRequest;
import no.nav.tps.forvalteren.domain.service.tps.servicerutiner.response.TpsServiceRoutineResponse;
import no.nav.tps.forvalteren.service.command.FilterEnvironmentsOnDeployedEnvironment;
import no.nav.tps.forvalteren.service.command.exceptions.HttpInternalServerErrorException;
import no.nav.tps.forvalteren.service.command.tps.servicerutiner.TpsRequestSender;
import no.nav.tps.forvalteren.service.command.tps.servicerutiner.utils.RsTpsRequestMappingUtils;
import no.nav.tps.forvalteren.service.command.tpsconfig.GetEnvironments;
import no.nav.tps.forvalteren.service.user.UserContextHolder;

/**
 * Denne tjenesten returnerer hvilke miljøer som de ulike fødselsnumrene eksisterer i.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class StatusPaaIdenterITps {

    private static final String EKSISTENS_SJEKK_FEILET = "Feilet å sjekke eksistensstatus for ident i miljoe {}";
    private static final String PROD_ENV = "p";

    private final GetEnvironments getEnvironments;
    private final FilterEnvironmentsOnDeployedEnvironment filterEnvironmentsOnDeployedEnvironment;
    private final UserContextHolder userContextHolder;
    private final TpsRequestSender tpsRequestSender;
    private final RsTpsRequestMappingUtils mappingUtils;

    public RsTpsStatusPaaIdenterResponse hentStatusPaaIdenterIAlleMiljoer(List<String> identer, Boolean prodIncluded) {

        Set<String> environmentsToCheck =
                filterEnvironmentsOnDeployedEnvironment.execute(getEnvironments.getEnvironmentsFromFasit("tpsws"));

        if (isTrue(prodIncluded)) {
            environmentsToCheck.add(PROD_ENV);
        }

        TpsServiceRoutineRequest tpsNonProdRequest = getTpsServiceRoutineRequest(identer, "A0");
        TpsServiceRoutineRequest tpsProdRequest = getTpsServiceRoutineRequest(identer, "A2");

        Map<String, Set<String>> identerPerMiljoe = new HashMap();
        identer.forEach(ident -> identerPerMiljoe.put(ident, new TreeSet<>()));

        environmentsToCheck.parallelStream().map(env -> {

            try {
                TpsServiceRoutineResponse tpsResponse = getTpsStatus(tpsNonProdRequest, tpsProdRequest, env);
                trekkUtIdenterMedStatusFunnetFraResponse(tpsResponse).forEach(ident -> {
                            Set miljoer = identerPerMiljoe.get(ident);
                            miljoer.add(env);
                            identerPerMiljoe.put(ident, miljoer);
                        }
                );

            } catch (HttpInternalServerErrorException e) {
                log.warn(EKSISTENS_SJEKK_FEILET, env);

            } catch (Exception e) {
                log.error(EKSISTENS_SJEKK_FEILET, env, e);
            }
            return true;
        }).collect(Collectors.toList());

        return RsTpsStatusPaaIdenterResponse.builder()
                .statusPaaIdenter(
                        identerPerMiljoe.entrySet().stream().map(entry ->
                                TpsStatusPaaIdent.builder()
                                        .ident(entry.getKey())
                                        .env(new ArrayList(entry.getValue()))
                                        .build()).collect(Collectors.toList()))
                .build();
    }

    private TpsServiceRoutineResponse getTpsStatus(TpsServiceRoutineRequest tpsNonProdRequest, TpsServiceRoutineRequest tpsProdRequest, String env) {
        if (PROD_ENV.equals(env)) {
            return tpsRequestSender.sendTpsRequest(tpsProdRequest,
                    new TpsRequestContext(userContextHolder.getUser(), "q2"));
        } else {
            return tpsRequestSender.sendTpsRequest(tpsNonProdRequest,
                    new TpsRequestContext(userContextHolder.getUser(), env));
        }
    }

    private TpsServiceRoutineRequest getTpsServiceRoutineRequest(List<String> identer, String aksjonskode) {
        Map<String, Object> tpsRequestParameters = opprettParametereForM201TpsRequest(identer, aksjonskode);
        TpsServiceRoutineRequest tpsServiceRoutineRequest =
                mappingUtils.convertToTpsServiceRoutineRequest(valueOf(tpsRequestParameters.get("serviceRutinenavn")), tpsRequestParameters);
        return tpsServiceRoutineRequest;
    }
}

