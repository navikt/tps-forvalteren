package no.nav.tps.forvalteren.service.command.testdata.utils;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import no.nav.tps.forvalteren.domain.jpa.Person;
import org.springframework.stereotype.Service;

@Service
public class GetLocalDateBirthdayFromPerson {

    public LocalDate execute(Person person) {

        DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyyMMdd");

        String personBirthdayDay = person.getIdent().substring(0, 2);
        String personBirthdayMonth = person.getIdent().substring(2, 4);
        String personLastDigitsBirthdayYear = person.getIdent().substring(4, 6);

        String lastDigitsCurrentYear = LocalDate.now().toString().substring(2, 4);

        String personFirstDigitsBirthdayYear = Integer.parseInt(personLastDigitsBirthdayYear) <= Integer.parseInt(lastDigitsCurrentYear) ? "20" : "19";

        String personBirthday = personFirstDigitsBirthdayYear + personLastDigitsBirthdayYear + personBirthdayMonth + personBirthdayDay;

        return LocalDate.parse(personBirthday, format);
    }

}
