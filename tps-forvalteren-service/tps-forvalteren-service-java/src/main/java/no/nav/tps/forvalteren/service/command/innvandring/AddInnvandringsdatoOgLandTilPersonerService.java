package no.nav.tps.forvalteren.service.command.innvandring;

import static java.util.Objects.isNull;
import static org.apache.commons.lang3.StringUtils.isBlank;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import no.nav.tps.forvalteren.domain.jpa.Person;
import no.nav.tps.forvalteren.repository.jpa.PersonRepository;
import no.nav.tps.forvalteren.service.command.testdata.utils.HentDatoFraIdentService;
import no.nav.tps.forvalteren.service.command.tps.skdmelding.skdparam.utils.LandkodeEncoder;

@Service
public class AddInnvandringsdatoOgLandTilPersonerService {

    @Autowired
    private LandkodeEncoder landkodeEncoder;

    @Autowired
    private HentDatoFraIdentService hentDatoFraIdent;

    @Autowired
    private PersonRepository personRepository;

    public void execute(List<Person> personer) {
        personer.forEach(person -> {
            if (isBlank(person.getInnvandretFraLand())) {
                person.setInnvandretFraLand(landkodeEncoder.getRandomLandTla());
            }
            if (isNull(person.getInnvandretFraLandFlyttedato())) {
                person.setInnvandretFraLandFlyttedato(hentDatoFraIdent.extract(person.getIdent()));
            }
        });

        personRepository.save(personer);
    }
}
