package no.nav.tps.forvalteren.service.command.dodsmeldinger;

import java.time.LocalDateTime;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.google.common.collect.Sets;

import no.nav.tps.forvalteren.domain.jpa.DeathRow;
import no.nav.tps.forvalteren.domain.jpa.Gateadresse;
import no.nav.tps.forvalteren.domain.jpa.Person;
import no.nav.tps.forvalteren.domain.service.tps.servicerutiner.definition.TpsSkdRequestMeldingDefinition;
import no.nav.tps.forvalteren.domain.service.tps.servicerutiner.definition.resolvers.skdmeldinger.SkdMeldingResolver;
import no.nav.tps.forvalteren.repository.jpa.DeathRowRepository;
import no.nav.tps.forvalteren.service.command.testdata.skd.SendSkdMeldingTilGitteMiljoer;
import no.nav.tps.forvalteren.service.command.testdata.skd.SkdMessageCreatorTrans1;

@Service
public class SendDodsmeldingTilTps {

    @Autowired
    private DeathRowRepository deathRowRepository;

    @Autowired
    private SkdMessageCreatorTrans1 skdCreator;

    @Autowired
    private SendSkdMeldingTilGitteMiljoer sendSkdMeldingTilMiljoe;

    @Autowired
    private SkdMeldingResolver innvandring;

    @Autowired
    private UpdateDeathRow updateDeathRow;

    public void execute() {

        TpsSkdRequestMeldingDefinition skdRequestMeldingDefinition = innvandring.resolve();
        List<DeathRow> deathRowTasks = deathRowRepository.findAllByStatus("Ikke sendt");

        for (DeathRow deathRow : deathRowTasks) {

            Person person = new Person();
            person.setId(deathRow.getId());
            person.setIdent(deathRow.getIdent());
            person.setRegdato(LocalDateTime.now());

            if ("U".equals(deathRow.getHandling()) || "D".equals(deathRow.getHandling())) {
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

                sendSkdMeldingTilMiljoe.execute(createDoedsmeldingerAnnullering(person), skdRequestMeldingDefinition, Sets.newHashSet(deathRow.getMiljoe()));
            }
            if ("U".equals(deathRow.getHandling()) || "C".equals(deathRow.getHandling())) {
                person.setDoedsdato(deathRow.getDoedsdato());

                sendSkdMeldingTilMiljoe.execute(createDoedsmeldinger(person), skdRequestMeldingDefinition, Sets.newHashSet(deathRow.getMiljoe()));
            }

            deathRow.setStatus("Sendt");
            updateDeathRow.execute(deathRow);
        }
    }

    private String createDoedsmeldinger(Person person) {

        return skdCreator.execute("Doedsmelding", person, true).toString();
    }

    private String createDoedsmeldingerAnnullering(Person person) {

        return skdCreator.execute("DoedsmeldingAnnullering", person, true).toString();
    }
}

