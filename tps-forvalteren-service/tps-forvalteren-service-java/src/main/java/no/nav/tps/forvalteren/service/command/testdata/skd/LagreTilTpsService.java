package no.nav.tps.forvalteren.service.command.testdata.skd;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import no.nav.tps.forvalteren.domain.jpa.Gruppe;
import no.nav.tps.forvalteren.domain.jpa.Person;
import no.nav.tps.forvalteren.service.command.testdata.FindGruppeById;
import no.nav.tps.forvalteren.service.command.testdata.FindPersonerSomSkalHaFoedselsmelding;
import no.nav.tps.forvalteren.service.command.testdata.FindPersonsNotInEnvironments;
import no.nav.tps.forvalteren.service.command.testdata.UppercaseDataInPerson;
import no.nav.tps.forvalteren.service.command.testdata.response.lagreTilTps.RsSkdMeldingResponse;
import no.nav.tps.forvalteren.service.command.testdata.response.lagreTilTps.SendSkdMeldingTilTpsResponse;
import no.nav.tps.forvalteren.service.command.testdata.response.lagreTilTps.ServiceRoutineResponseStatus;

@Service
public class LagreTilTpsService {
    @Autowired
    private FindPersonsNotInEnvironments findPersonsNotInEnvironments;
    
    @Autowired
    private FindPersonerSomSkalHaFoedselsmelding findPersonerSomSkalHaFoedselsmelding;

    @Autowired
    private FindGruppeById findGruppeById;

    @Autowired
    private SendNavEndringsmeldinger sendNavEndringsmeldinger;

    @Autowired
    private UppercaseDataInPerson uppercaseDataInPerson;

    @Autowired
    private SkdMeldingSender skdMeldingSender;

    public RsSkdMeldingResponse execute(Long gruppeId, List<String> environments) {
        Gruppe gruppe = findGruppeById.execute(gruppeId);
        List<Person> personerIGruppen = gruppe.getPersoner();
        return sendMeldinger(personerIGruppen, environments);
    }

    public RsSkdMeldingResponse execute(List<Person> personerIGruppen, List<String> environments) {
        return sendMeldinger(personerIGruppen, environments);
    }

    private RsSkdMeldingResponse sendMeldinger(List<Person> personerIGruppen, List<String> environments){
        for (Person person : personerIGruppen) {
            uppercaseDataInPerson.execute(person);
        }
        List<Person> personerSomIkkeEksitererITpsMiljoe = findPersonsNotInEnvironments.execute(personerIGruppen, environments);
        List<Person> personerSomAlleredeEksitererITpsMiljoe = createListPersonerSomAlleredeEksiterer(personerIGruppen, personerSomIkkeEksitererITpsMiljoe);
        List<Person> personerSomSkalFoedes = findPersonerSomSkalHaFoedselsmelding.execute(personerIGruppen);

        Set<String> environmentsSet = new HashSet<>(environments);
        
        personerSomIkkeEksitererITpsMiljoe.removeAll(personerSomSkalFoedes);
        personerSomAlleredeEksitererITpsMiljoe.removeAll(personerSomSkalFoedes);
        personerSomSkalFoedes.removeAll(personerSomAlleredeEksitererITpsMiljoe);
        
        personerSomAlleredeEksitererITpsMiljoe.removeAll(personerSomIkkeEksitererITpsMiljoe);
        
        List<SendSkdMeldingTilTpsResponse> listTpsResponsene = new ArrayList<>();

        listTpsResponsene.addAll(skdMeldingSender.sendInnvandringsMeldinger(personerSomIkkeEksitererITpsMiljoe, environmentsSet));
        listTpsResponsene.addAll(skdMeldingSender.sendUpdateInnvandringsMeldinger(personerSomAlleredeEksitererITpsMiljoe, environmentsSet));
        listTpsResponsene.addAll(skdMeldingSender.sendFoedselsMeldinger(personerSomSkalFoedes, environmentsSet));
        listTpsResponsene.addAll(skdMeldingSender.sendRelasjonsmeldinger(personerIGruppen, environmentsSet));
        listTpsResponsene.addAll(skdMeldingSender.sendDoedsmeldinger(personerIGruppen, environmentsSet));
        listTpsResponsene.addAll(skdMeldingSender.sendVergemaalsmeldinger(personerIGruppen, environmentsSet));
        listTpsResponsene.addAll(skdMeldingSender.sendUtvandringsmeldinger(personerSomAlleredeEksitererITpsMiljoe, environmentsSet));

        List<ServiceRoutineResponseStatus> serviceRoutineResponseList = sendNavEndringsmeldinger.execute(personerSomIkkeEksitererITpsMiljoe, environmentsSet);

        return new RsSkdMeldingResponse(null, listTpsResponsene, serviceRoutineResponseList);
    }
    
    private List<Person> createListPersonerSomAlleredeEksiterer(List<Person> personerIGruppe, List<Person> personerSomIkkeEksisterer) {
        List<Person> personerSomAlleredeEksisterer = new ArrayList<>();
        personerSomAlleredeEksisterer.addAll(personerIGruppe);
        personerSomAlleredeEksisterer.removeAll(personerSomIkkeEksisterer);
        
        return personerSomAlleredeEksisterer;
    }
}