package no.nav.tps.forvalteren.service.command.testdata.skd;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import no.nav.tps.forvalteren.domain.jpa.Person;
import no.nav.tps.forvalteren.service.command.testdata.FinnBarnTilForelder;

@Service
public class DivideBarnIntoTransRecords {

    @Autowired
    private SkdMessageSenderTrans2 skdMessageSenderTrans2;

    @Autowired
    private FinnBarnTilForelder finnBarnTilForelder;

    public static final int MAX_BARN = 26;
    public static final int MAX_BARN_PER_RECORD = 13;

    public void execute(Person forelder,  List<String> environments) {
        List<Person> barn = finnBarnTilForelder.execute(forelder);
        if (barn.size() > MAX_BARN) {
            throw new IllegalArgumentException("Personen har for mange barn.");
        } else if (barn.size() <= MAX_BARN_PER_RECORD) {
            skdMessageSenderTrans2.execute("Familieendring", forelder, barn, environments);
        } else {
            List<Person> barnRecord1 = barn.subList(0, 13);
            List<Person> barnRecord2 = barn.subList(13, barn.size());
            skdMessageSenderTrans2.execute("Familieendring", forelder, barnRecord1, environments);
            skdMessageSenderTrans2.execute("Familieendring", forelder, barnRecord2, environments);
        }
    }
}
