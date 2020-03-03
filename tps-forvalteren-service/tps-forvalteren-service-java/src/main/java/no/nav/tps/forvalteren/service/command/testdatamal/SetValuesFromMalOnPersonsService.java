package no.nav.tps.forvalteren.service.command.testdatamal;

import static java.util.Arrays.asList;
import static java.util.Collections.singletonList;
import static no.nav.tps.forvalteren.domain.jpa.InnvandretUtvandret.INNUTVANDRET.INNVANDRET;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Iterator;
import java.util.List;
import org.springframework.stereotype.Service;

import no.nav.tps.forvalteren.domain.jpa.InnvandretUtvandret;
import no.nav.tps.forvalteren.domain.jpa.Person;
import no.nav.tps.forvalteren.domain.jpa.Personmal;
import no.nav.tps.forvalteren.domain.jpa.Statsborgerskap;

@Service
public class SetValuesFromMalOnPersonsService {

    public void execute(List<Person> personer, List<Personmal> personmalList) {

        Iterator<Person> personIterator = personer.iterator();
        for (Personmal personmal : personmalList) {
            for (int i = 0; i < personmal.getAntallIdenter(); i++) {
                if (personIterator.hasNext()) {
                    Person person = personIterator.next();
                    person.setSivilstand(personmal.getSivilstand());
                    person.setDoedsdato(tolocalDateTime(personmal.getDoedsdato()));
                    person.setStatsborgerskap(asList(Statsborgerskap.builder()
                            .statsborgerskap(personmal.getStatsborgerskap())
                            .statsborgerskapRegdato(tolocalDateTime(personmal.getStatsborgerskapRegdato()))
                            .build()));
                    person.setSpesreg(personmal.getSpesreg());
                    person.setSpesregDato(tolocalDateTime(personmal.getSpesregDato()));
                    person.setEgenAnsattDatoFom(tolocalDateTime(personmal.getEgenAnsattDatoFom()));
                    person.setEgenAnsattDatoTom(tolocalDateTime(personmal.getEgenAnsattDatoTom()));
                    person.setInnvandretUtvandret(singletonList(
                            InnvandretUtvandret.builder()
                                    .innutvandret(INNVANDRET)
                                    .landkode(personmal.getInnvandretFraLand())
                                    .flyttedato(tolocalDateTime(personmal.getInnvandretFraLandFlyttedato()))
                                    .build()));
                    person.setSikkerhetsTiltakDatoFom(tolocalDateTime(personmal.getSikkerhetsTiltakDatoFom()));
                    person.setSikkerhetsTiltakDatoTom(tolocalDateTime(personmal.getSikkerhetsTiltakDatoTom()));
                    person.setBeskrSikkerhetsTiltak(personmal.getBeskrSikkerhetsTiltak());
                }
            }
        }
    }

    private LocalDateTime tolocalDateTime(LocalDate localDate) {
        return localDate != null ? localDate.atStartOfDay() : null;
    }
}
