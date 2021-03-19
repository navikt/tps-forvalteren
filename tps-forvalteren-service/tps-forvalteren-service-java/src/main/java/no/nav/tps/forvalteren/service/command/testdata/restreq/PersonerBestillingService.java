package no.nav.tps.forvalteren.service.command.testdata.restreq;

import static java.lang.Boolean.TRUE;
import static java.time.LocalDateTime.now;
import static java.util.Collections.emptyList;
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
import static no.nav.tps.forvalteren.service.command.testdata.restreq.OpprettPersonUtil.extractBarn;
import static no.nav.tps.forvalteren.service.command.testdata.restreq.OpprettPersonUtil.extractForeldre;
import static no.nav.tps.forvalteren.service.command.testdata.restreq.OpprettPersonUtil.extractMainPerson;
import static no.nav.tps.forvalteren.service.command.testdata.restreq.OpprettPersonUtil.extractPartner;
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

    @Autowired
    private EnforceForeldreKjoennService enforceForeldreKjoennService;

    @Autowired
    private VergemaalService vergemaalService;

    @Autowired
    private FullmaktService fullmaktService;

    public List<Person> createTpsfPersonFromRequest(RsPersonBestillingKriteriumRequest request) {

        validateOpprettRequest.validate(request);

        var tpsfPersoner = new ArrayList<Person>();
        var hovedPersoner = new ArrayList<Person>();

        for (int i = 0; i < request.getAntall(); i++) {

            enforceForeldreKjoennService.setDefaultKjoenn(request);

            if (request.getOpprettFraIdenter().isEmpty()) {
                RsPersonKriteriumRequest personKriterier = extractMainPerson(request);
                hovedPersoner.addAll(savePersonBulk.execute(opprettPersonerOgSjekkMiljoeService.createNyeIdenter(personKriterier)));

            } else {
                hovedPersoner.addAll(opprettPersonerOgSjekkMiljoeService.createEksisterendeIdenter(request));
            }

            if (nonNull(request.getRelasjoner().getPartner())) {
                request.getRelasjoner().getPartnere().add(request.getRelasjoner().getPartner());
            }
            List<Person> partnere = emptyList();
            if (!request.getRelasjoner().getPartnere().isEmpty()) {
                RsPersonKriteriumRequest kriteriePartner = extractPartner(request.getRelasjoner().getPartnere(),
                        request.getHarMellomnavn(), request.getNavSyntetiskIdent());
                partnere = savePersonBulk.execute(opprettPersonerOgSjekkMiljoeService.createNyeIdenter(kriteriePartner));
            }

            List<Person> barn = emptyList();
            if (!request.getRelasjoner().getBarn().isEmpty()) {
                RsPersonKriteriumRequest kriterieBarn = extractBarn(request.getRelasjoner().getBarn(),
                        request.getHarMellomnavn(), request.getNavSyntetiskIdent());
                barn = savePersonBulk.execute(opprettPersonerOgSjekkMiljoeService.createNyeIdenter(kriterieBarn));
            }

            List<Person> foreldre = emptyList();
            if (!request.getRelasjoner().getForeldre().isEmpty()) {
                RsPersonKriteriumRequest kriterieForeldre = extractForeldre(request.getRelasjoner().getForeldre(),
                        request.getHarMellomnavn(), request.getNavSyntetiskIdent());
                foreldre = savePersonBulk.execute(opprettPersonerOgSjekkMiljoeService.createNyeIdenter(kriterieForeldre));
            }

            setIdenthistorikkPaaPersoner(request, hovedPersoner, partnere, barn, foreldre);
            setRelasjonerPaaPersoner(hovedPersoner, partnere, barn, request);
            setForeldreRelasjonerPaaPersoner(hovedPersoner, foreldre, request);
            setSivilstandHistorikkPaaPersoner(request, hovedPersoner);
            vergemaalService.opprettVerge(request, hovedPersoner);
            fullmaktService.opprettFullmakt(request, hovedPersoner);

            /**
             * Add extended criteria etc benyttes kun for Dolly bestillinger som alltid sendes inn en og en.
             * Forenkler implementasjonen til kun å håndtere en hovedperson.
             */
            tpsfPersoner.addAll(extractOpprettKriterier.addExtendedKriterumValuesToPerson(request, hovedPersoner.get(i), partnere, barn, foreldre));
        }

        List<Person> lagredePersoner = savePersonBulk.execute(tpsfPersoner);

        if (!hovedPersoner.isEmpty()) {
            return lagredePersoner;
        } else {
            throw new TpsfFunctionalException("Ingen ledige identer funnet i miljø.");
        }
    }

    protected void setForeldreRelasjonerPaaPersoner(List<Person> hovedPerson, List<Person> foreldre, RsPersonBestillingKriteriumRequest request) {

        for (int i = 0; i < request.getRelasjoner().getForeldre().size(); i++) {
            hovedPerson.stream().findFirst().get().getRelasjoner().add(Relasjon.builder()
                    .person(hovedPerson.stream().findFirst().get())
                    .personRelasjonMed(foreldre.get(i))
                    .relasjonTypeNavn(nullcheckSetDefaultValue(request.getRelasjoner().getForeldre().get(i).getForeldreType(),
                            foreldre.get(i).isKvinne() ? MOR : FAR).name())
                    .build());
            foreldre.get(i).getRelasjoner().add(Relasjon.builder()
                    .person(foreldre.get(i))
                    .personRelasjonMed(hovedPerson.stream().findFirst().get())
                    .relasjonTypeNavn(BARN.getName())
                    .build());
        }
    }

    protected static void setSivilstandHistorikkPaaPersoner(RsPersonBestillingKriteriumRequest request, List<Person> personer) {

        personer.forEach(person -> {
            int partnerNumber = 0;
            for (int i = 0; i < person.getRelasjoner().size(); i++) {

                if (PARTNER.name().equals(person.getRelasjoner().get(i).getRelasjonTypeNavn())) {
                    for (int j = 0; j < request.getRelasjoner().getPartnere().get(partnerNumber).getSivilstander().size(); j++) {

                        setSivilstandHistory(person, person.getRelasjoner().get(i).getPersonRelasjonMed(),
                                request.getRelasjoner().getPartnere().get(partnerNumber).getSivilstander().get(j).getSivilstand(),
                                request.getRelasjoner().getPartnere().get(partnerNumber).getSivilstander().get(j).getSivilstandRegdato());
                    }

                    if (isNotUgiftAndNotSamboer(request.getSivilstand()) && partnerNumber == 0) {

                        if (person.getRelasjoner().get(partnerNumber).getPersonRelasjonMed().getSivilstander().isEmpty()) {

                            // Hvis sivilstand på hovedperson er annet enn gift preppes gift først da separert/skilt/enke må foranledes med gift
                            if (!GIFT.name().equals(request.getSivilstand())) {
                                setSivilstandHistory(person, person.getRelasjoner().get(partnerNumber).getPersonRelasjonMed(), GIFT.name(),
                                        nullcheckSetDefaultValue(request.getSivilstandRegdato(), now()).minusYears(3));
                            }
                            setSivilstandHistory(person, person.getRelasjoner().get(partnerNumber).getPersonRelasjonMed(), request.getSivilstand(),
                                    nullcheckSetDefaultValue(request.getSivilstandRegdato(), now().minusYears(1)));

                        } else if (!request.getSivilstand().equals(person.getRelasjoner().get(partnerNumber).getPersonRelasjonMed().getSivilstander().get(0).getSivilstand())) {

                            setSivilstandHistory(person, person.getRelasjoner().get(partnerNumber).getPersonRelasjonMed(), request.getSivilstand(), now());
                        }
                    }
                    partnerNumber++;
                }
            }
        });
    }

    protected static boolean isNotUgiftAndNotSamboer(String sivilstand) {

        return isNotBlank(sivilstand) && !UGIFT.getKodeverkskode().equals(sivilstand) && !SAMBOER.getKodeverkskode().equals(sivilstand);
    }

    protected static void setSivilstandHistory(Person person, Person partner, String sivilstand, LocalDateTime regdato) {

        person.getSivilstander().add(Sivilstand.builder()
                .person(person)
                .personRelasjonMed(partner)
                .sivilstand(sivilstand)
                .sivilstandRegdato(regdato)
                .build());

        partner.getSivilstander()
                .add(Sivilstand.builder()
                        .person(partner)
                        .personRelasjonMed(person)
                        .sivilstand(sivilstand)
                        .sivilstandRegdato(regdato)
                        .build());
    }

    protected static void setRelasjonerPaaPersoner(List<Person> personer, List<Person> partnere, List<Person> barn, RsPersonBestillingKriteriumRequest request) {

        int antallBarn = barn.isEmpty() ? 0 : barn.size() / personer.size();
        int antallPartnere = partnere.isEmpty() ? 0 : partnere.size() / personer.size();

        for (int i = 0; i < personer.size(); i++) {

            Person hovedPerson = personer.get(i);
            Map<Integer, Person> minePartnere = new HashMap(antallPartnere);
            for (int j = 0; j < antallPartnere; j++) {

                int startIndexPartner = i * antallPartnere;
                if (partnere.get(startIndexPartner + j).isNyPerson()) {
                    minePartnere.put(nullcheckSetDefaultValue(request.getRelasjoner().getPartnere().get(j).getPartnerNr(), j),
                            partnere.get(startIndexPartner + j));
                }
            }
            minePartnere.values().forEach(partner -> lagPartnerRelasjon(hovedPerson, partner));

            for (int j = 0; j < antallBarn; j++) {
                int startIndexBarn = i * antallBarn;
                if (barn.get(startIndexBarn + j).isNyPerson()) {
                    setBarnRelasjon(hovedPerson,
                            antallPartnere > 0 ?
                                    getPartner(minePartnere, request.getRelasjoner().getBarn().get(j).getPartnerNr(), j % antallPartnere) :
                                    null,
                            barn.get(startIndexBarn + j),
                            request.getRelasjoner().getBarn().get(j));
                }
            }
        }
    }

    protected void setIdenthistorikkPaaPersoner(RsPersonBestillingKriteriumRequest request, List<Person> hovedPersoner,
            List<Person> partnere, List<Person> barna, List<Person> foreldre) {

        hovedPersoner.forEach(hovedperson ->
                personIdenthistorikkService.prepareIdenthistorikk(hovedperson, request.getIdentHistorikk(), request.getNavSyntetiskIdent()));
        for (int i = 0; i < partnere.size(); i++) {
            personIdenthistorikkService.prepareIdenthistorikk(partnere.get(i),
                    request.getRelasjoner().getPartnere().get(i).getIdentHistorikk(), request.getNavSyntetiskIdent());
        }
        for (int i = 0; i < barna.size(); i++) {
            personIdenthistorikkService.prepareIdenthistorikk(barna.get(i),
                    request.getRelasjoner().getBarn().get(i).getIdentHistorikk(), request.getNavSyntetiskIdent());
        }

        for (int i = 0; i < foreldre.size(); i++) {
            personIdenthistorikkService.prepareIdenthistorikk(foreldre.get(i),
                    request.getRelasjoner().getForeldre().get(i).getIdentHistorikk(), request.getNavSyntetiskIdent());
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
            barn.getRelasjoner().add(Relasjon.builder().person(barn).personRelasjonMed(forelder).relasjonTypeNavn(forelder.isKvinne() ? MOR.name() : FAR.name()).build());
        }
    }

    private static void lagPartnerRelasjon(Person person, Person partner) {
        person.getRelasjoner().add(Relasjon.builder().person(person).personRelasjonMed(partner).relasjonTypeNavn(PARTNER.getName()).build());
        partner.getRelasjoner().add(Relasjon.builder().person(partner).personRelasjonMed(person).relasjonTypeNavn(PARTNER.getName()).build());
    }
}
