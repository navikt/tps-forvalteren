package no.nav.tps.forvalteren.service.command.testdata;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import no.nav.tps.forvalteren.domain.jpa.IdentHistorikk;
import no.nav.tps.forvalteren.domain.jpa.Person;
import no.nav.tps.forvalteren.repository.jpa.PersonRepository;

@Service
public class FindPersonsNotInEnvironments {

    @Autowired
    private PersonRepository personRepository;

    @Autowired
    private FiltrerPaaIdenterTilgjengeligIMiljo filtrerPaaIdenterTilgjengeligIMiljo;

    public List<Person> execute(List<Person> personerIGruppen, Set<String> environments) {
        Set<String> identer = personerIGruppen.stream().map(Person::getIdent).collect(Collectors.toSet());
        personerIGruppen.forEach(person ->
                identer.addAll(person.getIdentHistorikk().stream().map(IdentHistorikk::getAliasPerson).map(Person::getIdent).collect(Collectors.toSet()))
        );
        Set<String> identerSomIkkeFinnesiTPSiMiljoe = filtrerPaaIdenterTilgjengeligIMiljo.filtrer(identer, environments);

        return personRepository.findByIdentIn(identerSomIkkeFinnesiTPSiMiljoe);
    }
}
