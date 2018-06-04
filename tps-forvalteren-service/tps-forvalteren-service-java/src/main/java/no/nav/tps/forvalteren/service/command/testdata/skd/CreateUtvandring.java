package no.nav.tps.forvalteren.service.command.testdata.skd;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import no.nav.tps.forvalteren.domain.jpa.Person;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CreateUtvandring {
    private static final String NAVN_UTVANDRING = "Utvandring";

    @Autowired
    private SkdMessageCreatorTrans1 skdMessageCreatorTrans1;

    public List<SkdMeldingTrans1> execute(List<Person> personerSomAlleredeEksistererITps, boolean addHeader) {
        List<SkdMeldingTrans1> skdMeldinger = new ArrayList<>();

        List<Person> personerSomSkalUtvandre = personerSomAlleredeEksistererITps.stream().filter(person -> person.getUtvandretTilLand()!=null).collect(Collectors.toList());
        if (!personerSomSkalUtvandre.isEmpty()) {
            skdMeldinger.addAll(skdMessageCreatorTrans1.execute(NAVN_UTVANDRING, personerSomSkalUtvandre, addHeader));
        }
        return skdMeldinger;
    }
}
