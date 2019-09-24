package no.nav.tps.forvalteren.service.command.dodsmeldinger;

import static no.nav.tps.forvalteren.domain.rs.skd.DoedsmeldingHandlingType.C;
import static no.nav.tps.forvalteren.domain.rs.skd.DoedsmeldingHandlingType.D;
import static no.nav.tps.forvalteren.domain.rs.skd.DoedsmeldingHandlingType.U;
import static no.nav.tps.forvalteren.domain.service.tps.servicerutiner.definition.resolvers.skdmeldinger.DoedsmeldingAarsakskode43.DOEDSMELDING_MLD_NAVN;
import static no.nav.tps.forvalteren.domain.service.tps.servicerutiner.definition.resolvers.skdmeldinger.DoedsmeldingAnnulleringAarsakskode45.DOEDSMELDINGANNULLERING_MLD_NAVN;
import static org.apache.commons.lang3.StringUtils.isBlank;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.google.common.collect.Sets;

import no.nav.tps.forvalteren.domain.jpa.Adresse;
import no.nav.tps.forvalteren.domain.jpa.DeathRow;
import no.nav.tps.forvalteren.domain.jpa.Person;
import no.nav.tps.forvalteren.domain.service.tps.servicerutiner.definition.resolvers.skdmeldinger.SkdMeldingResolver;
import no.nav.tps.forvalteren.repository.jpa.DeathRowRepository;
import no.nav.tps.forvalteren.service.command.exceptions.TpsfFunctionalException;
import no.nav.tps.forvalteren.service.command.exceptions.TpsfTechnicalException;
import no.nav.tps.forvalteren.service.command.testdata.skd.SendSkdMeldingTilGitteMiljoer;
import no.nav.tps.forvalteren.service.command.testdata.skd.SkdMessageCreatorTrans1;
import no.nav.tps.forvalteren.service.command.tps.servicerutiner.PersonAdresseService;
import no.nav.tps.forvalteren.service.command.tps.servicerutiner.PersonstatusService;
import no.nav.tps.forvalteren.service.command.tps.skdmelding.skdparam.utils.ConvertStringToDate;
import no.nav.tps.xjc.ctg.domain.s004.PersondataFraTpsS004;

@Service
public class SendDodsmeldingTilTpsService {

    private static final String PERSON_ER_DOED = "Personen med ident %s er allerede død i miljø %s.";
    private static final String PERSON_IKKE_DOED = "Personen med ident %s er ikke død i miljø %s.";

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
    private PersonAdresseService personAdresseService;

    public void execute() {
        List<DeathRow> deathRowTasks = deathRowRepository.findAllByStatus("Ikke sendt");

        for (DeathRow deathRow : deathRowTasks) {

            Person person = new Person();
            person.setId(deathRow.getId());
            person.setIdent(deathRow.getIdent());
            person.setRegdato(LocalDateTime.now());

            PersondataFraTpsS004 persondataFraTps = hentPersonstatus(person.getIdent(), deathRow.getMiljoe());

            sendAnnulering(person, persondataFraTps.getDatoDo(), deathRow.getHandling(), deathRow.getMiljoe());
            sendDoedsmelding(person, persondataFraTps.getDatoDo(), deathRow.getDoedsdato(), deathRow.getHandling(), deathRow.getMiljoe());

            deathRow.setStatus("Sendt");
            deathRowRepository.save(deathRow);
        }
    }

    protected PersondataFraTpsS004 hentPersonstatus(String ident, String miljoe) {

        try {
            return personstatusService.hentPersonstatus(ident, miljoe);
        } catch (TpsfTechnicalException e) {
            throw new TpsfFunctionalException(
                    String.format("Fant ikke person med ident %s i miljø %s", ident, miljoe), e);
        }
    }

    protected Map<String, String> sendDoedsmelding(Person person, String tpsDoedsdato, LocalDateTime doedsdato, String handling, String miljoe) {

        if (U.name().equals(handling) || C.name().equals(handling)) {

            if (C.name().equals(handling) && StringUtils.isNotBlank(tpsDoedsdato)) {
                throw new TpsfFunctionalException(String.format(PERSON_ER_DOED, person.getIdent(), miljoe));
            }

            person.setDoedsdato(doedsdato);

            return sendMelding(person, DOEDSMELDING_MLD_NAVN, doedsmelding, miljoe);
        }
        return Collections.emptyMap();
    }

    protected Map<String, String> sendAnnulering(Person person, String doedsdato, String handling, String miljoe) {

        if (U.name().equals(handling) || D.name().equals(handling)) {

            if (isBlank(doedsdato)) {
                throw new TpsfFunctionalException(String.format(PERSON_IKKE_DOED, person.getIdent(), miljoe));
            }

            findLastAddress(person, doedsdato, miljoe);

            return sendMelding(person, DOEDSMELDINGANNULLERING_MLD_NAVN, doedsmeldingAnnuller, miljoe);
        }
        return Collections.emptyMap();
    }

    private Map<String, String> sendMelding(Person person, String type, SkdMeldingResolver resolver, String miljoe) {

        String melding = skdCreator.execute(type, person, true).toString();
        return sendSkdMeldingTilMiljoe.execute(melding, resolver.resolve(), Sets.newHashSet(miljoe));
    }

    private void findLastAddress(Person person, String doedsdato, String miljoe) {

        Adresse adresse = personAdresseService.hentBoadresseForDato(person.getIdent(), ConvertStringToDate.yyyysMMsdd(doedsdato).minusDays(1), miljoe);

        if (adresse != null) {
            adresse.setId(person.getId());
            adresse.setPerson(person);
            person.setBoadresse(adresse);
        }
    }
}