package no.nav.tps.forvalteren.service.command.testdata.skd;

import static java.util.Objects.nonNull;
import static no.nav.tps.forvalteren.domain.service.tps.servicerutiner.definition.resolvers.skdmeldinger.UtvandringAarsakskode32.UTVANDRING_MLD_NAVN;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import no.nav.tps.forvalteren.domain.jpa.Person;

@Service
public class CreateUtvandring {

    @Autowired
    private SkdMessageCreatorTrans1 skdMessageCreatorTrans1;

    public List<SkdMeldingTrans1> execute(List<Person> personerSomAlleredeEksistererITps, boolean addHeader) {
        List<SkdMeldingTrans1> skdMeldinger = new ArrayList<>();

        List<Person> personerSomSkalUtvandre = personerSomAlleredeEksistererITps.stream().filter(person -> nonNull(person.getUtvandretTilLand())).collect(Collectors.toList());
        if (!personerSomSkalUtvandre.isEmpty()) {
            skdMeldinger.addAll(skdMessageCreatorTrans1.execute(UTVANDRING_MLD_NAVN, personerSomSkalUtvandre, addHeader));
        }
        return skdMeldinger;
    }
}
