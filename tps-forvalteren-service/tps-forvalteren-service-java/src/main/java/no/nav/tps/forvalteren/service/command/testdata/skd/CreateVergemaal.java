package no.nav.tps.forvalteren.service.command.testdata.skd;

import java.util.ArrayList;
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

    public List<SkdMeldingTrans1> execute(List<Person> personerIGruppen, boolean addHeader) {

        List<Vergemaal> vergemaalIGruppen = new ArrayList<>();
        List<SkdMeldingTrans1> skdMeldinger = new ArrayList<>();

        for (Person person : personerIGruppen) {
            List<Vergemaal> personerVergemaal = vergemaalRepository.findAllByIdentAndVergemaalSendt(person.getIdent(), null);
            if (!personerVergemaal.isEmpty()) {
                vergemaalIGruppen.addAll(personerVergemaal);
            }
        }
        skdMeldinger.addAll(skdMessageCreatorTrans1.createVergemaalSkdMelding(vergemaalIGruppen, addHeader));

        return skdMeldinger;
    }
}
