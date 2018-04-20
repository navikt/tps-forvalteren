package no.nav.tps.forvalteren.service.command.testdata.opprett.implementation;

import java.util.List;

import no.nav.tps.forvalteren.domain.jpa.Person;
import no.nav.tps.forvalteren.domain.rs.RsPersonMal;
import no.nav.tps.forvalteren.service.command.testdata.opprett.SetValuesFromMalOnPersonsService;
import no.nav.tps.forvalteren.service.command.testdatamal.PersonmalPersonMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DefaultSetValuesFromMalOnPersonsService implements SetValuesFromMalOnPersonsService {

    @Autowired
    private PersonmalPersonMapper personmalPersonMapper;

    public void execute(List<Person> listeMedPersoner, RsPersonMal inputPerson) {

        listeMedPersoner.forEach(person -> {
            person = personmalPersonMapper.execute(inputPerson, person);
        });
    }

}
