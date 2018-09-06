package no.nav.tps.forvalteren.service.command.testdata.opprett;

import java.security.SecureRandom;
import java.util.List;
import org.springframework.stereotype.Service;

import no.nav.tps.forvalteren.domain.jpa.Person;

@Service
public class SetNameOnPersonsService {

    private static final String[] fornavn = { "Blå", "Gul", "Grønn", "Rask", "Døll", "Artig", "Stor", "Kriminell", "Luguber", "Smekker", "Glitrende", "Raus", "Naturlig", "Smart", "Kreativ", "God", "Vakker", "Snill",
            "Lunken", "Absurd", "Robust", "Blåøyd", "Tykkmaget", "Sedat", "Korrupt", "Godslig", "Nobel", "Slapp", "Talentfull", "Frodig", "Treig" };
    private static final String[] etternavn = { "Ert", "Hest", "Dorull", "Hatt", "Maskin", "Kaffi", "Kake", "Potet", "Konsoll", "Bærepose", "Sjokk", "Blyant", "Penn", "Bolle", "Saks", "Kopp", "Skilpadde", "Busk", "Nordmann",
            "Veggpryd", "Lapp" };

    private static SecureRandom randGenerator = new SecureRandom();

    public void execute(List<Person> personer) {
        for (Person person : personer) {
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
