package no.nav.tps.forvalteren.service.command.testdata.opprett.implementation;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import no.nav.tps.forvalteren.domain.jpa.Person;
import no.nav.tps.forvalteren.domain.rs.RsPersonMal;
import no.nav.tps.forvalteren.domain.rs.RsPersonMalRequest;
import no.nav.tps.forvalteren.service.command.testdata.opprett.SetValuesFromMalOnPersonsService;
import no.nav.tps.forvalteren.service.command.testdatamal.PersonmalPersonMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DefaultSetValuesFromMalOnPersonsService implements SetValuesFromMalOnPersonsService {

    @Autowired
    private PersonmalPersonMapper personmalPersonMapper;

    public void execute(List<Person> listeMedPersoner, RsPersonMalRequest inputPersonRequest) {
        AtomicInteger counter = new AtomicInteger(0);

        List<RsPersonMal> listeOverMaler = new ArrayList<>();

        for (int i = 0; i < inputPersonRequest.getInputPersonMalRequest().size(); i++) {
            listeOverMaler.add(inputPersonRequest.getInputPersonMalRequest().get(i));
        }

        listeMedPersoner.stream().forEach(Person -> {
            Person = personmalPersonMapper.execute(listeOverMaler.get(counter.get()), Person);
            counter.getAndIncrement();
        });

    }

}
