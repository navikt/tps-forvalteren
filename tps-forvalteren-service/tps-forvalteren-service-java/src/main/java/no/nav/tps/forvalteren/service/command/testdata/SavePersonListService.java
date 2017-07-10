package no.nav.tps.forvalteren.service.command.testdata;

import no.nav.tps.forvalteren.domain.jpa.Adresse;
import no.nav.tps.forvalteren.domain.jpa.Person;
import no.nav.tps.forvalteren.domain.jpa.Postadresse;
import no.nav.tps.forvalteren.repository.jpa.AdresseRepository;
import no.nav.tps.forvalteren.repository.jpa.PersonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SavePersonListService {

    @Autowired
    private PersonRepository personRepository;

    @Autowired
    private AdresseRepository adresseRepository;

    public void execute(List<Person> personer) {
        for (Person person : personer) {
            if (person.getPostadresse() != null) {
                for (Postadresse adr : person.getPostadresse()) {
                    adr.setPerson(person);
                }
            }
            if (person.getBoadresse() != null) {
                Adresse personAdresseDB = adresseRepository.findAdresseByPersonId(person.getId());
                if (personAdresseDB == null) {
                    continue;
                }
                adresseRepository.deleteById(personAdresseDB.getId());
            }
        }
        personRepository.save(personer);
    }
}
