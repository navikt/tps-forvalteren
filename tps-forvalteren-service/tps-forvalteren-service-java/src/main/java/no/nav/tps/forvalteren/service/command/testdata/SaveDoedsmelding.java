package no.nav.tps.forvalteren.service.command.testdata;

import java.util.ArrayList;
import java.util.List;

import no.nav.tps.forvalteren.domain.jpa.Doedsmelding;
import no.nav.tps.forvalteren.domain.jpa.Person;
import no.nav.tps.forvalteren.repository.jpa.DoedsmeldingRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SaveDoedsmelding {

    private static final Boolean MELDING_SENDT = true;

    @Autowired
    private DoedsmeldingRepository doedsmeldingRepository;

    public void execute(List<Person> personer){
        List<Doedsmelding> doedsmeldinger = new ArrayList<>();
        for (Person person : personer) {
            Doedsmelding newDoedsmelding = new Doedsmelding();
            newDoedsmelding.setPerson(person);
            newDoedsmelding.setMeldingSendt(MELDING_SENDT);
            doedsmeldinger.add(newDoedsmelding);
        }
        doedsmeldingRepository.save(doedsmeldinger);
    }
}
