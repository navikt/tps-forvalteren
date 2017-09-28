package no.nav.tps.forvalteren.service.command.testdata.skd;

import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import no.nav.tps.forvalteren.domain.jpa.Gruppe;
import no.nav.tps.forvalteren.domain.jpa.Person;
import no.nav.tps.forvalteren.service.command.testdata.FindGruppeById;
import no.nav.tps.forvalteren.service.command.testdata.SaveDoedsmelding;
import no.nav.tps.forvalteren.service.command.testdata.SjekkDoedsmeldingSentForPersonId;

@Service
public class SendDoedsmelding {

    private static final String NAVN_DOEDSMELDING = "Doedsmelding";

    @Autowired
    private SkdCreatePersoner skdCreatePersoner;

    @Autowired
    private FindGruppeById findGruppeById;

    @Autowired
    private SjekkDoedsmeldingSentForPersonId sjekkDoedsmeldingSentForPersonId;

    @Autowired
    private SaveDoedsmelding saveDoedsmelding;

    public void execute(Long gruppeId, List<String> environments) {
        Gruppe gruppe = findGruppeById.execute(gruppeId);
        List<Person> personerIGruppen = gruppe.getPersoner();
        List<Person> doedePersonerUtenDoedsmelding = findDoedePersonerUtenDoedsmelding(personerIGruppen);

        if (!doedePersonerUtenDoedsmelding.isEmpty()) {
            skdCreatePersoner.execute(NAVN_DOEDSMELDING, doedePersonerUtenDoedsmelding, environments);
            saveDoedsmelding.execute(doedePersonerUtenDoedsmelding);
        }
    }

    private List<Person> findDoedePersonerUtenDoedsmelding(List<Person> personer) {
        List<Person> doedePersoner = findDoedePersoner(personer);
        List<Person> doedePersonerUtenDoedsmelding = filtrerPaaPersonerMedDoedsmelding(doedePersoner);
        return doedePersonerUtenDoedsmelding;
    }

    private List<Person> findDoedePersoner(List<Person> personer) {
        List<Person> doedePersoner = new ArrayList<>();
        for (Person person : personer) {
            // What if death date is in the future?
            if (person.getDoedsdato() != null) {
                doedePersoner.add(person);
            }
        }
        return doedePersoner;
    }

    private List<Person> filtrerPaaPersonerMedDoedsmelding(List<Person> doedePersoner) {
        List<Person> doedePersonerUtenDoedsmelding = new ArrayList<>();
        for (Person person : doedePersoner) {
            if (!sjekkDoedsmeldingSentForPersonId.execute(person.getId())) {
                doedePersonerUtenDoedsmelding.add(person);
            }
        }
        return doedePersonerUtenDoedsmelding;
    }
}
