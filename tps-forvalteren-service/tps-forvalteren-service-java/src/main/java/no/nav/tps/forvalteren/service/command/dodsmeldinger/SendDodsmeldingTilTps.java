package no.nav.tps.forvalteren.service.command.dodsmeldinger;

import static no.nav.tps.forvalteren.domain.service.DiskresjonskoderType.UFB;

import java.time.LocalDateTime;
import java.util.List;
import org.codehaus.plexus.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.google.common.collect.Sets;

import no.nav.tps.forvalteren.domain.jpa.Adresse;
import no.nav.tps.forvalteren.domain.jpa.DeathRow;
import no.nav.tps.forvalteren.domain.jpa.Person;
import no.nav.tps.forvalteren.domain.service.tps.servicerutiner.definition.resolvers.skdmeldinger.SkdMeldingResolver;
import no.nav.tps.forvalteren.repository.jpa.DeathRowRepository;
import no.nav.tps.forvalteren.service.command.exceptions.TpsfFunctionalException;
import no.nav.tps.forvalteren.service.command.testdata.skd.SendSkdMeldingTilGitteMiljoer;
import no.nav.tps.forvalteren.service.command.testdata.skd.SkdMessageCreatorTrans1;
import no.nav.tps.forvalteren.service.command.tps.servicerutiner.AdresseService;
import no.nav.tps.forvalteren.service.command.tps.servicerutiner.PersonstatusService;
import no.nav.tps.forvalteren.service.command.tps.skdmelding.skdparam.utils.ConvertStringToDate;
import no.nav.tps.xjc.ctg.domain.s004.PersondataFraTpsS004;

@Service
public class SendDodsmeldingTilTps {

    private static final String PERSON_ER_DOED = "Personen med ident %s er allerede død i miljoe %s.";
    private static final String PERSON_IKKE_DOED = "Personen med ident %s er ikke død i miljoe %s.";

    private enum Action {C, U, D;}

    @Autowired
    private DeathRowRepository deathRowRepository;

    @Autowired
    private SkdMessageCreatorTrans1 skdCreator;

    @Autowired
    private SendSkdMeldingTilGitteMiljoer sendSkdMeldingTilMiljoe;

    @Autowired
    private SkdMeldingResolver doedsmelding;

    @Autowired
    private SkdMeldingResolver doedsmeldingAnnuller;

    @Autowired
    private PersonstatusService personstatusService;

    @Autowired
    private AdresseService adresseService;

    public void execute() {

        List<DeathRow> deathRowTasks = deathRowRepository.findAllByStatus("Ikke sendt");

        for (DeathRow deathRow : deathRowTasks) {

            Person person = new Person();
            person.setId(deathRow.getId());
            person.setIdent(deathRow.getIdent());
            person.setRegdato(LocalDateTime.now());

            PersondataFraTpsS004 persondataFraTpsS004 = personstatusService.hentPersonstatus(person.getIdent(), deathRow.getMiljoe());

            if (Action.U.name().equals(deathRow.getHandling()) || Action.D.name().equals(deathRow.getHandling())) {

                if (StringUtils.isBlank(persondataFraTpsS004.getDatoDo())) {
                    throw new TpsfFunctionalException(String.format(PERSON_IKKE_DOED, person.getIdent(), deathRow.getMiljoe()));
                }
                
                findSecondLastAddress(person, persondataFraTpsS004.getDatoDo(), deathRow.getMiljoe());

                String melding = skdCreator.execute("DoedsmeldingAnnullering", person, true).toString();
                sendSkdMeldingTilMiljoe.execute(melding, doedsmeldingAnnuller.resolve(), Sets.newHashSet(deathRow.getMiljoe()));
            }

            if (Action.U.name().equals(deathRow.getHandling()) || Action.C.name().equals(deathRow.getHandling())) {

                if (Action.C.name().equals(deathRow.getHandling()) && StringUtils.isNotBlank(persondataFraTpsS004.getDatoDo())) {
                    throw new TpsfFunctionalException(String.format(PERSON_ER_DOED, person.getIdent(), deathRow.getMiljoe()));
                }

                person.setDoedsdato(deathRow.getDoedsdato());

                String melding = skdCreator.execute("Doedsmelding", person, true).toString();
                sendSkdMeldingTilMiljoe.execute(melding, doedsmelding.resolve(), Sets.newHashSet(deathRow.getMiljoe()));
            }

            deathRow.setStatus("Sendt");
            deathRowRepository.save(deathRow);
        }
    }

    private void findSecondLastAddress(Person person, String doedsdato, String miljoe) {
        Adresse adresse = adresseService.hentAdresseForDato(person.getIdent(), ConvertStringToDate.yyyysMMsdd(doedsdato).minusDays(1), miljoe);

        if (adresse != null) {
            adresse.setId(person.getId());
            adresse.setPerson(person);
            person.setBoadresse(adresse);

        } else {
            person.setSpesreg(Integer.toString(UFB.ordinal()));
        }
    }
}