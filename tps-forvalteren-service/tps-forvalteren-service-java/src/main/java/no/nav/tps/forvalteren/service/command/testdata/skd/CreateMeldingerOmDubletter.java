package no.nav.tps.forvalteren.service.command.testdata.skd;

import static no.nav.tps.forvalteren.domain.service.tps.servicerutiner.definition.resolvers.skdmeldinger.MeldingOmDubletter.MELDING_OM_DUBLETTER;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import no.nav.tps.forvalteren.domain.jpa.IdentHistorikk;
import no.nav.tps.forvalteren.domain.jpa.Person;

@Service
public class CreateMeldingerOmDubletter {

    @Autowired
    private SkdMessageCreatorTrans1 skdMessageCreatorTrans1;

    public List<SkdMeldingTrans1> filterDubletter(List<Person> personerSomAlleredeEksistererITps, boolean addHeader) {
        List<SkdMeldingTrans1> skdMeldinger = new ArrayList<>();

        List<Person> personerSomErDubletter = new ArrayList();
        personerSomAlleredeEksistererITps.forEach(person ->
                personerSomErDubletter.addAll(person.getIdentHistorikk().stream().map(IdentHistorikk::getAliasPerson).collect(Collectors.toList()))
        );

        if (!personerSomErDubletter.isEmpty()) {
            skdMeldinger.addAll(skdMessageCreatorTrans1.execute(MELDING_OM_DUBLETTER, personerSomErDubletter, addHeader));
        }
        return skdMeldinger;
    }
}
