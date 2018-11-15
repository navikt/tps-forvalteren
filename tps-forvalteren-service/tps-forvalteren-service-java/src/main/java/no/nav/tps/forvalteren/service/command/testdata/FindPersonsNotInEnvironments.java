package no.nav.tps.forvalteren.service.command.testdata;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.google.common.collect.Lists;

import no.nav.tps.forvalteren.domain.jpa.Person;

@Service
public class FindPersonsNotInEnvironments {

    @Autowired
    private FiltrerPaaIdenterTilgjengeligeIMiljo filtrerPaaIdenterTilgjengeligeIMiljo;

    public List<Person> execute(List<Person> personerIGruppen, Set<String> environments) {
        List<String> identer = ekstraherIdenterFraPersoner(personerIGruppen);
        Set<String> identerSomIkkeFinnesiTPSiMiljoe = filtrerPaaIdenterTilgjengeligeIMiljo.filtrer(identer, environments);

        return personerSomIkkeFinnesIMiljoe(identerSomIkkeFinnesiTPSiMiljoe, personerIGruppen);
    }

    private List<Person> personerSomIkkeFinnesIMiljoe(Set<String> identerSomIkkeFinnesiTPSiMiljoe, List<Person> personer) {
        List<Person> personerSomIkkeAlleredeFinnesIMiljoe = new ArrayList<>();
        for (Person person : personer) {
            if (identerSomIkkeFinnesiTPSiMiljoe.contains(person.getIdent())) {
                personerSomIkkeAlleredeFinnesIMiljoe.add(person);
            }
        }
        return personerSomIkkeAlleredeFinnesIMiljoe;
    }

    private List<String> ekstraherIdenterFraPersoner(List<Person> personer) {

        List<String> identer = Lists.newArrayListWithExpectedSize(personer.size());

        for (Person person : personer) {
            identer.add(person.getIdent());
        }
        return identer;
    }
}
