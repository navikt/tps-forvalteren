package no.nav.tps.forvalteren.service.command.testdata.skd;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import no.nav.tps.forvalteren.domain.jpa.Gruppe;
import no.nav.tps.forvalteren.domain.jpa.Person;
import no.nav.tps.forvalteren.service.command.testdata.FindDoedePersoner;
import no.nav.tps.forvalteren.service.command.testdata.FindGruppeById;
import no.nav.tps.forvalteren.service.command.testdata.FindPersonerWithoutDoedsmelding;
import no.nav.tps.forvalteren.service.command.testdata.SaveDoedsmeldingToDB;

@Service
public class CreateDoedsmeldinger {

    private static final String NAVN_DOEDSMELDING = "Doedsmelding";

    @Autowired
    private SkdMessageSenderTrans1 skdMessageSenderTrans1;

    @Autowired
    private FindGruppeById findGruppeById;

    @Autowired
    private FindDoedePersoner findDoedePersoner;

    @Autowired
    private FindPersonerWithoutDoedsmelding findPersonerWithoutDoedsmelding;

    @Autowired
    private SaveDoedsmeldingToDB saveDoedsmeldingToDB;

    public void execute(Long gruppeId, List<String> environments) {
        Gruppe gruppe = findGruppeById.execute(gruppeId);
        List<Person> personerIGruppen = gruppe.getPersoner();
        List<Person> doedePersonerWithoutDoedsmelding = findDoedePersonerWithoutDoedsmelding(personerIGruppen);

        if (!doedePersonerWithoutDoedsmelding.isEmpty()) {
            skdMessageSenderTrans1.execute(NAVN_DOEDSMELDING, doedePersonerWithoutDoedsmelding, environments);
            saveDoedsmeldingToDB.execute(doedePersonerWithoutDoedsmelding);
        }
    }

    private List<Person> findDoedePersonerWithoutDoedsmelding(List<Person> personer) {
        List<Person> doedePersoner = findDoedePersoner.execute(personer);
        List<Person> doedePersonerWithoutDoedsmelding = findPersonerWithoutDoedsmelding.execute(doedePersoner);
        return doedePersonerWithoutDoedsmelding;
    }
}
