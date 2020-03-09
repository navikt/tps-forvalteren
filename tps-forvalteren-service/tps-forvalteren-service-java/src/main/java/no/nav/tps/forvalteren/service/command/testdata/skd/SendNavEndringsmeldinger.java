package no.nav.tps.forvalteren.service.command.testdata.skd;

import static com.google.common.collect.Lists.newArrayList;
import static com.google.common.collect.Maps.newHashMap;
import static java.lang.String.format;
import static no.nav.tps.forvalteren.service.command.testdata.utils.BehandleTpsRespons.ekstraherStatusFraServicerutineRespons;
import static org.apache.commons.lang3.StringUtils.isBlank;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import no.nav.tps.forvalteren.domain.jpa.Person;
import no.nav.tps.forvalteren.domain.service.tps.ResponseStatus;
import no.nav.tps.forvalteren.domain.service.tps.servicerutiner.requests.TpsRequestContext;
import no.nav.tps.forvalteren.domain.service.tps.servicerutiner.requests.TpsServiceRoutineEndringRequest;
import no.nav.tps.forvalteren.domain.service.tps.servicerutiner.response.TpsServiceRoutineResponse;
import no.nav.tps.forvalteren.service.command.testdata.EndreNorskGironummerService;
import no.nav.tps.forvalteren.service.command.testdata.EndreSprakkodeService;
import no.nav.tps.forvalteren.service.command.testdata.OpprettEgenAnsattMelding;
import no.nav.tps.forvalteren.service.command.testdata.OpprettSikkerhetstiltakMelding;
import no.nav.tps.forvalteren.service.command.testdata.response.lagretiltps.ServiceRoutineResponseStatus;
import no.nav.tps.forvalteren.service.command.testdata.utils.TpsPacemaker;
import no.nav.tps.forvalteren.service.command.tps.servicerutiner.TpsRequestSender;
import no.nav.tps.forvalteren.service.user.UserContextHolder;

@Service
@RequiredArgsConstructor
public class SendNavEndringsmeldinger {

    private final OpprettEgenAnsattMelding opprettEgenAnsattMelding;
    private final OpprettSikkerhetstiltakMelding opprettSikkerhetstiltakMelding;
    private final EndreSprakkodeService endreSprakkodeService;
    private final EndreNorskGironummerService endreNorskGironummerService;
    private final TpsRequestSender tpsRequestSender;
    private final UserContextHolder userContextHolder;
    private final TpsPacemaker tpsPacemaker;

    public List<ServiceRoutineResponseStatus> execute(List<Person> listeMedPersoner, Set<String> environmentsSet) {

        TpsRequestContext tpsRequestContext = new TpsRequestContext();
        tpsRequestContext.setUser(userContextHolder.getUser());

        List<TpsNavEndringsMelding> navEndringsMeldinger = new ArrayList<>();
        listeMedPersoner.forEach(person -> {
            navEndringsMeldinger.addAll(opprettEgenAnsattMelding.execute(person, environmentsSet));
            navEndringsMeldinger.addAll(opprettSikkerhetstiltakMelding.execute(person, environmentsSet));
            navEndringsMeldinger.addAll(endreSprakkodeService.execute(person, environmentsSet));
            navEndringsMeldinger.addAll(endreNorskGironummerService.execute(person, environmentsSet));
        });

        Map<String, ServiceRoutineResponseStatus> responseStatuses = new HashMap<>();
        for (int i = 0; i < navEndringsMeldinger.size(); i++) {
            TpsNavEndringsMelding serviceRoutineRequest = navEndringsMeldinger.get(i);

            String ident = ((TpsServiceRoutineEndringRequest) serviceRoutineRequest.getMelding()).getOffentligIdent();

            tpsRequestContext.setEnvironment(serviceRoutineRequest.getMiljo());
            TpsServiceRoutineResponse svar = tpsRequestSender.sendTpsRequest(serviceRoutineRequest.getMelding(), tpsRequestContext);

            String key = format("%s %s", ident, serviceRoutineRequest.getMelding().getServiceRutinenavn());
            if (!responseStatuses.containsKey(key)) {
                responseStatuses.put(key, ServiceRoutineResponseStatus.builder()
                        .personId(ident)
                        .serviceRutinenavn(serviceRoutineRequest.getMelding().getServiceRutinenavn())
                        .status(newHashMap())
                        .build()
                );
            }
            responseStatuses.get(key).getStatus().put(serviceRoutineRequest.getMiljo(), formatResultatMelding(svar));

            tpsPacemaker.iteration(i);
        }

        return newArrayList(responseStatuses.values());
    }

    private static String formatResultatMelding(TpsServiceRoutineResponse response) {
        ResponseStatus status = ekstraherStatusFraServicerutineRespons(response);
        return isBlank(status.getKode()) || "00".equals(status.getKode()) || "04".equals(status.getKode()) ? "OK" :
                format("FEIL: %s", status.getUtfyllendeMelding());
    }
}