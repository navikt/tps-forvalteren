package no.nav.tps.forvalteren.service.command.innvandring;

import no.nav.tps.forvalteren.domain.jpa.Person;
import no.nav.tps.forvalteren.repository.jpa.PersonRepository;
import no.nav.tps.forvalteren.service.command.testdata.utils.HentDatoFraIdent;
import no.nav.tps.forvalteren.service.command.tps.skdmelding.skdparam.utils.LandkodeEncoder;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AddInndringsdatoOgLandTilPersonerService {

    @Autowired
    private LandkodeEncoder landkodeEncoder;

    @Autowired
    private HentDatoFraIdent hentDatoFraIdent;

    @Autowired
    private PersonRepository personRepository;

    public void execute(List<Person> personer){
        personer.forEach(person -> {
            person.setInnvandretFraLand(landkodeEncoder.getRandomLandTla());
            person.setInnvandretFraLandFlyttedato(hentDatoFraIdent.extract(person.getIdent()));
        });

        personRepository.save(personer);
    }
}
