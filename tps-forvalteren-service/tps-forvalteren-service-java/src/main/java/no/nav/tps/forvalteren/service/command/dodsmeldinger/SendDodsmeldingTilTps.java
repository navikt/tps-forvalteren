package no.nav.tps.forvalteren.service.command.dodsmeldinger;

import no.nav.tps.forvalteren.domain.jpa.DeathRow;
import no.nav.tps.forvalteren.domain.jpa.Gateadresse;
import no.nav.tps.forvalteren.domain.jpa.Person;
import no.nav.tps.forvalteren.domain.service.tps.servicerutiner.definition.TpsSkdRequestMeldingDefinition;
import no.nav.tps.forvalteren.domain.service.tps.servicerutiner.definition.resolvers.skdmeldinger.SkdMeldingResolver;
import no.nav.tps.forvalteren.repository.jpa.DeathRowRepository;
import no.nav.tps.forvalteren.service.command.testdata.FindDoedePersoner;
import no.nav.tps.forvalteren.service.command.testdata.skd.SendSkdMeldingTilGitteMiljoer;
import no.nav.tps.forvalteren.service.command.testdata.skd.SkdMeldingTrans1;
import no.nav.tps.forvalteren.service.command.testdata.skd.SkdMessageCreatorTrans1;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class SendDodsmeldingTilTps {
    
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
    private UpdateDeathRow updateDeathRow;

    public void execute() {

        List<List<DeathRow>> deathRowTasks = findAllDeathRowTasks.execute();

        List<Person> deleteDeathRowPersonList = new ArrayList<>();
        List<Person> createDeathRowPersonList = new ArrayList<>();

        for (List<DeathRow> list : deathRowTasks) {
            for (DeathRow melding : list) {

                Person person = new Person();
                person.setId(melding.getId());
                person.setIdent(melding.getIdent());
                person.setRegdato(LocalDateTime.now());

                if ("C".equals(melding.getHandling())) {
                    person.setDoedsdato(melding.getDoedsdato());
                    person.setRegdato(melding.getDoedsdato());
                    createDeathRowPersonList.add(person);

                } else if ("D".equals(melding.getHandling())) {
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
                } else {
                    continue;
                }
                melding.setStatus("Sendt");
                updateDeathRow.execute(melding);
            }
        }

        List<SkdMeldingTrans1> skdMeldinger = new ArrayList<>();
        skdMeldinger.addAll(createDoedsmeldingerAnnullering(deleteDeathRowPersonList));
        skdMeldinger.addAll(createDoedsmeldinger(createDeathRowPersonList));

        TpsSkdRequestMeldingDefinition skdRequestMeldingDefinition = innvandring.resolve();
        Set<String> environment;

        for (SkdMeldingTrans1 skdmelding : skdMeldinger) {
            environment = new HashSet<>(Arrays.asList(getEnvironmentFromSkdDoedsmelding(skdmelding)));
            sendSkdMeldingTilGitteMiljoer.execute(skdmelding.toString(), skdRequestMeldingDefinition, environment);
            environment.clear();
        }
    }

    private List<SkdMeldingTrans1> createDoedsmeldinger(List<Person> deathRowPersonList) {

        List<Person> doedePersonerWithoutDoedsmelding = findDoedePersoner.execute(deathRowPersonList);
        List<SkdMeldingTrans1> skdDodsmelding = new ArrayList<>();

        if (!doedePersonerWithoutDoedsmelding.isEmpty()) {
            skdDodsmelding.addAll(skdCreator.execute("Doedsmelding", doedePersonerWithoutDoedsmelding, true));
        }
        return skdDodsmelding;
    }

    private List<SkdMeldingTrans1> createDoedsmeldingerAnnullering(List<Person> deleteDeathRowPersonList) {

        List<SkdMeldingTrans1> skdDodsmeldingAnnulering = new ArrayList<>();

        if (!deleteDeathRowPersonList.isEmpty()) {
            skdDodsmeldingAnnulering.addAll(skdCreator.execute("DoedsmeldingAnnullering", deleteDeathRowPersonList, true));
        }

        return skdDodsmeldingAnnulering;
    }

    private String getEnvironmentFromSkdDoedsmelding(SkdMeldingTrans1 skdmelding) {

        return deathRowRepository.findByIdent(skdmelding.getFodselsnummer()).getMiljoe();
    }
}

