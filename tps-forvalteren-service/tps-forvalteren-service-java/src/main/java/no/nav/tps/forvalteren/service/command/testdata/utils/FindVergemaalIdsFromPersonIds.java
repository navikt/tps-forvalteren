package no.nav.tps.forvalteren.service.command.testdata.utils;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import no.nav.tps.forvalteren.domain.jpa.Person;
import no.nav.tps.forvalteren.domain.jpa.Vergemaal;
import no.nav.tps.forvalteren.repository.jpa.PersonRepository;
import no.nav.tps.forvalteren.repository.jpa.VergemaalRepository;

@Service
public class FindVergemaalIdsFromPersonIds {

    @Autowired
    private PersonRepository personRepository;

    @Autowired
    private VergemaalRepository vergemaalRepository;

    public List<Long> execute(List<Long> liste) {
        List<Person> personerIGruppe = personRepository.findByIdIn(liste);
        List<String> listeFnr = personerIGruppe.stream()
                .map(person -> person.getIdent())
                .collect(Collectors.toList());

        List<Vergemaal> vergemaalListe = vergemaalRepository.findAllByIdentIn(listeFnr);
        return vergemaalListe.stream()
                .map(vergemaal -> vergemaal.getId())
                .distinct()
                .collect(Collectors.toList());
    }
}
