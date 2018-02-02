package no.nav.tps.forvalteren.service.command.dodsmeldinger;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import no.nav.tps.forvalteren.domain.jpa.DeathRow;
import no.nav.tps.forvalteren.domain.jpa.Person;
import no.nav.tps.forvalteren.domain.service.tps.servicerutiner.definition.TpsSkdRequestMeldingDefinition;
import no.nav.tps.forvalteren.domain.service.tps.servicerutiner.definition.resolvers.skdmeldinger.SkdMeldingResolver;
import no.nav.tps.forvalteren.service.command.testdata.FindDoedePersoner;
import no.nav.tps.forvalteren.service.command.testdata.FindPersonerWithoutDoedsmelding;
import no.nav.tps.forvalteren.service.command.testdata.SaveDoedsmeldingToDB;
import no.nav.tps.forvalteren.service.command.testdata.skd.SendSkdMeldingTilGitteMiljoer;
import no.nav.tps.forvalteren.service.command.testdata.skd.SkdMessageCreatorTrans1;
import no.nav.tps.forvalteren.service.command.tps.SkdStartAjourhold;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LagreDodsmeldingTilTps {

    @Autowired
    private FindAllDeathRowTasks findAllDeathRowTasks;

    @Autowired
    private SkdMessageCreatorTrans1 skdCreator;

    @Autowired
    private SendSkdMeldingTilGitteMiljoer sendSkdMeldingTilGitteMiljoer;

    @Autowired
    private FindPersonerWithoutDoedsmelding findDoedePersonerWithoutDoedsmelding;

    @Autowired
    private FindDoedePersoner findDoedePersoner;

    @Autowired
    private SkdMeldingResolver innvandring;

    @Autowired
    private SkdStartAjourhold skdStartAjourhold;

    @Autowired
    private SaveDoedsmeldingToDB saveDoedsmeldingToDB;

    public void execute() {

        List<List<DeathRow>> deathRowTasks = findAllDeathRowTasks.execute();

        List<Person> deleteDeathRowPersonList = new ArrayList<>();
        List<Person> createDeathRowPersonList = new ArrayList<>();

        for(List<DeathRow> list : deathRowTasks){
            for(DeathRow melding : list){

                Person person = new Person();
                person.setId(melding.getId());
                person.setIdent(melding.getIdent());
                person.setDoedsdato(LocalDateTime.of(melding.getDoedsdato(), LocalTime.now()));
                person.setRegdato(LocalDateTime.of(melding.getDoedsdato(), LocalTime.now()));

                if(melding.getHandling().equals("D")){
                    deleteDeathRowPersonList.add(person);
                } else if( melding.getHandling().equals("C")) {
                    createDeathRowPersonList.add(person);
                } else {
                    continue;
                }

            }
        }
        System.out.println("DeleteDeathRowPersonList: " + deleteDeathRowPersonList);
        System.out.println("CreateDeathRowPersonList: " + createDeathRowPersonList);

        List<String> skdMeldinger = createDoedsmeldinger(createDeathRowPersonList);

        TpsSkdRequestMeldingDefinition skdRequestMeldingDefinition = innvandring.resolve();
        List<String> envi = new ArrayList<String>();
        for( String skdmelding : skdMeldinger) {

            envi.add("U5");

            Set<String> env = new HashSet<>();
            env.add("U5");
            sendSkdMeldingTilGitteMiljoer.execute(skdmelding, skdRequestMeldingDefinition, env);
        }
        skdStartAjourhold.execute(new HashSet<>(envi));
    }


    private List<String> createDoedsmeldinger(List<Person> deathRowPersonList){

        List<Person> doedePersonerWithoutDoedsmelding = findDoedePersonerWithoutDoedsmelding(deathRowPersonList);
        List<String> skdDodsmelding = new ArrayList<>();
        if(!doedePersonerWithoutDoedsmelding.isEmpty()){
            skdDodsmelding.addAll(skdCreator.execute("Doedsmelding", doedePersonerWithoutDoedsmelding, true));
            //saveDoedsmeldingToDB.execute(doedePersonerWithoutDoedsmelding);
        }

        return skdDodsmelding;
    }
    private List<Person> findDoedePersonerWithoutDoedsmelding(List<Person> personer) {
        List<Person> doedePersoner = findDoedePersoner.execute(personer);
        List<Person> doedePersonerWithoutDoedsmelding = findDoedePersonerWithoutDoedsmelding.execute(doedePersoner);
        return doedePersonerWithoutDoedsmelding;
    }

}
