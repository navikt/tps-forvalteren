package no.nav.tps.forvalteren.service.command.testdata;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;

import no.nav.tps.forvalteren.domain.jpa.Person;

@Service
public class FindDoedePersoner {

    public List<Person> execute(List<Person> personer) {

        // Does not consider if death is in the future
        return personer.stream()
                .filter(person -> person.getDoedsdato() != null)
                .collect(Collectors.toList());
    }
}
