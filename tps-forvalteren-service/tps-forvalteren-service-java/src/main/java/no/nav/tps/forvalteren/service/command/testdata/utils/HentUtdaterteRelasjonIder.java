package no.nav.tps.forvalteren.service.command.testdata.utils;

import java.util.HashSet;
import java.util.Set;
import org.springframework.stereotype.Service;

import no.nav.tps.forvalteren.domain.jpa.Person;
import no.nav.tps.forvalteren.domain.jpa.Relasjon;

@Service
public class HentUtdaterteRelasjonIder {

    public Set<Long> execute(Person person, Person personFraDB) {
        Set<Long> utdaterteIder = new HashSet<>();
        if (personFraDB.getRelasjoner() != null) {
            for (Relasjon relasjonA : personFraDB.getRelasjoner()) {
                boolean found = false;
                if (person.getRelasjoner() != null) {
                    for (Relasjon relasjonB : person.getRelasjoner()) {
                        if ((relasjonA.getPersonRelasjonMed().getIdent().equals(relasjonB.getPersonRelasjonMed().getIdent()) &&
                                relasjonA.getRelasjonTypeNavn().equals(relasjonB.getRelasjonTypeNavn()))) {
                            found = true;
                        }
                    }
                }
                if (!found) {
                    utdaterteIder.add(relasjonA.getId());
                }
            }
        }
        return utdaterteIder;
    }
}
