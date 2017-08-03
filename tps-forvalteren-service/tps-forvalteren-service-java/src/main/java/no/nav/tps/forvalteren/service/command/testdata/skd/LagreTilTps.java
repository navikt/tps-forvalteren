package no.nav.tps.forvalteren.service.command.testdata.skd;

import no.nav.tps.forvalteren.domain.jpa.Gruppe;
import no.nav.tps.forvalteren.domain.jpa.Person;
import no.nav.tps.forvalteren.domain.jpa.Relasjon;
import no.nav.tps.forvalteren.domain.service.tps.servicerutiner.definition.TpsSkdRequestMeldingDefinition;
import no.nav.tps.forvalteren.domain.service.tps.servicerutiner.requests.TpsRequestContext;
import no.nav.tps.forvalteren.domain.service.tps.servicerutiner.requests.TpsServiceRoutineRequest;
import no.nav.tps.forvalteren.domain.service.tps.servicerutiner.response.TpsServiceRoutineResponse;
import no.nav.tps.forvalteren.service.command.testdata.FiltrerPaaIdenterTilgjengeligeIMiljo;
import no.nav.tps.forvalteren.service.command.testdata.FindGruppeById;
import no.nav.tps.forvalteren.service.command.tps.servicerutiner.TpsRequestSender;
import no.nav.tps.forvalteren.service.command.tps.servicerutiner.utils.RsTpsRequestMappingUtils;
import no.nav.tps.forvalteren.service.command.tps.skdmelding.GetSkdMeldingByName;
import no.nav.tps.forvalteren.service.user.UserContextHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Service
public class LagreTilTps {

    private static final String NAVN_INNVANDRINGSMELDING = "Innvandring";

    @Autowired
    private SkdCreatePersoner skdCreatePersoner;

    @Autowired
    private FiltrerPaaIdenterTilgjengeligeIMiljo filtrerPaaIdenterTilgjengeligeIMiljo;

    @Autowired
    private FindGruppeById findGruppeById;

    @Autowired
    private TpsRequestSender tpsRequestSender;

    @Autowired
    private UserContextHolder userContextHolder;

    @Autowired
    private RsTpsRequestMappingUtils mappingUtils;


    public void execute(Long gruppeId,List<String> environments){
        Gruppe gruppe = findGruppeById.execute(gruppeId);
        List<Person> personerIGruppen = gruppe.getPersoner();

        List<String> identer = ekstraherIdenterFraPersoner(personerIGruppen);
        Set<String> identerSomIkkeFinnesiTPSiMiljoe = filtrerPaaIdenterTilgjengeligeIMiljo.filtrer(identer, new HashSet<>(environments));
        List<Person> personerSomIkkeEksitererITPSMiljoe = personerSomIkkeFinnesIMiljoe(identerSomIkkeFinnesiTPSiMiljoe,  personerIGruppen);

        skdCreatePersoner.execute(NAVN_INNVANDRINGSMELDING, personerSomIkkeEksitererITPSMiljoe, environments);

        List<Person> personerMedRelasjoner = getPersonerMedRelasjoner(personerSomIkkeEksitererITPSMiljoe);

        for(Person person : personerMedRelasjoner){
            for(Relasjon relasjon : person.getRelasjoner()){
                String skdMeldingNavn = getSkdMeldingNavn(relasjon);
                skdCreatePersoner.execute(skdMeldingNavn, Arrays.asList(person), environments);
            }
        }
    }

    private List<Person> personerSomIkkeFinnesIMiljoe(Set<String> identerSomIkkeFinnesiTPSiMiljoe, List<Person> personer) {
        List<Person> personerSomIkkeAlleredeFinnesIMiljoe = new ArrayList<>();
        for(Person person : personer) {
            if(identerSomIkkeFinnesiTPSiMiljoe.contains(person.getIdent())) {
                personerSomIkkeAlleredeFinnesIMiljoe.add(person);
            }
        }
        return personerSomIkkeAlleredeFinnesIMiljoe;
    }


    private String getSkdMeldingNavn(Relasjon relasjon){
        //TODO Legg til for barn. Men har ikke lagd 98 korreksjon av familieopplysninger enda
        switch (relasjon.getRelasjonTypeNavn()){
            case "EKTEFELLE":
                return "Vigsel";
            default:
                return "Vigsel";
        }
    }

    private List<Person> getPersonerMedRelasjoner(List<Person> personerTidligereLagret) {
        List<Person> personer = new ArrayList<>();
        for(Person person : personerTidligereLagret){
            if(!person.getRelasjoner().isEmpty()){
                personer.add(person);
            }
        }
        return personer;
    }

    private List<String> ekstraherIdenterFraPersoner(List<Person> personer) {
        List<String> identer = new ArrayList<>();
        for(Person person : personer){
            identer.add(person.getIdent());
        }
        return identer;
    }
}
