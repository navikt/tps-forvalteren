package no.nav.tps.forvalteren.service.command.dodsmeldinger;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import no.nav.tps.forvalteren.domain.jpa.DeathRow;
import no.nav.tps.forvalteren.domain.jpa.Person;
import no.nav.tps.forvalteren.domain.service.tps.servicerutiner.definition.TpsSkdRequestMeldingDefinition;
import no.nav.tps.forvalteren.domain.service.tps.servicerutiner.definition.resolvers.skdmeldinger.SkdMeldingResolver;
import no.nav.tps.forvalteren.service.command.testdata.FindDoedePersoner;
import no.nav.tps.forvalteren.service.command.testdata.FindPersonerWithoutDoedsmelding;
import no.nav.tps.forvalteren.service.command.testdata.skd.SkdMessageCreatorTrans1;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LagreDodsmeldingTilTps {

    @Autowired
    private FindAllDeathRowTasks findAllDeathRowTasks;

    @Autowired
    private FindAllDeathRows findAllDeathRows;

    @Autowired
    private SkdMessageCreatorTrans1 skdCreator;

    @Autowired
    FindPersonerWithoutDoedsmelding findDoedePersonerWithoutDoedsmelding;

    @Autowired
    FindDoedePersoner findDoedePersoner;

    @Autowired
    private SkdMeldingResolver innvandring;

    public void execute() {

        List<List<DeathRow>> deathRowTasks = findAllDeathRowTasks.execute();
        System.out.println(deathRowTasks);



    }


    private List<String> createDoedsmeldinger(List<Person> deathRowPersonList){

        List<Person> doedePersonerWithoutDoedsmelding = findDoedePersonerWithoutDoedsmelding(deathRowPersonList);
        List<String> skdDodsmelding = new ArrayList<>();
        if(!doedePersonerWithoutDoedsmelding.isEmpty()){
            skdDodsmelding.addAll(skdCreator.execute("Doedsmelding", doedePersonerWithoutDoedsmelding, true));
        }

        return skdDodsmelding;
    }
    private List<Person> findDoedePersonerWithoutDoedsmelding(List<Person> personer) {
        List<Person> doedePersoner = findDoedePersoner.execute(personer);
        List<Person> doedePersonerWithoutDoedsmelding = findDoedePersonerWithoutDoedsmelding.execute(doedePersoner);
        return doedePersonerWithoutDoedsmelding;
    }


    private void sendTilTps(){

    }
}
