package no.nav.tps.forvalteren.service.command.testdata.skd;

import static java.util.Objects.nonNull;
import static no.nav.tps.forvalteren.domain.service.tps.servicerutiner.definition.resolvers.skdmeldinger.MeldingOmForsvunnetAarsakskode82.MELDING_OM_FORSVUNNET;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import no.nav.tps.forvalteren.domain.jpa.Person;

@Service
public class CreateMeldingerOmForsvunnet {

    @Autowired
    private SkdMessageCreatorTrans1 skdMessageCreatorTrans1;

    public List<SkdMeldingTrans1> filterForsvunnet(List<Person> personerSomAlleredeEksistererITps, boolean addHeader) {
        List<SkdMeldingTrans1> skdMeldinger = new ArrayList<>();

        List<Person> personerSomHarForsvunnet = personerSomAlleredeEksistererITps.stream()
                .filter(person -> nonNull(person.getForsvunnetDato())).collect(Collectors.toList());

        personerSomHarForsvunnet.forEach(person -> {
            person.setBoadresse(null);
            person.getPostadresse().clear();
        });

        if (!personerSomHarForsvunnet.isEmpty()) {
            skdMeldinger.addAll(skdMessageCreatorTrans1.execute(MELDING_OM_FORSVUNNET, personerSomHarForsvunnet, addHeader));
        }
        return skdMeldinger;
    }
}
