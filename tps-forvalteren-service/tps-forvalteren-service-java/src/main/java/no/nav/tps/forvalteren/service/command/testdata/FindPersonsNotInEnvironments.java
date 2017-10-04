package no.nav.tps.forvalteren.service.command.testdata;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import no.nav.tps.forvalteren.domain.jpa.Gruppe;
import no.nav.tps.forvalteren.domain.jpa.Person;

@Service
public class FindPersonsNotInEnvironments {

    @Autowired
    private FindGruppeById findGruppeById;

    @Autowired
    private FiltrerPaaIdenterTilgjengeligeIMiljo filtrerPaaIdenterTilgjengeligeIMiljo;

    public List<Person> execute(Long gruppeId, List<String> environments) {
        Gruppe gruppe = findGruppeById.execute(gruppeId);
        List<Person> personerIGruppen = gruppe.getPersoner();

        List<String> identer = ekstraherIdenterFraPersoner(personerIGruppen);
        Set<String> identerSomIkkeFinnesiTPSiMiljoe = filtrerPaaIdenterTilgjengeligeIMiljo.filtrer(identer, new HashSet<>(environments));

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
        List<String> identer = new ArrayList<>();
        for (Person person : personer) {
            identer.add(person.getIdent());
        }
        return identer;
    }
}
