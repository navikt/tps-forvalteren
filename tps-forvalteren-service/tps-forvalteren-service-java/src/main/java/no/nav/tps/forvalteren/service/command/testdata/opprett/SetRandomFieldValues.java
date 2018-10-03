package no.nav.tps.forvalteren.service.command.testdata.opprett;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Random;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import no.nav.tps.forvalteren.domain.jpa.Person;
import no.nav.tps.forvalteren.service.command.testdata.utils.DateGenerator;
import no.nav.tps.forvalteren.service.command.testdata.utils.HentDatoFraIdentService;
import no.nav.tps.forvalteren.service.command.tps.skdmelding.skdparam.utils.LandkodeEncoder;

@Service
public class SetRandomFieldValues {

    private static final Random random = new Random();

    @Autowired
    private HentDatoFraIdentService hentDatoFraIdentService;

    @Autowired
    private LandkodeEncoder landkodeEncoder;

    public Person execute(String fieldName, Person person) {

        switch (fieldName) {
        case "doedsdato":
            person.setDoedsdato(getRandomDoedsdato(person));
            break;
        case "statsborgerskap":
            person.setStatsborgerskap(landkodeEncoder.getRandomLandTla());
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
