package no.nav.tps.forvalteren.service.command.testdata;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import no.nav.tps.forvalteren.domain.jpa.Fullmakt;
import no.nav.tps.forvalteren.domain.jpa.IdentHistorikk;
import no.nav.tps.forvalteren.domain.jpa.Person;
import no.nav.tps.forvalteren.domain.jpa.Vergemaal;
import no.nav.tps.forvalteren.repository.jpa.PersonRepository;

@Service
@RequiredArgsConstructor
public class PeripheralPersonService {

    private final PersonRepository personRepository;

    public List<Person> getPersoner(List<Person> personerIGruppen) {

        Set<String> identer = new HashSet<>();
        identer.addAll(personerIGruppen.stream()
                .map(Person::getIdentHistorikk)
                .flatMap(Collection::stream)
                .map(IdentHistorikk::getAliasPerson)
                .map(Person::getIdent)
                .collect(Collectors.toSet()));
        identer.addAll(personerIGruppen.stream()
                .map(Person::getVergemaal)
                .flatMap(Collection::stream)
                .map(Vergemaal::getVerge)
                .map(Person::getIdent)
                .collect(Collectors.toSet()));
        identer.addAll(personerIGruppen.stream()
                .map(Person::getFullmakt)
                .flatMap(Collection::stream)
                .map(Fullmakt::getFullmektig)
                .map(Person::getIdent)
                .collect(Collectors.toSet()));

        return personRepository.findByIdentIn(identer);
    }
}
