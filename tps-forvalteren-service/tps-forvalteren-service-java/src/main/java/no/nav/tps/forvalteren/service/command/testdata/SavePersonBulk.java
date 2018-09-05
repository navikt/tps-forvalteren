package no.nav.tps.forvalteren.service.command.testdata;

import com.google.common.collect.Lists;
import no.nav.tps.forvalteren.domain.jpa.Person;
import no.nav.tps.forvalteren.repository.jpa.PersonRepository;

import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

@Service
public class SavePersonBulk {

    @Autowired
    private PersonRepository personRepository;

    private static final int ORACLE_MAX_SUM_IN_QUERY = 1000;

    public List<Person> execute(List<Person> persons) {
        List<Person> lagredePersoner = new ArrayList<>();
        try {

            if (persons.size() > ORACLE_MAX_SUM_IN_QUERY) {
                List<List<Person>> partitionsIds = Lists.partition(persons, ORACLE_MAX_SUM_IN_QUERY);
                for (List<Person> partition : partitionsIds) {
                    lagredePersoner.addAll(personRepository.save(partition));
                }
                return persons;
            } else {
                lagredePersoner.addAll(personRepository.save(persons));
                return lagredePersoner;
            }

        } catch (DataIntegrityViolationException e) {
            throw new DataIntegrityViolationException("En T_PERSON DB constraint er brutt! Kan ikke lagre Person. Error: " + e.getMessage()
                    + " Cause: " + e.getCause().getCause());
        }
    }
}
