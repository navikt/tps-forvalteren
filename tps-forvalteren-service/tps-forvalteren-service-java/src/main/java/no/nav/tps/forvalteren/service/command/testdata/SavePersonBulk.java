package no.nav.tps.forvalteren.service.command.testdata;

import static java.util.Objects.nonNull;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import com.google.common.collect.Lists;

import no.nav.tps.forvalteren.domain.jpa.Person;
import no.nav.tps.forvalteren.repository.jpa.PersonRepository;

@Service
public class SavePersonBulk {

    @Autowired
    private PersonRepository personRepository;

    private static final int ORACLE_MAX_SUM_IN_QUERY = 1000;

    public List<Person> execute(List<Person> persons) {

        persons.forEach(person ->
                person.setRegdato(nonNull(person.getRegdato()) ? person.getRegdato() : LocalDateTime.now()));

        List<Person> personerTilLagring = new ArrayList<>();
        try {

            if (persons.size() > ORACLE_MAX_SUM_IN_QUERY) {
                List<List<Person>> partitionsIds = Lists.partition(persons, ORACLE_MAX_SUM_IN_QUERY);
                for (List<Person> partition : partitionsIds) {
                    personerTilLagring.addAll((List) personRepository.saveAll(partition));
                }
                return persons;
            } else {
                personerTilLagring.addAll((List) personRepository.saveAll(persons));
                return personerTilLagring;
            }

        } catch (DataIntegrityViolationException e) {
            throw new DataIntegrityViolationException(
                    String.format("En T_PERSON DB constraint er brutt! Kan ikke lagre Person. Error: %s Cause: %s",
                            e.getMessage(), e.getCause().getCause()), e);
        }
    }
}
