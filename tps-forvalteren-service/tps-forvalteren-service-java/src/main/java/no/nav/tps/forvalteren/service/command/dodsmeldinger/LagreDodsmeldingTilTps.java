package no.nav.tps.forvalteren.service.command.dodsmeldinger;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import no.nav.tps.forvalteren.domain.jpa.DeathRow;
import no.nav.tps.forvalteren.domain.jpa.Gateadresse;
import no.nav.tps.forvalteren.domain.jpa.Person;
import no.nav.tps.forvalteren.domain.service.tps.servicerutiner.definition.TpsSkdRequestMeldingDefinition;
import no.nav.tps.forvalteren.domain.service.tps.servicerutiner.definition.resolvers.skdmeldinger.SkdMeldingResolver;
import no.nav.tps.forvalteren.repository.jpa.DeathRowRepository;
import no.nav.tps.forvalteren.service.command.FilterEnvironmentsOnDeployedEnvironment;
import no.nav.tps.forvalteren.service.command.testdata.FindDoedePersoner;
import no.nav.tps.forvalteren.service.command.testdata.FindPersonerWithoutDoedsmelding;
import no.nav.tps.forvalteren.service.command.testdata.SaveDoedsmeldingToDB;
import no.nav.tps.forvalteren.service.command.testdata.SjekkDoedsmeldingSentForPerson;
import no.nav.tps.forvalteren.service.command.testdata.skd.SendSkdMeldingTilGitteMiljoer;
import no.nav.tps.forvalteren.service.command.testdata.skd.SkdMessageCreatorTrans1;
import no.nav.tps.forvalteren.service.command.tps.SkdStartAjourhold;
import no.nav.tps.forvalteren.service.command.tpsconfig.GetEnvironments;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LagreDodsmeldingTilTps {

    @Autowired
    private FilterEnvironmentsOnDeployedEnvironment filterEnvironmentsOnDeployedEnvironment;

    @Autowired
    private GetEnvironments getEnvironments;

    @Autowired
    private DeathRowRepository deathRowRepository;

    @Autowired
    private FindAllDeathRowTasks findAllDeathRowTasks;

    @Autowired
    private SkdMessageCreatorTrans1 skdCreator;

    @Autowired
    private SendSkdMeldingTilGitteMiljoer sendSkdMeldingTilGitteMiljoer;

    @Autowired
    private FindDoedePersoner findDoedePersoner;

    @Autowired
    private SkdMeldingResolver innvandring;

    @Autowired
    private SkdStartAjourhold skdStartAjourhold;

    @Autowired
    private UpdateDeathRow updateDeathRow;

    public void execute() {

        List<List<DeathRow>> deathRowTasks = findAllDeathRowTasks.execute();

        // Liste med annuleringer av dødsmelding
        List<Person> deleteDeathRowPersonList = new ArrayList<>();
        // Liste med oppretting av dødsmelding
        List<Person> createDeathRowPersonList = new ArrayList<>();

        for (List<DeathRow> list : deathRowTasks) {
            for (DeathRow melding : list) {

                Person person = new Person();
                person.setId(melding.getId());
                person.setIdent(melding.getIdent());
                person.setRegdato(LocalDateTime.now());

                if (melding.getHandling().equals("C")) {
                    person.setDoedsdato(LocalDateTime.of(melding.getDoedsdato(), LocalTime.now()));
                    person.setRegdato(LocalDateTime.of(melding.getDoedsdato(), LocalTime.now()));

                }

                if (melding.getHandling().equals("D")) {
                    /* Setter en dummy adresse*/
                    Gateadresse adr = new Gateadresse();
                    adr.setAdresse("SANNERGATA");
                    adr.setHusnummer("2");
                    adr.setGatekode("12345");
                    adr.setId(person.getId());
                    adr.setPerson(person);
                    adr.setFlyttedato(LocalDateTime.now());
                    adr.setPostnr("1069");
                    adr.setKommunenr("1111");
                    person.setBoadresse(adr);

                    deleteDeathRowPersonList.add(person);
                } else if (melding.getHandling().equals("C")) {
                    createDeathRowPersonList.add(person);
                } else {
                    continue;
                }
                melding.setStatus("Sendt");
                updateDeathRow.execute(melding);
            }
        }

        List<String> skdMeldinger = new ArrayList<>();
        skdMeldinger.addAll(createDoedsmeldingerAnnullering(deleteDeathRowPersonList));
        skdMeldinger.addAll(createDoedsmeldinger(createDeathRowPersonList));

        TpsSkdRequestMeldingDefinition skdRequestMeldingDefinition = innvandring.resolve();
        Set<String> environments = filterEnvironmentsOnDeployedEnvironment.execute(getEnvironments.getEnvironmentsFromFasit("tpsws"));
        Set<String> environment;

        for (String skdmelding : skdMeldinger) {
            environment = new HashSet<>(Arrays.asList(getEnvironmentFromSkdDoedsmelding(skdmelding)));
            sendSkdMeldingTilGitteMiljoer.execute(skdmelding, skdRequestMeldingDefinition, environment);
            environment.clear();
        }

        skdStartAjourhold.execute(new HashSet<>(environments));
        //deathRowRepository.save();

    }

    private List<String> createDoedsmeldinger(List<Person> deathRowPersonList) {

        List<Person> doedePersonerWithoutDoedsmelding = findDoedePersoner.execute(deathRowPersonList);
        List<String> skdDodsmelding = new ArrayList<>();

        if (!doedePersonerWithoutDoedsmelding.isEmpty()) {
            skdDodsmelding.addAll(skdCreator.execute("Doedsmelding", doedePersonerWithoutDoedsmelding, true));
        }
        return skdDodsmelding;
    }

    private List<String> createDoedsmeldingerAnnullering(List<Person> deleteDeathRowPersonList) {

        List<String> skdDodsmeldingAnnulering = new ArrayList<>();

        if (!deleteDeathRowPersonList.isEmpty()) {
            skdDodsmeldingAnnulering.addAll(skdCreator.execute("DoedsmeldingAnnullering", deleteDeathRowPersonList, true));
        }

        return skdDodsmeldingAnnulering;
    }

    private String getEnvironmentFromSkdDoedsmelding(String skdmelding) {

        return deathRowRepository.findByIdent(skdmelding.substring(46, 57)).getMiljoe();
    }

}

