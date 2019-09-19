package no.nav.tps.forvalteren.service.command.testdata.skd;

import static java.util.Comparator.comparing;
import static java.util.Comparator.reverseOrder;
import static no.nav.tps.forvalteren.domain.service.tps.servicerutiner.definition.resolvers.skdmeldinger.MeldingOmDubletter.MELDING_OM_DUBLETTER;

import java.util.ArrayList;
import java.util.List;
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

        personerSomAlleredeEksistererITps.forEach(person -> {

                    if (!person.getIdentHistorikk().isEmpty()) {
                        person.getIdentHistorikk().sort(comparing(IdentHistorikk::getHistoricIdentOrder, reverseOrder()));

                        for (int i = 0; i < person.getIdentHistorikk().size(); i++) {

                            if (1 == person.getIdentHistorikk().get(i).getHistoricIdentOrder()) {
                                person.getIdentHistorikk().get(i).getAliasPerson().setReplacedByIdent(person.getIdent());

                            } else {
                                person.getIdentHistorikk().get(i).getAliasPerson().setReplacedByIdent(
                                        person.getIdentHistorikk().get(i + 1).getAliasPerson().getIdent());
                            }

                            personerSomErDubletter.add(person.getIdentHistorikk().get(i).getAliasPerson());
                        }
                    }
                }
        );

        if (!personerSomErDubletter.isEmpty()) {
            skdMeldinger.addAll(skdMessageCreatorTrans1.execute(MELDING_OM_DUBLETTER, personerSomErDubletter, addHeader));
        }
        return skdMeldinger;
    }
}
