package no.nav.tps.forvalteren.service.command.dodsmeldinger;

import static no.nav.tps.forvalteren.domain.service.DiskresjonskoderType.UFB;

import java.time.LocalDateTime;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.google.common.collect.Sets;

import no.nav.tps.forvalteren.domain.jpa.Adresse;
import no.nav.tps.forvalteren.domain.jpa.DeathRow;
import no.nav.tps.forvalteren.domain.jpa.Person;
import no.nav.tps.forvalteren.domain.service.tps.servicerutiner.definition.TpsSkdRequestMeldingDefinition;
import no.nav.tps.forvalteren.domain.service.tps.servicerutiner.definition.resolvers.skdmeldinger.SkdMeldingResolver;
import no.nav.tps.forvalteren.repository.jpa.DeathRowRepository;
import no.nav.tps.forvalteren.service.command.testdata.skd.SendSkdMeldingTilGitteMiljoer;
import no.nav.tps.forvalteren.service.command.testdata.skd.SkdMessageCreatorTrans1;
import no.nav.tps.forvalteren.service.command.tps.servicerutiner.AdresseService;

@Service
public class SendDodsmeldingTilTps {

    private enum Action {C, U, D}

    @Autowired
    private DeathRowRepository deathRowRepository;

    @Autowired
    private SkdMessageCreatorTrans1 skdCreator;

    @Autowired
    private SendSkdMeldingTilGitteMiljoer sendSkdMeldingTilMiljoe;

    @Autowired
    private SkdMeldingResolver innvandring;

    @Autowired
    private AdresseService adresseService;

    public void execute() {

        TpsSkdRequestMeldingDefinition skdRequestMeldingDefinition = innvandring.resolve();
        List<DeathRow> deathRowTasks = deathRowRepository.findAllByStatus("Ikke sendt");

        for (DeathRow deathRow : deathRowTasks) {

            Person person = new Person();
            person.setId(deathRow.getId());
            person.setIdent(deathRow.getIdent());
            person.setRegdato(LocalDateTime.now());

            if (Action.U.name().equals(deathRow.getHandling()) || Action.D.name().equals(deathRow.getHandling())) {

                Adresse adresse = adresseService.hentAdresseFoerDoed(deathRow.getIdent(), deathRow.getMiljoe());
                if (adresse != null) {
                    adresse.setId(person.getId());
                    adresse.setPerson(person);
                    person.setBoadresse(adresse);
                } else {
                    person.setSpesreg(Integer.toString(UFB.ordinal()));
                }

                sendSkdMeldingTilMiljoe.execute(createDoedsmeldingerAnnullering(person), skdRequestMeldingDefinition, Sets.newHashSet(deathRow.getMiljoe()));
            }
            if (Action.U.name().equals(deathRow.getHandling()) || Action.C.name().equals(deathRow.getHandling())) {
                person.setDoedsdato(deathRow.getDoedsdato());

                sendSkdMeldingTilMiljoe.execute(createDoedsmeldinger(person), skdRequestMeldingDefinition, Sets.newHashSet(deathRow.getMiljoe()));
            }

            deathRow.setStatus("Sendt");
            deathRowRepository.save(deathRow);
        }
    }

    private String createDoedsmeldinger(Person person) {

        return skdCreator.execute("Doedsmelding", person, true).toString();
    }

    private String createDoedsmeldingerAnnullering(Person person) {

        return skdCreator.execute("DoedsmeldingAnnullering", person, true).toString();
    }
}