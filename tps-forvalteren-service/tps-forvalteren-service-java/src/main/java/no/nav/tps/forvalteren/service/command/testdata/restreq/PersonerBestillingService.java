package no.nav.tps.forvalteren.service.command.testdata.restreq;

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

            if (harPartner(personKriteriumRequest)) {
                partnere = savePersonBulk.execute(opprettPersonerOgSjekkMiljoeService.createNyeIdenter(kriteriePartner));
            }
            if (harBarn(personKriteriumRequest)) {
                barn = savePersonBulk.execute(opprettPersonerOgSjekkMiljoeService.createNyeIdenter(kriterieBarn));
            }

            setIdenthistorikkPaaPersoner(personKriteriumRequest, hovedPersoner, partnere, barn);
            setRelasjonerPaaPersoner(hovedPersoner, partnere, barn);
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

    private void setIdenthistorikkPaaPersoner(RsPersonBestillingKriteriumRequest request, List<Person> hovedPersoner, List<Person> partnere, List<Person> barna) {

        hovedPersoner.forEach(hovedperson ->
                personIdenthistorikkService.prepareIdenthistorikk(hovedperson.getIdent(), request.getIdenthistorikk()));
        partnere.forEach(partner ->
                personIdenthistorikkService.prepareIdenthistorikk(partner.getIdent(), request.getRelasjoner().getPartner().getIdenthistorikk()));
        for (int i = 0; i <barna.size(); i++) {
            personIdenthistorikkService.prepareIdenthistorikk(barna.get(i).getIdent(), request.getRelasjoner().getBarn().get(i).getIdenthistorikk());
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

    protected static void setRelasjonerPaaPersoner(List<Person> personer, List<Person> partnerListe, List<Person> barn) {
        int antallbarn = (barn == null || barn.isEmpty()) ? 0 : barn.size() / personer.size();

        for (int i = 0; i < personer.size(); i++) {
            Person person = personer.get(i);
            person.setRelasjoner(new ArrayList<>());
            Person partner = null;

            if (partnerListe != null && !partnerListe.isEmpty()) {
                partner = partnerListe.get(i);
                partner.setRelasjoner(new ArrayList<>());

                lagPartnerRelasjon(person, partner);
            }

            for (int j = 0; j < antallbarn; j++) {
                int startIndexBarn = i * antallbarn;
                Person barnet = barn.get(startIndexBarn + j);
                setBarnRelasjon(person, partner, barnet);
            }
        }
    }

    private static void setBarnRelasjon(Person forelder, Person barn) {
        if (forelder == null || barn == null) {
            return;
        }

        if (isMann(forelder)) {
            setFarBarnRelasjonMedInnvadring(forelder, barn);
        }

        if (isKvinne(forelder)) {
            setMorBarnRelasjonMedFodsel(forelder, barn);
        }
    }

    private static void setBarnRelasjon(Person person, Person partner, Person barn) {
        if (partner == null) {
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

    private static boolean harPartner(RsPersonBestillingKriteriumRequest request) {
        return request.getRelasjoner() != null && request.getRelasjoner().getPartner() != null;
    }

    private static boolean harBarn(RsPersonBestillingKriteriumRequest request) {
        return request.getRelasjoner() != null && request.getRelasjoner().getBarn() != null && !request.getRelasjoner().getBarn().isEmpty();
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
        person.getRelasjoner().add(new Relasjon(person, partner, RelasjonType.EKTEFELLE.getName()));
        partner.getRelasjoner().add(new Relasjon(partner, person, RelasjonType.EKTEFELLE.getName()));
    }
}
