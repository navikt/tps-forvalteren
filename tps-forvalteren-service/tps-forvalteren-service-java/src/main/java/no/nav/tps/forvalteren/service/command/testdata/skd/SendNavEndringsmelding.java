package no.nav.tps.forvalteren.service.command.testdata.skd;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Set;

import no.nav.tps.forvalteren.domain.jpa.Person;
import no.nav.tps.forvalteren.domain.service.tps.ResponseStatus;
import no.nav.tps.forvalteren.domain.service.tps.servicerutiner.requests.TpsRequestContext;
import no.nav.tps.forvalteren.domain.service.tps.servicerutiner.response.TpsServiceRoutineResponse;
import no.nav.tps.forvalteren.service.command.testdata.FinnPersonerForNavEndringsmelding;
import no.nav.tps.forvalteren.service.command.testdata.OpprettEgenAnsattMelding;
import no.nav.tps.forvalteren.service.command.testdata.OpprettSikkerhetstiltakMelding;
import no.nav.tps.forvalteren.service.command.tps.servicerutiner.TpsRequestSender;
import no.nav.tps.forvalteren.service.user.UserContextHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SendNavEndringsmelding {

    @Autowired
    private FinnPersonerForNavEndringsmelding finnPersonerForNavEndringsmelding;

    @Autowired
    private OpprettEgenAnsattMelding opprettEgenAnsattMelding;

    @Autowired
    private OpprettSikkerhetstiltakMelding opprettSikkerhetstiltakMelding;

    @Autowired
    private TpsRequestSender tpsRequestSender;

    @Autowired
    private UserContextHolder userContextHolder;

    public void execute(List<Person> listeMedPersoner, Set<String> environmentsSet) {
        List<Person> personerSomSkalHaNavEndring = finnPersonerForNavEndringsmelding.execute(listeMedPersoner);
        List<TpsNavEndringsMelding> navEndringsMeldinger = new ArrayList<>();

        TpsRequestContext tpsRequestContext = new TpsRequestContext();
        tpsRequestContext.setUser(userContextHolder.getUser());

        personerSomSkalHaNavEndring.forEach(person -> {
            navEndringsMeldinger.addAll(opprettEgenAnsattMelding.execute(person, environmentsSet));
            navEndringsMeldinger.addAll(opprettSikkerhetstiltakMelding.execute(person, environmentsSet));
        });

        navEndringsMeldinger.forEach(serviceRoutineRequest -> {
            tpsRequestContext.setEnvironment(serviceRoutineRequest.getMiljo());
            TpsServiceRoutineResponse svar = tpsRequestSender.sendTpsRequest(serviceRoutineRequest.getMelding(), tpsRequestContext);

            //sjekkNavEndringsmeldingStatus(svar);
        });

    }

    private void sjekkNavEndringsmeldingStatus(TpsServiceRoutineResponse svar){
        LinkedHashMap map = (LinkedHashMap) svar.getResponse();
        ResponseStatus respons =(ResponseStatus) map.get("status");

        if(respons.getKode() != "00"){

        }
    }

}
