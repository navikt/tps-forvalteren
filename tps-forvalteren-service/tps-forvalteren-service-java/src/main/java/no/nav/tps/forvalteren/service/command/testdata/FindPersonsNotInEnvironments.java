package no.nav.tps.forvalteren.service.command.testdata;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import no.nav.tps.forvalteren.domain.jpa.Person;

@Service
@RequiredArgsConstructor
public class FindPersonsNotInEnvironments {

    private final FiltrerPaaIdenterTilgjengeligIMiljo filtrerPaaIdenterTilgjengeligIMiljo;

    public List<Person> execute(List<Person> personerIGruppen, Set<String> environments) {

        Set<String> ikkeEksisterendeIdenter = filtrerPaaIdenterTilgjengeligIMiljo.filtrer(
                personerIGruppen.stream()
                        .map(Person::getIdent)
                        .collect(Collectors.toSet()),
                environments);

        return personerIGruppen.stream()
                .filter(person -> ikkeEksisterendeIdenter.stream()
                        .anyMatch(ident -> person.getIdent().equals(ident)))
                .collect(Collectors.toList());
    }
}
