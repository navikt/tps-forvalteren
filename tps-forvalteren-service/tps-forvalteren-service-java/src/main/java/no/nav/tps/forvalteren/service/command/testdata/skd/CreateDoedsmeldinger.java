package no.nav.tps.forvalteren.service.command.testdata.skd;

import static no.nav.tps.forvalteren.domain.service.tps.servicerutiner.definition.resolvers.skdmeldinger.DoedsmeldingAarsakskode43.DOEDSMELDING_MLD_NAVN;

import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import no.nav.tps.forvalteren.domain.jpa.Person;
import no.nav.tps.forvalteren.service.command.testdata.FindDoedePersoner;
import no.nav.tps.forvalteren.service.command.testdata.FindGruppeById;
import no.nav.tps.forvalteren.service.command.testdata.FindPersonerWithoutDoedsmelding;
import no.nav.tps.forvalteren.service.command.testdata.SaveDoedsmeldingToDB;

@Service
public class CreateDoedsmeldinger {

    @Autowired
    private SkdMessageCreatorTrans1 skdMessageCreatorTrans1;

    @Autowired
    private FindGruppeById findGruppeById;

    @Autowired
    private FindDoedePersoner findDoedePersoner;

    @Autowired
    private FindPersonerWithoutDoedsmelding findPersonerWithoutDoedsmelding;

    @Autowired
    private SaveDoedsmeldingToDB saveDoedsmeldingToDB;

    public List<SkdMeldingTrans1> execute(List<Person> personerIGruppen, boolean addHeader) {
        List<Person> doedePersonerWithoutDoedsmelding = findDoedePersonerWithoutDoedsmelding(personerIGruppen);
        List<SkdMeldingTrans1> skdMeldinger = new ArrayList<>();
        if (!doedePersonerWithoutDoedsmelding.isEmpty()) {
            skdMeldinger.addAll(skdMessageCreatorTrans1.execute(DOEDSMELDING_MLD_NAVN, doedePersonerWithoutDoedsmelding, addHeader));
            saveDoedsmeldingToDB.execute(doedePersonerWithoutDoedsmelding);
        }
        return skdMeldinger;
    }

    private List<Person> findDoedePersonerWithoutDoedsmelding(List<Person> personer) {

        List<Person> doedePersoner = findDoedePersoner.execute(personer);
        return findPersonerWithoutDoedsmelding.execute(doedePersoner);
    }
}
