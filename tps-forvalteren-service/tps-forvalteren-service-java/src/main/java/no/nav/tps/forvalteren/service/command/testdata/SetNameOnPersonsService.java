package no.nav.tps.forvalteren.service.command.testdata;

import no.nav.tps.forvalteren.domain.jpa.Person;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.List;

@Service
public class SetNameOnPersonsService {

    private static final String[] fornavn = {"Blå", "Gul", "Grønn", "Rask", "Døll", "Artig", "Stor", "Kriminell", "Luguber", "Smekker", "Glitrende", "Raus", "Naturlig", "Smart", "Kreativ"};
    private static final String[] etternavn = {"Ert", "Hest", "Dorull", "Hatt", "Maskin", "Kaffi", "Kake", "Potet", "Konsoll", "Bærepose", "Blyant", "Penn", "Bolle", "Saks", "Kopp"};

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
