package no.nav.tps.forvalteren.service.command.testdatamal;

import static java.util.Arrays.asList;
import static java.util.Collections.singletonList;
import static java.util.Objects.nonNull;
import static no.nav.tps.forvalteren.domain.jpa.InnvandretUtvandret.InnUtvandret.INNVANDRET;

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
                    person.setDoedsdato(toLocalDateTime(personmal.getDoedsdato()));
                    person.setStatsborgerskap(asList(Statsborgerskap.builder()
                            .statsborgerskap(personmal.getStatsborgerskap())
                            .statsborgerskapRegdato(toLocalDateTime(personmal.getStatsborgerskapRegdato()))
                            .build()));
                    person.setSpesreg(personmal.getSpesreg());
                    person.setSpesregDato(toLocalDateTime(personmal.getSpesregDato()));
                    person.setEgenAnsattDatoFom(toLocalDateTime(personmal.getEgenAnsattDatoFom()));
                    person.setEgenAnsattDatoTom(toLocalDateTime(personmal.getEgenAnsattDatoTom()));
                    person.setInnvandretUtvandret(singletonList(
                            InnvandretUtvandret.builder()
                                    .innutvandret(INNVANDRET)
                                    .landkode(personmal.getInnvandretFraLand())
                                    .flyttedato(toLocalDateTime(personmal.getInnvandretFraLandFlyttedato()))
                                    .build()));
                    person.setSikkerhetTiltakDatoFom(toLocalDateTime(personmal.getSikkerhetsTiltakDatoFom()));
                    person.setSikkerhetTiltakDatoTom(toLocalDateTime(personmal.getSikkerhetsTiltakDatoTom()));
                    person.setBeskrSikkerhetTiltak(personmal.getBeskrSikkerhetsTiltak());
                }
            }
        }
    }

    private LocalDateTime toLocalDateTime(LocalDate localDate) {
        return nonNull(localDate) ? localDate.atStartOfDay() : null;
    }
}
