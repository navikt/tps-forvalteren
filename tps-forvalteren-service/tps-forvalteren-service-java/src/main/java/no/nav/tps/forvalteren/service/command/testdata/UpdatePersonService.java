package no.nav.tps.forvalteren.service.command.testdata;

import static java.util.Objects.nonNull;

import java.util.Set;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import no.nav.tps.forvalteren.domain.jpa.Person;
import no.nav.tps.forvalteren.repository.jpa.AdresseRepository;
import no.nav.tps.forvalteren.repository.jpa.PersonRepository;
import no.nav.tps.forvalteren.repository.jpa.RelasjonRepository;
import no.nav.tps.forvalteren.service.command.testdata.utils.HentUtdaterteRelasjonIder;
import no.nav.tps.forvalteren.service.command.testdata.utils.OppdaterRelasjonReferanser;

@Service
public class UpdatePersonService {

    @Autowired
    private PersonRepository personRepository;

    @Autowired
    private AdresseRepository adresseRepository;

    @Autowired
    private RelasjonRepository relasjonRepository;

    @Autowired
    private OppdaterRelasjonReferanser oppdaterRelasjonReferanser;

    @Autowired
    private HentUtdaterteRelasjonIder hentUtdaterteRelasjonIder;

    @Autowired
    private AdresseOgSpesregService adresseOgSpesregService;

    @Transactional
    public void update(Person person) {

        Set<Long> utdaterteRelasjonIder = null;

        Person storedPerson = personRepository.findById(person.getId());
        if (nonNull(storedPerson)) {
            oppdaterRelasjonReferanser.execute(person, storedPerson);
            utdaterteRelasjonIder = hentUtdaterteRelasjonIder.execute(person, storedPerson);
            adresseRepository.deleteAllByPerson(storedPerson);
        }

        adresseOgSpesregService.updateAdresseOgSpesregAttributes(person);

        if (nonNull(utdaterteRelasjonIder)) {
            relasjonRepository.deleteByIdIn(utdaterteRelasjonIder);
        }

        personRepository.save(person);
    }
}
