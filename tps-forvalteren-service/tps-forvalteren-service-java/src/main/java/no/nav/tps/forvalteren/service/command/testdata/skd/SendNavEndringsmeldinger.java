package no.nav.tps.forvalteren.service.command.testdata.skd;

import static com.google.common.collect.Lists.newArrayList;
import static com.google.common.collect.Maps.newHashMap;
import static java.lang.String.format;
import static no.nav.tps.forvalteren.service.command.testdata.utils.BehandleTpsRespons.ekstraherStatusFraServicerutineRespons;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import no.nav.tps.forvalteren.domain.jpa.Person;
import no.nav.tps.forvalteren.domain.service.tps.ResponseStatus;
import no.nav.tps.forvalteren.domain.service.tps.servicerutiner.requests.TpsRequestContext;
import no.nav.tps.forvalteren.domain.service.tps.servicerutiner.requests.TpsServiceRoutineEndringRequest;
import no.nav.tps.forvalteren.domain.service.tps.servicerutiner.response.TpsServiceRoutineResponse;
import no.nav.tps.forvalteren.service.command.testdata.EndreSprakkodeService;
import no.nav.tps.forvalteren.service.command.testdata.OpprettEgenAnsattMelding;
import no.nav.tps.forvalteren.service.command.testdata.OpprettSikkerhetstiltakMelding;
import no.nav.tps.forvalteren.service.command.testdata.response.lagretiltps.ServiceRoutineResponseStatus;
import no.nav.tps.forvalteren.service.command.testdata.utils.TpsPacemaker;
import no.nav.tps.forvalteren.service.command.tps.servicerutiner.TpsRequestSender;
import no.nav.tps.forvalteren.service.user.UserContextHolder;

@Service
public class SendNavEndringsmeldinger {

    @Autowired
    private OpprettEgenAnsattMelding opprettEgenAnsattMelding;

    @Autowired
    private OpprettSikkerhetstiltakMelding opprettSikkerhetstiltakMelding;

    @Autowired
    private EndreSprakkodeService endreSprakkodeService;

    @Autowired
    private TpsRequestSender tpsRequestSender;

    @Autowired
    private UserContextHolder userContextHolder;

    @Autowired
    private TpsPacemaker tpsPacemaker;

    public List<ServiceRoutineResponseStatus> execute(List<Person> listeMedPersoner, Set<String> environmentsSet) {

        TpsRequestContext tpsRequestContext = new TpsRequestContext();
        tpsRequestContext.setUser(userContextHolder.getUser());

        List<TpsNavEndringsMelding> navEndringsMeldinger = new ArrayList<>();
        listeMedPersoner.forEach(person -> {
            navEndringsMeldinger.addAll(opprettEgenAnsattMelding.execute(person, environmentsSet));
            navEndringsMeldinger.addAll(opprettSikkerhetstiltakMelding.execute(person, environmentsSet));
            navEndringsMeldinger.addAll(endreSprakkodeService.execute(person, environmentsSet));
        });

        Map<String, ServiceRoutineResponseStatus> responseStatuses = new HashMap<>();
        for (int i = 0; i < navEndringsMeldinger.size(); i++) {
            TpsNavEndringsMelding serviceRoutineRequest = navEndringsMeldinger.get(i);

            String ident = ((TpsServiceRoutineEndringRequest) serviceRoutineRequest.getMelding()).getOffentligIdent();

            tpsRequestContext.setEnvironment(serviceRoutineRequest.getMiljo());
            TpsServiceRoutineResponse svar = tpsRequestSender.sendTpsRequest(serviceRoutineRequest.getMelding(), tpsRequestContext);

            if (!responseStatuses.containsKey(ident)) {
                responseStatuses.put(ident, ServiceRoutineResponseStatus.builder()
                        .personId(ident)
                        .serviceRutinenavn(serviceRoutineRequest.getMelding().getServiceRutinenavn())
                        .status(newHashMap())
                        .build()
                );
            }
            responseStatuses.get(ident).getStatus().put(serviceRoutineRequest.getMiljo(), formatResultatMelding(svar));

            tpsPacemaker.iteration(i);
        }

        return newArrayList(responseStatuses.values());
    }

    private String formatResultatMelding(TpsServiceRoutineResponse response) {
        ResponseStatus status = ekstraherStatusFraServicerutineRespons(response);
        return "00".equals(status.getKode()) || "04".equals(status.getKode()) ? "OK" :
                formaterFeilmelding(status);
    }

    private String formaterFeilmelding(ResponseStatus status) {
        return isNotBlank(status.getUtfyllendeMelding()) ?
                format("FEIL: %s", status.getUtfyllendeMelding()) : "STATUS: UKJENT";
    }
}