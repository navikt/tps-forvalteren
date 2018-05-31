package no.nav.tps.forvalteren.service.command.testdata.skd;

import static no.nav.tps.forvalteren.service.command.testdata.utils.BehandleTpsRespons.ekstraherStatusFraServicerutineRespons;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import no.nav.tps.forvalteren.domain.jpa.Person;
import no.nav.tps.forvalteren.domain.service.tps.ResponseStatus;
import no.nav.tps.forvalteren.domain.service.tps.servicerutiner.requests.TpsRequestContext;
import no.nav.tps.forvalteren.domain.service.tps.servicerutiner.requests.TpsServiceRoutineEndringRequest;
import no.nav.tps.forvalteren.domain.service.tps.servicerutiner.response.TpsServiceRoutineResponse;
import no.nav.tps.forvalteren.service.command.testdata.OpprettEgenAnsattMelding;
import no.nav.tps.forvalteren.service.command.testdata.OpprettSikkerhetstiltakMelding;
import no.nav.tps.forvalteren.service.command.testdata.response.lagreTilTps.ServiceRoutineResponseStatus;
import no.nav.tps.forvalteren.service.command.tps.servicerutiner.TpsRequestSender;
import no.nav.tps.forvalteren.service.user.UserContextHolder;

@Service
public class SendNavEndringsmeldinger {
    
    @Autowired
    private OpprettEgenAnsattMelding opprettEgenAnsattMelding;
    
    @Autowired
    private OpprettSikkerhetstiltakMelding opprettSikkerhetstiltakMelding;
    
    @Autowired
    private TpsRequestSender tpsRequestSender;
    
    @Autowired
    private UserContextHolder userContextHolder;
    
    public List<ServiceRoutineResponseStatus> execute(List<Person> listeMedPersoner, Set<String> environmentsSet) {
        List<TpsNavEndringsMelding> navEndringsMeldinger = new ArrayList<>();
        
        TpsRequestContext tpsRequestContext = new TpsRequestContext();
        tpsRequestContext.setUser(userContextHolder.getUser());
    
        listeMedPersoner.forEach(person -> {
            navEndringsMeldinger.addAll(opprettEgenAnsattMelding.execute(person, environmentsSet));
            navEndringsMeldinger.addAll(opprettSikkerhetstiltakMelding.execute(person, environmentsSet));
        });
        
        List<ServiceRoutineResponseStatus> responseStatuses = new ArrayList<>();
        navEndringsMeldinger.forEach(serviceRoutineRequest -> {
            tpsRequestContext.setEnvironment(serviceRoutineRequest.getMiljo());
            TpsServiceRoutineResponse svar = tpsRequestSender.sendTpsRequest(serviceRoutineRequest.getMelding(), tpsRequestContext);
            
            ResponseStatus status = ekstraherStatusFraServicerutineRespons(svar);
            if ("00" != status.getKode()) {
                responseStatuses.add(byggRespons(serviceRoutineRequest, status));
            }
        });
        
        if (responseStatuses.isEmpty()) {
            responseStatuses.add(new ServiceRoutineResponseStatus("Alle identer", "Alle NAV endringsmeldinger", "alle miljøer", new ResponseStatus("OK","","") ));
        }
        
        return responseStatuses;
    }
    
    private ServiceRoutineResponseStatus byggRespons(TpsNavEndringsMelding serviceRoutineRequest, ResponseStatus responseStatus) {
        String ident = ((TpsServiceRoutineEndringRequest) serviceRoutineRequest.getMelding()).getOffentligIdent();
        return ServiceRoutineResponseStatus.builder()
                .personId(ident)
                .serviceRutinenavn(serviceRoutineRequest.getMelding().getServiceRutinenavn())
                .environment(serviceRoutineRequest.getMiljo())
                .status(responseStatus).build();
    }
    
}
