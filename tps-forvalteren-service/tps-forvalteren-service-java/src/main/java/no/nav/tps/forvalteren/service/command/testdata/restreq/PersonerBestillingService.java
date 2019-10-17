package no.nav.tps.forvalteren.service.command.testdata.restreq;

import static java.util.Objects.isNull;
import static no.nav.tps.forvalteren.service.command.testdata.restreq.ExtractOpprettKriterier.extractBarn;
import static no.nav.tps.forvalteren.service.command.testdata.restreq.ExtractOpprettKriterier.extractMainPerson;
import static no.nav.tps.forvalteren.service.command.testdata.restreq.ExtractOpprettKriterier.extractPartner;

import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ma.glasnost.orika.MapperFacade;
import no.nav.tps.forvalteren.domain.jpa.Person;
import no.nav.tps.forvalteren.domain.jpa.Relasjon;
import no.nav.tps.forvalteren.domain.rs.RsPersonKriteriumRequest;
import no.nav.tps.forvalteren.domain.rs.dolly.RsPersonBestillingKriteriumRequest;
import no.nav.tps.forvalteren.domain.service.RelasjonType;
import no.nav.tps.forvalteren.service.command.exceptions.TpsfFunctionalException;
import no.nav.tps.forvalteren.service.command.testdata.SavePersonBulk;
import no.nav.tps.forvalteren.service.command.testdata.opprett.OpprettPersonerOgSjekkMiljoeService;

@Service
public class PersonerBestillingService {

    @Autowired
    private SavePersonBulk savePersonBulk;

    @Autowired
    private ExtractOpprettKriterier extractOpprettKriterier;

    @Autowired
    private ValidateOpprettRequest validateOpprettRequest;

    @Autowired
    private OpprettPersonerOgSjekkMiljoeService opprettPersonerOgSjekkMiljoeService;

    @Autowired
    private PersonIdenthistorikkService personIdenthistorikkService;

    @Autowired
    private MapperFacade mapperFacade;

    public List<Person> createTpsfPersonFromRestRequest(RsPersonBestillingKriteriumRequest personKriteriumRequest) {
        validateOpprettRequest.validate(personKriteriumRequest);

        List<Person> hovedPersoner;
        List<Person> partnere = new ArrayList<>();
        List<Person> barn = new ArrayList<>();
        if (personKriteriumRequest.getOpprettFraIdenter().isEmpty()) {
            RsPersonKriteriumRequest personKriterier = extractMainPerson(personKriteriumRequest);
            RsPersonKriteriumRequest kriteriePartner = extractPartner(personKriteriumRequest);
            RsPersonKriteriumRequest kriterieBarn = extractBarn(personKriteriumRequest);

            hovedPersoner = savePersonBulk.execute(opprettPersonerOgSjekkMiljoeService.createNyeIdenter(personKriterier));

            if (!personKriteriumRequest.getRelasjoner().getPartnere().isEmpty()) {
                partnere = savePersonBulk.execute(opprettPersonerOgSjekkMiljoeService.createNyeIdenter(kriteriePartner));
            }
            if (!personKriteriumRequest.getRelasjoner().getBarn().isEmpty()) {
                barn = savePersonBulk.execute(opprettPersonerOgSjekkMiljoeService.createNyeIdenter(kriterieBarn));
            }

            setIdenthistorikkPaaPersoner(personKriteriumRequest, hovedPersoner, partnere, barn);
            setRelasjonerPaaPersoner(hovedPersoner, partnere, barn);
            setSivilstandHistorikkPaaPersoner(personKriteriumRequest, hovedPersoner, partnere, barn);
        } else {
            hovedPersoner = opprettPersonerOgSjekkMiljoeService.createEksisterendeIdenter(personKriteriumRequest);
        }

        List<Person> tpsfPersoner = extractOpprettKriterier.addExtendedKriterumValuesToPerson(personKriteriumRequest, hovedPersoner, partnere, barn);

        List<Person> lagredePersoner = savePersonBulk.execute(tpsfPersoner);

        if (!hovedPersoner.isEmpty()) {
            return sortWithBestiltPersonFoerstIListe(lagredePersoner, hovedPersoner.get(0).getIdent());
        } else {
            throw new TpsfFunctionalException("Ingen ledige identer funnet i miljø.");
        }
    }

    private void setSivilstandHistorikkPaaPersoner(RsPersonBestillingKriteriumRequest personKriteriumRequest, List<Person> hovedPersoner, List<Person> partnere, List<Person> barn) {


    }

    private void setIdenthistorikkPaaPersoner(RsPersonBestillingKriteriumRequest request, List<Person> hovedPersoner, List<Person> partnere, List<Person> barna) {

        hovedPersoner.forEach(hovedperson ->
                personIdenthistorikkService.prepareIdenthistorikk(hovedperson, request.getIdentHistorikk()));
        for (int i = 0; i < partnere.size(); i++) {
            personIdenthistorikkService.prepareIdenthistorikk(partnere.get(i), request.getRelasjoner().getPartnere().get(i).getIdentHistorikk());
        }
        for (int i = 0; i < barna.size(); i++) {
            personIdenthistorikkService.prepareIdenthistorikk(barna.get(i), request.getRelasjoner().getBarn().get(i).getIdentHistorikk());
        }
    }

    private static List<Person> sortWithBestiltPersonFoerstIListe(List<Person> personer, String identBestiltPerson) {

        List<Person> sorted = new ArrayList<>();
        if (!personer.isEmpty()) {
            for (Person person : personer) {
                if (person.getIdent().equals(identBestiltPerson)) {
                    sorted.add(0, person);
                } else {
                    sorted.add(person);
                }
            }
        }
        return sorted;
    }

    protected static void setRelasjonerPaaPersoner(List<Person> personer, List<Person> partnere, List<Person> barn) {

        int antallBarn = barn.isEmpty() ? 0 : barn.size() / personer.size();
        int antallPartnere = partnere.isEmpty() ? 0 : partnere.size() / personer.size();

        for (int i = 0; i < personer.size(); i++) {

            for (int j = 0; j < antallPartnere; j++) {
                int startIndexPartner = i * antallPartnere;
                lagPartnerRelasjon(personer.get(i), partnere.get(startIndexPartner + j));
            }

            for (int j = 0; j < antallBarn; j++) {
                int startIndexBarn = i * antallBarn;
                int startIndexPartner = i * antallPartnere;
                setBarnRelasjon(personer.get(i),
                        antallPartnere != 0 ? partnere.get(startIndexPartner + (j % antallPartnere)) : null,
                        barn.get(startIndexBarn + j));
            }
        }
    }

    private static void setBarnRelasjon(Person forelder, Person barn) {

        if (isMann(forelder)) {
            setFarBarnRelasjonMedInnvadring(forelder, barn);
        }

        if (isKvinne(forelder)) {
            setMorBarnRelasjonMedFodsel(forelder, barn);
        }
    }

    private static void setBarnRelasjon(Person person, Person partner, Person barn) {

        if (isNull(partner)) {
            setBarnRelasjon(person, barn);
            return;
        }

        if (isMotsattKjonn(person, partner)) {
            if (isKvinne(person)) {
                setMorBarnRelasjonMedFodsel(person, barn);
                setFarBarnRelasjonMedFodsel(partner, barn);
            } else {
                setMorBarnRelasjonMedFodsel(partner, barn);
                setFarBarnRelasjonMedFodsel(person, barn);
            }
        } else if (isToKvinner(person, partner)) {
            setMorBarnRelasjonMedFodsel(person, barn);
            setFarBarnRelasjonMedInnvadring(partner, barn);
        } else {
            setFarBarnRelasjonMedInnvadring(person, barn);
            setFarBarnRelasjonMedInnvadring(partner, barn);
        }
    }

    private static void setFarBarnRelasjonMedInnvadring(Person far, Person barn) {
        far.getRelasjoner().add(new Relasjon(far, barn, RelasjonType.BARN.getName()));
        barn.getRelasjoner().add(new Relasjon(barn, far, RelasjonType.FAR.getName()));
    }

    private static void setMorBarnRelasjonMedFodsel(Person mor, Person barn) {
        mor.getRelasjoner().add(new Relasjon(mor, barn, RelasjonType.FOEDSEL.getName()));
        barn.getRelasjoner().add(new Relasjon(barn, mor, RelasjonType.MOR.getName()));
    }

    private static void setFarBarnRelasjonMedFodsel(Person mor, Person barn) {
        mor.getRelasjoner().add(new Relasjon(mor, barn, RelasjonType.FOEDSEL.getName()));
        barn.getRelasjoner().add(new Relasjon(barn, mor, RelasjonType.FAR.getName()));
    }

    private static boolean isMotsattKjonn(Person person, Person partner) {
        return !person.getKjonn().equals(partner.getKjonn());
    }

    private static boolean isToKvinner(Person person, Person partner) {
        return isKvinne(person) && isKvinne(partner);
    }

    private static boolean isKvinne(Person person) {
        return "K".equals(person.getKjonn());
    }

    private static boolean isMann(Person person) {
        return "M".equals(person.getKjonn());
    }

    private static void lagPartnerRelasjon(Person person, Person partner) {
        person.getRelasjoner().add(new Relasjon(person, partner, RelasjonType.PARTNER.getName()));
        partner.getRelasjoner().add(new Relasjon(partner, person, RelasjonType.PARTNER.getName()));
    }
}
