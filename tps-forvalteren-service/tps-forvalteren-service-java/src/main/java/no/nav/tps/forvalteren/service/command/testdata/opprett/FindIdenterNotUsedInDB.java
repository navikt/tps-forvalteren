package no.nav.tps.forvalteren.service.command.testdata.opprett;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import no.nav.tps.forvalteren.domain.jpa.Person;
import no.nav.tps.forvalteren.service.command.testdata.FindPersonerByIdIn;

@Service
public class FindIdenterNotUsedInDB {

    @Autowired
    private FindPersonerByIdIn findPersonerByIdIn;

    public Set<String> filtrer(Set<String> identer) {
        List<String> identListe = new ArrayList<>(identer);
        List<Person> personerSomFinnes = findPersonerByIdIn.execute(identListe);
        List<String> opptatteIdenter = personerSomFinnes.stream()
                .map(Person::getIdent)
                .collect(Collectors.toList());

        identListe.removeAll(opptatteIdenter);
        return new HashSet<>(identListe);
    }

}
