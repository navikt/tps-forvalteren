package no.nav.tps.forvalteren.service.command.testdata.skd;

import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import no.nav.tps.forvalteren.domain.jpa.Person;
import no.nav.tps.forvalteren.service.command.testdata.FinnBarnTilForelder;

@Service
public class PersistBarnTransRecordsToTps {

    @Autowired
    private SkdMessageCreatorTrans2 skdMessageCreatorTrans2;

    @Autowired
    private FinnBarnTilForelder finnBarnTilForelder;

    private static final int MAX_BARN = 26;
    private static final int MAX_BARN_PER_RECORD = 13;
    private static final String SKD_MELDING_NAVN = "Familieendring";

    public List<String> execute(Person forelder, boolean addHeader) {
        List<Person> barn = finnBarnTilForelder.execute(forelder);
        List<String> skdMeldinger = new ArrayList<>();
        if (barn.size() > MAX_BARN) {
            throw new IllegalArgumentException("Personen har for mange barn.");
        } else if (barn.size() <= MAX_BARN_PER_RECORD) {
            skdMeldinger.addAll(skdMessageCreatorTrans2.execute(SKD_MELDING_NAVN, forelder, barn, addHeader));
        } else {
            List<Person> barnRecord1 = barn.subList(0, 13);
            List<Person> barnRecord2 = barn.subList(13, barn.size());
            skdMeldinger.addAll(skdMessageCreatorTrans2.execute(SKD_MELDING_NAVN, forelder, barnRecord1, addHeader));
            skdMeldinger.addAll(skdMessageCreatorTrans2.execute(SKD_MELDING_NAVN, forelder, barnRecord2, addHeader));
        }
        return skdMeldinger;
    }
}
