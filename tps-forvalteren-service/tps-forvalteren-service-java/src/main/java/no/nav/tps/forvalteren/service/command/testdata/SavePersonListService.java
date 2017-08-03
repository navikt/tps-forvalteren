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

import java.util.List;

@Service
public class SavePersonListService {

    @Autowired
    private PersonRepository personRepository;

    @Autowired
    private AdresseRepository adresseRepository;

    @Autowired
    private RelasjonForAndrePersonIEnRelasjonGetter relasjonForAndrePersonIEnRelasjonGetter;

    @Autowired
    private RelasjonRepository relasjonRepository;

    public void execute(List<Person> personer) {
        for (Person person : personer) {

            if(person.getRelasjoner() != null && !person.getRelasjoner().isEmpty()){
                for(Relasjon relasjon : person.getRelasjoner()){

                    if(relasjon.getId() != null) {
                        continue;
                    }

                    relasjonRepository.save(relasjon);

                    Relasjon relasjonB = relasjonForAndrePersonIEnRelasjonGetter.execute(relasjon);
                    relasjonRepository.save(relasjonB);
                }
            }

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
                adresseRepository.deleteById(personAdresseDB.getId());
            }
            person.setOpprettetDato(null);
            person.setOpprettetAv(null);
            person.setEndretDato(null);
            person.setEndretAv(null);
        }
        personRepository.save(personer);
    }
}
