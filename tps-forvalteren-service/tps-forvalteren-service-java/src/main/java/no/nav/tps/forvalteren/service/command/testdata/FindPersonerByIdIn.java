package no.nav.tps.forvalteren.service.command.testdata;

import static no.nav.tps.forvalteren.service.command.testdata.utils.TestdataConstants.ORACLE_MAX_IN_SET_ELEMENTS;

import no.nav.tps.forvalteren.domain.jpa.Person;
import no.nav.tps.forvalteren.repository.jpa.PersonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import com.google.common.collect.Lists;

@Service
public class FindPersonerByIdIn {

    @Autowired
    private PersonRepository personRepository;

    public List<Person> execute(List<String> identer){
        if(identer.size() > ORACLE_MAX_IN_SET_ELEMENTS) {
            List<Person> persistedPersoner = new ArrayList<>();
            List<List<String>> partitionsIdenter = Lists.partition(identer, ORACLE_MAX_IN_SET_ELEMENTS);
            for (List<String> partition : partitionsIdenter) {
                persistedPersoner.addAll(personRepository.findByIdentIn(partition));
            }
            return persistedPersoner;
        } else {
            return personRepository.findByIdentIn(identer);
        }
    }
    
}
