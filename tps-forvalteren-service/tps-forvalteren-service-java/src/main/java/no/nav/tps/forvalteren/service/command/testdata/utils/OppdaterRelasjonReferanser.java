package no.nav.tps.forvalteren.service.command.testdata.utils;

import org.springframework.stereotype.Service;

import no.nav.tps.forvalteren.domain.jpa.Person;
import no.nav.tps.forvalteren.domain.jpa.Relasjon;

@Service
public class OppdaterRelasjonReferanser {

    public void execute(Person person, Person personFraDB) {
        if (person.getRelasjoner() != null) {
            for (Relasjon relasjonA : person.getRelasjoner()) {
                for (Relasjon relasjonB : personFraDB.getRelasjoner()) {
                    if ((relasjonA.getPersonRelasjonMed().getIdent().equals(relasjonB.getPersonRelasjonMed().getIdent()) &&
                            relasjonA.getRelasjonTypeNavn().equals(relasjonB.getRelasjonTypeNavn()))) {
                        relasjonA.setId(relasjonB.getId());
                    }
                }
            }
        }
    }
}
