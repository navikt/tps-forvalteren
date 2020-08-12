package no.nav.tps.forvalteren.service.command.testdata;

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

        Set<String> identerSomIkkeFinnesIMiljoe = filtrerPaaIdenterTilgjengeligIMiljo.filtrer(
                personerIGruppen.stream().map(Person::getIdent).collect(Collectors.toList()), environments);

        return personerIGruppen.stream()
                .filter(person -> identerSomIkkeFinnesIMiljoe.stream().anyMatch(ident -> ident.equals(person.getIdent())))
                .collect(Collectors.toList());
    }
}