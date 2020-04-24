package no.nav.tps.forvalteren.service.command.testdata.skd;

import static no.nav.tps.forvalteren.domain.service.tps.servicerutiner.definition.resolvers.skdmeldinger.Familieendring.FAMILIEENDRING_MLD_NAVN;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import no.nav.tps.forvalteren.domain.jpa.Person;
import no.nav.tps.forvalteren.domain.jpa.Relasjon;

@Service
public class PersistBarnTransRecordsToTps {

    @Autowired
    private SkdMessageCreatorTrans2 skdMessageCreatorTrans2;

    private static final int MAX_BARN = 26;
    private static final int MAX_BARN_PER_RECORD = 13;

    public List<SkdMeldingTrans2> execute(Person forelder, boolean addHeader) {
        List<Person> barn = forelder.getRelasjoner().stream()
                .filter(Relasjon::isBarn)
                .map(Relasjon::getPersonRelasjonMed).collect(Collectors.toList());
        List<SkdMeldingTrans2> skdMeldinger = new ArrayList<>();
        if (barn.size() > MAX_BARN) {
            throw new IllegalArgumentException("Personen har for mange barn.");
        } else if (barn.size() <= MAX_BARN_PER_RECORD) {
            skdMeldinger.addAll(skdMessageCreatorTrans2.execute(FAMILIEENDRING_MLD_NAVN, forelder, barn, addHeader));
        } else {
            List<Person> barnRecord1 = barn.subList(0, 13);
            List<Person> barnRecord2 = barn.subList(13, barn.size());
            skdMeldinger.addAll(skdMessageCreatorTrans2.execute(FAMILIEENDRING_MLD_NAVN, forelder, barnRecord1, addHeader));
            skdMeldinger.addAll(skdMessageCreatorTrans2.execute(FAMILIEENDRING_MLD_NAVN, forelder, barnRecord2, addHeader));
        }
        return skdMeldinger;
    }
}
