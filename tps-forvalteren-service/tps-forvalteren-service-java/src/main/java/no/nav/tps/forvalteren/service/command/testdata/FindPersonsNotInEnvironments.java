package no.nav.tps.forvalteren.service.command.testdata;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import no.nav.tps.forvalteren.domain.jpa.Person;

@Service
public class FindPersonsNotInEnvironments {

    @Autowired
    private FiltrerPaaIdenterTilgjengeligIMiljo filtrerPaaIdenterTilgjengeligIMiljo;

    public List<Person> execute(List<Person> personerIGruppen, Set<String> environments) {
        List<String> identer = personerIGruppen.stream().map(ident -> ident.getIdent()).collect(Collectors.toList());
        Set<String> identerSomIkkeFinnesiTPSiMiljoe = filtrerPaaIdenterTilgjengeligIMiljo.filtrer(identer, environments);

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
}
