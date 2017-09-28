package no.nav.tps.forvalteren.service.command.testdata;

import no.nav.tps.forvalteren.domain.jpa.Adresse;
import no.nav.tps.forvalteren.domain.jpa.Person;
import no.nav.tps.forvalteren.domain.jpa.Postadresse;
import no.nav.tps.forvalteren.domain.jpa.Relasjon;
import no.nav.tps.forvalteren.repository.jpa.AdresseRepository;
import no.nav.tps.forvalteren.repository.jpa.PersonRepository;
import no.nav.tps.forvalteren.repository.jpa.RelasjonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

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

    public void execute(List<Person> personer) {

        Set<Long> ids = new HashSet<>();
        for (Person person : personer) {

            Person personFraDB = personRepository.findById(person.getId());
            if (personFraDB != null) {
                relasjonerSomSkalOppdateres(person, personFraDB);
                relasjonerSomSkalFjernes(person, personFraDB, ids);
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
                if (personAdresseDB == null) {
                    continue;
                }
                person.getBoadresse().setId(personAdresseDB.getId());
            }
            person.setOpprettetDato(null);
            person.setOpprettetAv(null);
            person.setEndretDato(null);
            person.setEndretAv(null);
        }
        personRepository.save(personer);

        // Sletter relasjoner etter at personer er lagret.
        for (Long id : ids) {
            relasjonRepository.deleteById(id);
        }
    }

    private void relasjonerSomSkalOppdateres(Person person, Person personFraDB) {
        if (person.getRelasjoner() != null) {
            for (Relasjon relasjonA : person.getRelasjoner()) {
                for (Relasjon relasjonB : personFraDB.getRelasjoner()) {
                    if ((relasjonA.getPersonRelasjonMed().getIdent().equals(relasjonB.getPersonRelasjonMed().getIdent()) &&
                            relasjonA.getRelasjonTypeNavn().equals(relasjonB.getRelasjonTypeNavn()) )) {
                        relasjonA.setId(relasjonB.getId());
                    }
                }
            }
        }
    }

    private void relasjonerSomSkalFjernes(Person person, Person personFraDB, Set<Long> ids) {
        if (personFraDB.getRelasjoner() != null) {
            for (Relasjon relasjonA : personFraDB.getRelasjoner()) {
                boolean found = false;
                if (person.getRelasjoner() != null) {
                    for (Relasjon relasjonB : person.getRelasjoner()) {
                        if ((relasjonA.getPersonRelasjonMed().getIdent().equals(relasjonB.getPersonRelasjonMed().getIdent()) &&
                                relasjonA.getRelasjonTypeNavn().equals(relasjonB.getRelasjonTypeNavn()) )) {
                            found = true;
                        }
                    }
                }
                if (!found) {
                    ids.add(relasjonA.getId());
                }
            }
        }
    }


}