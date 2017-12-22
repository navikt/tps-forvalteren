package no.nav.tps.forvalteren.service.command.testdata;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.google.common.collect.Lists;

import no.nav.tps.forvalteren.domain.jpa.Person;
import no.nav.tps.forvalteren.repository.jpa.PersonRepository;

@Service
public class SavePersonBulk {

    @Autowired
    private PersonRepository personRepository;

    private static final int ORACLE_MAX_SUM_IN_QUERY = 1000;

    public void execute(List<Person> persons) {
        if (persons.size() > ORACLE_MAX_SUM_IN_QUERY) {
            List<List<Person>> partitionsIds = Lists.partition(persons, ORACLE_MAX_SUM_IN_QUERY);
            for (List<Person> partition : partitionsIds) {
                personRepository.save(partition);
            }
        } else {
            personRepository.save(persons);
        }
    }
}
