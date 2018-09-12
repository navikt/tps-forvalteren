package no.nav.tps.forvalteren.service.command.dodsmeldinger;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.google.common.collect.Sets;

import lombok.AllArgsConstructor;
import lombok.Getter;
import no.nav.tps.forvalteren.domain.jpa.DeathRow;
import no.nav.tps.forvalteren.domain.jpa.Gateadresse;
import no.nav.tps.forvalteren.domain.jpa.Person;
import no.nav.tps.forvalteren.domain.service.tps.servicerutiner.definition.TpsSkdRequestMeldingDefinition;
import no.nav.tps.forvalteren.domain.service.tps.servicerutiner.definition.resolvers.skdmeldinger.SkdMeldingResolver;
import no.nav.tps.forvalteren.repository.jpa.DeathRowRepository;
import no.nav.tps.forvalteren.service.command.testdata.skd.SendSkdMeldingTilGitteMiljoer;
import no.nav.tps.forvalteren.service.command.testdata.skd.SkdMeldingTrans1;
import no.nav.tps.forvalteren.service.command.testdata.skd.SkdMessageCreatorTrans1;

@Service
public class SendDodsmeldingTilTps {

    @Autowired
    private DeathRowRepository deathRowRepository;

    @Autowired
    private SkdMessageCreatorTrans1 skdCreator;

    @Autowired
    private SendSkdMeldingTilGitteMiljoer sendSkdMeldingTilGitteMiljoer;

    @Autowired
    private SkdMeldingResolver meldingResolver;

    @Autowired
    private UpdateDeathRow updateDeathRow;

    public void execute() {

        List<DeathRow> deathRowTasks = deathRowRepository.findAllByStatus("Ikke sendt");

        List<Container> containere = new ArrayList<>();

        for (DeathRow melding : deathRowTasks) {

            Person person = new Person();
            person.setId(melding.getId());
            person.setIdent(melding.getIdent());
            person.setRegdato(LocalDateTime.now());

            if ("U".equals(melding.getHandling()) || "D".equals(melding.getHandling())) {
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

                containere.add(new Container(createDoedsmeldingerAnnullering(person), melding.getMiljoe()));
            }
            if ("U".equals(melding.getHandling()) || "C".equals(melding.getHandling())) {
                person.setDoedsdato(melding.getDoedsdato());
                person.setRegdato(melding.getDoedsdato());
                containere.add(new Container(createDoedsmeldinger(person), melding.getMiljoe()));
            }
            melding.setStatus("Sendt");
            updateDeathRow.execute(melding);
        }

        TpsSkdRequestMeldingDefinition skdRequestMeldingDefinition = meldingResolver.resolve();

        for (Container container : containere) {
            sendSkdMeldingTilGitteMiljoer.execute(container.getSkdMeldingTrans1().toString(), skdRequestMeldingDefinition, Sets.newHashSet(container.getEnvironment()));
        }
    }

    private SkdMeldingTrans1 createDoedsmeldinger(Person person) {

        return skdCreator.execute("Doedsmelding", person, true);
    }

    private SkdMeldingTrans1 createDoedsmeldingerAnnullering(Person person) {

        return skdCreator.execute("DoedsmeldingAnnullering", person, true);
    }

    @Getter
    @AllArgsConstructor
    private class Container {
        private SkdMeldingTrans1 skdMeldingTrans1;
        private String environment;
    }
}

