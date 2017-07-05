package no.nav.tps.forvalteren.service.command.testdata;

import no.nav.tps.forvalteren.domain.jpa.Gateadresse;
import no.nav.tps.forvalteren.domain.jpa.Person;
import no.nav.tps.forvalteren.domain.jpa.Postadresse;
import no.nav.tps.forvalteren.repository.jpa.PersonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SavePersonListService {

    @Autowired
    private PersonRepository repository;

    @PreAuthorize("hasRole('0000-GA-TPSF-SKRIV')")
    public void execute(List<Person> personer) {
        for (Person person : personer) {
            if (person.getGateadresse() != null) {
                for (Gateadresse adr : person.getGateadresse()) {
                    adr.setPerson(person);
                }
            }
            if (person.getPostadresse() != null) {
                for (Postadresse adr : person.getPostadresse()) {
                    adr.setPerson(person);
                }
            }
        }
        repository.save(personer);
    }
}
