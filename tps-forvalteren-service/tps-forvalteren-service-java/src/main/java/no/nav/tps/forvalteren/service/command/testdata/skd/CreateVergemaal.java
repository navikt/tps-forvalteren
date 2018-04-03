package no.nav.tps.forvalteren.service.command.testdata.skd;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import no.nav.tps.forvalteren.domain.jpa.Person;
import no.nav.tps.forvalteren.domain.jpa.Vergemaal;
import no.nav.tps.forvalteren.repository.jpa.VergemaalRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CreateVergemaal {

    @Autowired
    VergemaalRepository vergemaalRepository;

    @Autowired
    SkdMessageCreatorTrans1 skdMessageCreatorTrans1;

    public List<String> execute(List<Person> personerSomIkkeEksitererITpsMiljoe, boolean addHeader) {

        List<Person> personerMedVerge = getPersonerMedVerger(personerSomIkkeEksitererITpsMiljoe);
        List<String> skdMeldinger = new ArrayList<>();
        for (Person person : personerMedVerge) {
            skdMeldinger.addAll(skdMessageCreatorTrans1.execute("Vergemaal", Arrays.asList(person), addHeader));
        }
        return skdMeldinger;
    }

    private List<Person> getPersonerMedVerger(List<Person> personerTidligereLagret) {

        List<Person> personer = new ArrayList<>();
        for (Person person : personerTidligereLagret) {
            List<Vergemaal> personerVergemaal = vergemaalRepository.findAllByIdent(person.getIdent());
            if (!personerVergemaal.isEmpty()) {
                personer.add(person);
            }
        }
        return personer;
    }
}
