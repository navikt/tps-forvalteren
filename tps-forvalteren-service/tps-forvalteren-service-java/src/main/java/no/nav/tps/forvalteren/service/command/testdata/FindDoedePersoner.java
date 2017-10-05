package no.nav.tps.forvalteren.service.command.testdata;

import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Service;

import no.nav.tps.forvalteren.domain.jpa.Person;

@Service
public class FindDoedePersoner {

    public List<Person> execute(List<Person> personer) {
        List<Person> doedePersoner = new ArrayList<>();

        for (Person person : personer) {
            // Does not consider if death is in the future
            if (person.getDoedsdato() != null) {
                doedePersoner.add(person);
            }
        }
        return doedePersoner;
    }
}
