package no.nav.tps.forvalteren.service.command.testdata.skd;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import no.nav.tps.forvalteren.domain.jpa.Person;
import no.nav.tps.forvalteren.service.command.testdata.FinnBarnTilForeldre;

@Service
public class DivideBarnIntoTransRecords {

    @Autowired
    private SkdMessageSenderTrans2 skdMessageSenderTrans2;

    @Autowired
    private FinnBarnTilForeldre finnBarnTilForeldre;

    public void execute(Person foreldre,  List<String> environments) {
        List<Person> barn = finnBarnTilForeldre.execute(foreldre);
        if (barn.size() > 26) {
            throw new IllegalArgumentException("Personen har for mange barn.");
        } else if (barn.size() <= 13) {
            skdMessageSenderTrans2.execute("Familieendring", foreldre, barn, environments);
        } else {
            List<Person> barnRecord1 = barn.subList(0, 13);
            List<Person> barnRecord2 = barn.subList(13, barn.size());
            skdMessageSenderTrans2.execute("Familieendring", foreldre, barnRecord1, environments);
            skdMessageSenderTrans2.execute("Familieendring", foreldre, barnRecord2, environments);
        }
    }
}
