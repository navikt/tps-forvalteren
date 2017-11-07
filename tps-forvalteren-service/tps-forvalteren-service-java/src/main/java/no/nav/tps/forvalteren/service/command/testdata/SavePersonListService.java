package no.nav.tps.forvalteren.service.command.testdata;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import no.nav.tps.forvalteren.domain.jpa.Adresse;
import no.nav.tps.forvalteren.domain.jpa.Person;
import no.nav.tps.forvalteren.domain.jpa.Postadresse;
import no.nav.tps.forvalteren.repository.jpa.AdresseRepository;
import no.nav.tps.forvalteren.repository.jpa.PersonRepository;
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
    private RelasjonRepository relasjonRepository;

    @Autowired
    private UppercaseDataInPerson uppercaseDataInPerson;

    @Autowired
    private OppdaterRelasjonReferanser oppdaterRelasjonReferanser;

    @Autowired
    private HentUtdaterteRelasjonIder hentUtdaterteRelasjonIder;

    public void execute(List<Person> personer) {

        Set<Long> utdaterteRelasjonIder = new HashSet<>();
        for (Person person : personer) {

            Person personFraDB = personRepository.findById(person.getId());
            if (personFraDB != null) {
                oppdaterRelasjonReferanser.execute(person, personFraDB);
                utdaterteRelasjonIder = hentUtdaterteRelasjonIder.execute(person, personFraDB);
            }

            uppercaseDataInPerson.execute(person);
            if (person.getPostadresse() != null) {
                for (Postadresse adr : person.getPostadresse()) {
                    adr.setPerson(person);
                }
            }

            if (person.getBoadresse() != null) {
                person.getBoadresse().setPerson(person);
                Adresse personAdresseDB = adresseRepository.findAdresseByPersonId(person.getId());
                if (personAdresseDB != null) {
                    adresseRepository.deleteById(personAdresseDB.getId());
                }
            }

            person.setOpprettetDato(null);
            person.setOpprettetAv(null);
            person.setEndretDato(null);
            person.setEndretAv(null);
        }
        personRepository.save(personer);

        if (!utdaterteRelasjonIder.isEmpty()) {
            relasjonRepository.deleteByIdIn(utdaterteRelasjonIder);
        }

    }

}