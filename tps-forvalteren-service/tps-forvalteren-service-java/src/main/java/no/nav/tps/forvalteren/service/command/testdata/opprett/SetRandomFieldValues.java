package no.nav.tps.forvalteren.service.command.testdata.opprett;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import no.nav.tps.forvalteren.domain.jpa.Person;
import no.nav.tps.forvalteren.service.command.testdata.utils.DateGenerator;
import no.nav.tps.forvalteren.service.command.testdata.utils.HentDatoFraIdentService;

@Service
public class SetRandomFieldValues {

    private static final Random random = new Random();

    @Autowired
    private HentDatoFraIdentService hentDatoFraIdentService;

    public Person execute(String fieldName, Person person) {

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
        case "spesregDato":
            person.setSpesregDato(getRandomSpesregdato(person));
            break;
        case "sivilstand":
            person.setSivilstand(getRandomSivilstand());
            break;
        default:
            break;
        }

        return person;
    }

    private LocalDateTime getRandomDoedsdato(Person person) {
        return LocalDateTime.of(DateGenerator.genererRandomDatoInnenforIntervalInclusiveDatoEtterExclusiveDatoFoer(
                hentDatoFraIdentService.extract(person.getIdent()).toLocalDate(), LocalDate.now()), LocalTime.now());
    }

    private LandKode getRandomLandKode() {
        List<LandKode> landkoder = Collections.unmodifiableList(Arrays.asList(LandKode.values()));
        return landkoder.get(random.nextInt(landkoder.size()));
    }

    private String getRandomSpesreg() {
        int spesregnr = random.nextInt(8);
        return String.valueOf(spesregnr);
    }

    private LocalDateTime getRandomSpesregdato(Person person) {
        return LocalDateTime.of(DateGenerator.genererRandomDatoInnenforIntervalInclusiveDatoEtterExclusiveDatoFoer(
                hentDatoFraIdentService.extract(person.getIdent()).toLocalDate(), LocalDate.now()), LocalTime.now());
    }

    private String getRandomSivilstand() {
        int sivilstandnr = random.nextInt(9);
        return String.valueOf(sivilstandnr);
    }
}
