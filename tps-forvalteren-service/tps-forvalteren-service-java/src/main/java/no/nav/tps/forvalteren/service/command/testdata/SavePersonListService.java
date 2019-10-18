package no.nav.tps.forvalteren.service.command.testdata;

import static java.util.Objects.nonNull;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import no.nav.tps.forvalteren.domain.jpa.Person;
import no.nav.tps.forvalteren.repository.jpa.AdresseRepository;
import no.nav.tps.forvalteren.repository.jpa.PersonRepository;
import no.nav.tps.forvalteren.repository.jpa.PostadresseRepository;
import no.nav.tps.forvalteren.repository.jpa.RelasjonRepository;
import no.nav.tps.forvalteren.service.command.testdata.utils.HentUtdaterteRelasjonIder;
import no.nav.tps.forvalteren.service.command.testdata.utils.OppdaterRelasjonReferanser;

@Service
public class SavePersonListService {

    @Autowired
    private PersonRepository personRepository;

    @Autowired
    private AdresseRepository adresseRepository;

    @Autowired
    private PostadresseRepository postadresseRepository;

    @Autowired
    private RelasjonRepository relasjonRepository;

    @Autowired
    private OppdaterRelasjonReferanser oppdaterRelasjonReferanser;

    @Autowired
    private HentUtdaterteRelasjonIder hentUtdaterteRelasjonIder;

    @Autowired
    private AdresseOgSpesregService adresseOgSpesregService;

    @Transactional
    public void execute(List<Person> personer) {

        for (Person person : personer) {
            Set<Long> utdaterteRelasjonIder = new HashSet<>();

            Person personDb = personRepository.findById(person.getId());
            if (nonNull(personDb)) {
                oppdaterRelasjonReferanser.execute(person, personDb);
                person.getSivilstander().forEach(sivilstand -> sivilstand.setPerson(person));
                utdaterteRelasjonIder = hentUtdaterteRelasjonIder.execute(person, personDb);
                adresseRepository.deleteAllByPerson(personDb);
                personDb.getPostadresse().forEach(adresse -> postadresseRepository.deletePostadresseById(adresse.getId()));
            }

            adresseOgSpesregService.updateAdresseOgSpesregAttributes(person);

            if (!utdaterteRelasjonIder.isEmpty()) {
                relasjonRepository.deleteByIdIn(utdaterteRelasjonIder);
            }

            personRepository.save(person);
        }

    }

}