package no.nav.tps.forvalteren.service.command.testdata;

import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import no.nav.tps.forvalteren.domain.jpa.Person;

@Service
public class FindPersonerWithoutDoedsmelding {

    @Autowired
    private SjekkDoedsmeldingSentForPerson sjekkDoedsmeldingSentForPerson;

    public List<Person> execute(List<Person> personer) {
        List<Person> personerWithoutDoedsmelding = new ArrayList<>();
        for (Person person : personer) {
            if (!sjekkDoedsmeldingSentForPerson.execute(person)) {
                personerWithoutDoedsmelding.add(person);
            }
        }
        return personerWithoutDoedsmelding;
    }
}