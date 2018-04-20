package no.nav.tps.forvalteren.service.command.testdata.opprett;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import no.nav.tps.forvalteren.domain.jpa.Person;
import org.springframework.stereotype.Service;

@Service
public class GetRandomFieldValues {

    private static final Random random = new Random();

    public void execute(String fieldName, Person person) {

        switch (fieldName) {
            case "doedsdato":
                person.setDoedsdato(getRandomDoedsdato(person));
                break;
            case "statsborgerskap":
                person.setStatsborgerskap(getRandomLandKode().getLandkodesiffer());
                break;
            case "spesreg":
                person.setSpesreg(getRandomSpesreg());
                break;


        }

    }

    private LocalDateTime getRandomDoedsdato(Person person) {
        //        String birthday = person.getIdent().substring(0,8);
        //        int birthdayYear = Integer.parseInt(person.getIdent().substring(0,4));
        //        int birthdayMonth = Integer.parseInt(person)
        //        long minDate = LocalDateTime.of()
        LocalDateTime today = LocalDateTime.now();
        return today;
    }

    private LandKode getRandomLandKode() {
        List<LandKode> landkoder = Collections.unmodifiableList(Arrays.asList(LandKode.values()));
        return landkoder.get(random.nextInt(landkoder.size()));
    }

    private String getRandomSpesreg() {
        int spesregnr = random.nextInt(8);
        return String.valueOf(spesregnr);
    }

}
