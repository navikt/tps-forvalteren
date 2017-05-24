package no.nav.tps.forvalteren.service.command.testdata;

import no.nav.tps.forvalteren.domain.jpa.Person;
import no.nav.tps.forvalteren.service.command.Command;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.List;

@Service
public class SetNameOnPersons implements Command {

    private static String[] fornavn = {"Blå", "Grønn", "Rask", "Døll", "Artig", "Stor", "Kriminell"};
    private static String[] etternavn = {"Ert", "Hest", "Dorull", "Hatt", "Maskin"};

    private static SecureRandom randGenerator = new SecureRandom();

    public void execute(List<Person> personer) {
        for(Person person : personer) {
            person.setFornavn(randomFornavn());
            person.setEtternavn(randomEtternavn());
        }
    }

    private String randomFornavn() {
        return fornavn[randGenerator.nextInt(fornavn.length)];
    }

    private String randomEtternavn() {
        return etternavn[randGenerator.nextInt(etternavn.length)];
    }

}
