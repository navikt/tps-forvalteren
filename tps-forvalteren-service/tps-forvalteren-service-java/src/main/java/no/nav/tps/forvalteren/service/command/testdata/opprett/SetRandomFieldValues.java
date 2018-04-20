package no.nav.tps.forvalteren.service.command.testdata.opprett;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import no.nav.tps.forvalteren.domain.jpa.Person;
import no.nav.tps.forvalteren.service.command.testdata.utils.DateGenerator;
import org.springframework.stereotype.Service;

@Service
public class SetRandomFieldValues {

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

    private LocalDate getPersonFodselsDato(Person person) {
        DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyyMMdd");
        String personBirthdayDay = person.getIdent().substring(0, 2);
        String personBirthdayMonth = person.getIdent().substring(2, 4);
        String personLastDigitsBirthdayYear = person.getIdent().substring(4, 6);
        String lastDigitsCurrentYear = LocalDate.now().toString().substring(2, 4);
        String personFirstDigitsBirthdayYear = Integer.parseInt(personLastDigitsBirthdayYear) <= Integer.parseInt(lastDigitsCurrentYear) ? "20" : "19";

        String personBirthday = personFirstDigitsBirthdayYear + personLastDigitsBirthdayYear + personBirthdayMonth + personBirthdayDay;

        return LocalDate.parse(personBirthday, format);
    }

    private static int concatenateInt(int int1, int int2) {
        StringBuilder builder = new StringBuilder(int1);
        builder.append(int2);

        return Integer.parseInt(builder.toString());
    }

    private LocalDateTime getRandomDoedsdato(Person person) {
        return LocalDateTime.of(DateGenerator.genererRandomDatoInnenforIntervalInclusiveDatoEtterExclusiveDatoFoer(getPersonFodselsDato(person), LocalDate.now()), LocalTime.now());
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
