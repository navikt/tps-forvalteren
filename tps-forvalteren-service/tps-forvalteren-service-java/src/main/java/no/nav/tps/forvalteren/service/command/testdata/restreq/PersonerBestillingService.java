package no.nav.tps.forvalteren.service.command.testdata.restreq;

import static java.lang.Boolean.TRUE;
import static java.time.LocalDateTime.now;
import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;
import static no.nav.tps.forvalteren.domain.jpa.Sivilstatus.GIFT;
import static no.nav.tps.forvalteren.domain.jpa.Sivilstatus.SAMBOER;
import static no.nav.tps.forvalteren.domain.jpa.Sivilstatus.UGIFT;
import static no.nav.tps.forvalteren.domain.rs.RsBarnRequest.BarnType.DITT;
import static no.nav.tps.forvalteren.domain.rs.RsBarnRequest.BarnType.FELLES;
import static no.nav.tps.forvalteren.domain.rs.RsBarnRequest.BarnType.MITT;
import static no.nav.tps.forvalteren.domain.service.RelasjonType.BARN;
import static no.nav.tps.forvalteren.domain.service.RelasjonType.FAR;
import static no.nav.tps.forvalteren.domain.service.RelasjonType.FOEDSEL;
import static no.nav.tps.forvalteren.domain.service.RelasjonType.MOR;
import static no.nav.tps.forvalteren.domain.service.RelasjonType.PARTNER;
import static no.nav.tps.forvalteren.service.command.testdata.restreq.ExtractOpprettKriterier.extractBarn;
import static no.nav.tps.forvalteren.service.command.testdata.restreq.ExtractOpprettKriterier.extractMainPerson;
import static no.nav.tps.forvalteren.service.command.testdata.restreq.ExtractOpprettKriterier.extractPartner;
import static no.nav.tps.forvalteren.service.command.tps.skdmelding.skdparam.utils.NullcheckUtil.nullcheckSetDefaultValue;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import no.nav.tps.forvalteren.domain.jpa.Person;
import no.nav.tps.forvalteren.domain.jpa.Relasjon;
import no.nav.tps.forvalteren.domain.jpa.Sivilstand;
import no.nav.tps.forvalteren.domain.rs.RsBarnRequest;
import no.nav.tps.forvalteren.domain.rs.RsPersonKriteriumRequest;
import no.nav.tps.forvalteren.domain.rs.dolly.RsPersonBestillingKriteriumRequest;
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

    public List<Person> createTpsfPersonFromRequest(RsPersonBestillingKriteriumRequest request) {
        validateOpprettRequest.validate(request);

        List<Person> hovedPersoner;
        if (request.getOpprettFraIdenter().isEmpty()) {
            RsPersonKriteriumRequest personKriterier = extractMainPerson(request);
            hovedPersoner = savePersonBulk.execute(opprettPersonerOgSjekkMiljoeService.createNyeIdenter(personKriterier));

        } else {
            hovedPersoner = opprettPersonerOgSjekkMiljoeService.createEksisterendeIdenter(request);
        }

        if (nonNull(request.getRelasjoner().getPartner())) {
            request.getRelasjoner().getPartnere().add(request.getRelasjoner().getPartner());
        }
        List<Person> partnere = new ArrayList<>();
        if (!request.getRelasjoner().getPartnere().isEmpty()) {
            RsPersonKriteriumRequest kriteriePartner = extractPartner(request);
            partnere = savePersonBulk.execute(opprettPersonerOgSjekkMiljoeService.createNyeIdenter(kriteriePartner));
        }

        List<Person> barn = new ArrayList<>();
        if (!request.getRelasjoner().getBarn().isEmpty()) {
            RsPersonKriteriumRequest kriterieBarn = extractBarn(request);
            barn = savePersonBulk.execute(opprettPersonerOgSjekkMiljoeService.createNyeIdenter(kriterieBarn));
        }

        setIdenthistorikkPaaPersoner(request, hovedPersoner, partnere, barn);
        setRelasjonerPaaPersoner(hovedPersoner, partnere, barn, request);
        setSivilstandHistorikkPaaPersoner(request, hovedPersoner);

        List<Person> tpsfPersoner = extractOpprettKriterier.addExtendedKriterumValuesToPerson(request, hovedPersoner, partnere, barn);

        List<Person> lagredePersoner = savePersonBulk.execute(tpsfPersoner);

        if (!hovedPersoner.isEmpty()) {
            return sortWithBestiltPersonFoerstIListe(lagredePersoner, hovedPersoner.get(0).getIdent());
        } else {
            throw new TpsfFunctionalException("Ingen ledige identer funnet i miljø.");
        }
    }

    protected static void setSivilstandHistorikkPaaPersoner(RsPersonBestillingKriteriumRequest request, List<Person> personer) {

        personer.forEach(person -> {
            int partnerNumber = 0;
            for (int i = 0; i < person.getRelasjoner().size(); i++) {

                if (PARTNER.name().equals(person.getRelasjoner().get(i).getRelasjonTypeNavn())) {
                    for (int j = 0; j < request.getRelasjoner().getPartnere().get(partnerNumber).getSivilstander().size(); j++) {

                        setSivilstandHistory(person, person.getRelasjoner().get(i),
                                request.getRelasjoner().getPartnere().get(partnerNumber).getSivilstander().get(j).getSivilstand(),
                                request.getRelasjoner().getPartnere().get(partnerNumber).getSivilstander().get(j).getSivilstandRegdato());
                    }

                    if (isNotUgiftAndNotSamboer(request.getSivilstand()) && partnerNumber == 0) {

                        if (person.getRelasjoner().get(partnerNumber).getPersonRelasjonMed().getSivilstander().isEmpty()) {

                            // Hvis sivilstand på hovedperson er annet enn gift preppes gift først da separert/skilt/enke må foranledes med gift
                            if (!GIFT.name().equals(request.getSivilstand())) {
                                setSivilstandHistory(person, person.getRelasjoner().get(partnerNumber), GIFT.name(),
                                        nullcheckSetDefaultValue(request.getSivilstandRegdato(), now()).minusYears(3));
                            }
                            setSivilstandHistory(person, person.getRelasjoner().get(partnerNumber), request.getSivilstand(),
                                    nullcheckSetDefaultValue(request.getSivilstandRegdato(), now().minusYears(1)));

                        } else if (!request.getSivilstand().equals(person.getRelasjoner().get(partnerNumber).getPersonRelasjonMed().getSivilstander().get(0).getSivilstand())) {

                            setSivilstandHistory(person, person.getRelasjoner().get(partnerNumber), request.getSivilstand(), now());
                        }
                    }
                    partnerNumber++;
                }
            }
        });
    }

    private static boolean isNotUgiftAndNotSamboer(String sivilstand) {

        return isNotBlank(sivilstand) && !UGIFT.getKodeverkskode().equals(sivilstand) && !SAMBOER.getKodeverkskode().equals(sivilstand);
    }

    protected static void setSivilstandHistory(Person person, Relasjon relasjon, String sivilstand, LocalDateTime regdato) {

        person.getSivilstander().add(Sivilstand.builder()
                .person(person)
                .personRelasjonMed(relasjon.getPersonRelasjonMed())
                .sivilstand(sivilstand)
                .sivilstandRegdato(regdato)
                .build());

        relasjon.getPersonRelasjonMed().getSivilstander()
                .add(Sivilstand.builder()
                        .person(relasjon.getPersonRelasjonMed())
                        .personRelasjonMed(person)
                        .sivilstand(sivilstand)
                        .sivilstandRegdato(regdato)
                        .build());
    }

    protected void setIdenthistorikkPaaPersoner(RsPersonBestillingKriteriumRequest request, List<Person> hovedPersoner, List<Person> partnere, List<Person> barna) {

        hovedPersoner.forEach(hovedperson ->
                personIdenthistorikkService.prepareIdenthistorikk(hovedperson, request.getIdentHistorikk()));
        for (int i = 0; i < partnere.size(); i++) {
            personIdenthistorikkService.prepareIdenthistorikk(partnere.get(i), request.getRelasjoner().getPartnere().get(i).getIdentHistorikk());
        }
        for (int i = 0; i < barna.size(); i++) {
            personIdenthistorikkService.prepareIdenthistorikk(barna.get(i), request.getRelasjoner().getBarn().get(i).getIdentHistorikk());
        }
    }

    protected static List<Person> sortWithBestiltPersonFoerstIListe(List<Person> personer, String identBestiltPerson) {

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

    protected static void setRelasjonerPaaPersoner(List<Person> personer, List<Person> partnere, List<Person> barn, RsPersonBestillingKriteriumRequest request) {

        int antallBarn = barn.isEmpty() ? 0 : barn.size() / personer.size();
        int antallPartnere = partnere.isEmpty() ? 0 : partnere.size() / personer.size();

        for (int i = 0; i < personer.size(); i++) {

            Person hovedPerson = personer.get(i);
            Map<Integer, Person> minePartnere = new HashMap(antallPartnere);
            for (int j = 0; j < antallPartnere; j++) {

                int startIndexPartner = i * antallPartnere;
                minePartnere.put(nullcheckSetDefaultValue(request.getRelasjoner().getPartnere().get(j).getPartnerNr(), j),
                        partnere.get(startIndexPartner + j));
            }
            minePartnere.values().forEach(partner -> lagPartnerRelasjon(hovedPerson, partner));

            for (int j = 0; j < antallBarn; j++) {
                int startIndexBarn = i * antallBarn;
                setBarnRelasjon(hovedPerson,
                        antallPartnere > 0 ?
                                getPartner(minePartnere, request.getRelasjoner().getBarn().get(j).getPartnerNr(), j % antallPartnere) :
                                null,
                        barn.get(startIndexBarn + j),
                        request.getRelasjoner().getBarn().get(j));
            }
        }
    }

    private static Person getPartner(Map<Integer, Person> minePartnere, Integer partnerNr, int index) {

        return nonNull(partnerNr) ? minePartnere.get(partnerNr - 1) : minePartnere.get(index);
    }

    private static void setBarnRelasjon(Person hovedPerson, Person partner, Person barn, RsBarnRequest request) {

        if (isNull(request.getBarnType()) || FELLES == request.getBarnType()) {

            setRelasjonForBarn(hovedPerson, barn, TRUE.equals(request.getErAdoptert()));
            setRelasjonForBarn(partner, barn, TRUE.equals(request.getErAdoptert()));

        } else if (MITT == request.getBarnType()) {

            setRelasjonForBarn(hovedPerson, barn, TRUE.equals(request.getErAdoptert()));
            setRelasjonForBarn(partner, barn, true);

        } else if (DITT == request.getBarnType()) {

            setRelasjonForBarn(partner, barn, TRUE.equals(request.getErAdoptert()));
            setRelasjonForBarn(hovedPerson, barn, true);
        }
    }

    private static void setRelasjonForBarn(Person forelder, Person barn, boolean isAdopted) {
        if (nonNull(forelder)) {
            forelder.getRelasjoner().add(Relasjon.builder().person(forelder).personRelasjonMed(barn).relasjonTypeNavn((isAdopted ? BARN : FOEDSEL).name()).build());
            barn.getRelasjoner().add(Relasjon.builder().person(barn).personRelasjonMed(forelder).relasjonTypeNavn((isKvinne(forelder) ? MOR : FAR).name()).build());
        }
    }

    private static boolean isKvinne(Person person) {
        return "K".equals(person.getKjonn());
    }

    private static void lagPartnerRelasjon(Person person, Person partner) {
        person.getRelasjoner().add(Relasjon.builder().person(person).personRelasjonMed(partner).relasjonTypeNavn(PARTNER.getName()).build());
        partner.getRelasjoner().add(Relasjon.builder().person(partner).personRelasjonMed(person).relasjonTypeNavn(PARTNER.getName()).build());
    }
}
