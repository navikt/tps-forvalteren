package no.nav.tps.forvalteren.service.command.testdata.restreq;

import lombok.RequiredArgsConstructor;
import no.nav.tps.forvalteren.common.java.message.MessageProvider;
import no.nav.tps.forvalteren.domain.jpa.Person;
import no.nav.tps.forvalteren.domain.jpa.Relasjon;
import no.nav.tps.forvalteren.domain.rs.RsSimpleRelasjoner;
import no.nav.tps.forvalteren.domain.rs.dolly.RsPersonBestillingKriteriumRequest;
import no.nav.tps.forvalteren.repository.jpa.PersonRepository;
import no.nav.tps.forvalteren.service.command.exceptions.TpsfFunctionalException;
import no.nav.tps.forvalteren.service.command.testdata.SavePersonBulk;
import no.nav.tps.forvalteren.service.command.testdata.opprett.OpprettPersonerOgSjekkMiljoeService;
import org.springframework.stereotype.Service;

import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

import static java.util.Collections.singletonList;
import static java.util.Objects.nonNull;
import static no.nav.tps.forvalteren.domain.service.RelasjonType.PARTNER;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

/**
 * Denne klasse benyttes for å legge til nye personer <br>
 * nye partner(e) og nye barn eksisterer ikke fra før og opprettes ved denne bestillingen <br>
 * Hvis hovedperson har partner(e) fra før betyr første partner i denne bestilling en oppdatering av sivilstand på forrige partner
 */
@Service
@RequiredArgsConstructor
public class RelasjonNyePersonerBestillingService extends PersonerBestillingService {

    private final SavePersonBulk savePersonBulk;
    private final OpprettPersonerOgSjekkMiljoeService opprettPersonerOgSjekkMiljoeService;
    private final RelasjonExtractOpprettKriterier relasjonExtractOpprettKriterier;
    private final MessageProvider messageProvider;
    private final PersonRepository personRepository;
    private final VergemaalService vergemaalService;
    private final FullmaktService fullmaktService;

    public List<Person> makeRelasjoner(RsPersonBestillingKriteriumRequest request, Person hovedperson) {

        if (nonNull(request.getRelasjoner().getPartner())) {
            request.getRelasjoner().getPartnere().add(request.getRelasjoner().getPartner());
        }

        boolean isSivilstandUpdate = isSivilstandUpdateForrigePartner(hovedperson, request.getRelasjoner());
        if (isSivilstandUpdate) {
            request.getRelasjoner().getPartnere().remove(0); // denne "partner" innehold oppdatering av sivilstand på forrige
        }

        List<Person> partnere = request.getRelasjoner().getPartnere().stream()
                .map(reqPart -> isNotBlank(reqPart.getIdent()) ?
                        personRepository.findByIdent(reqPart.getIdent()) :
                        opprettPersonerOgSjekkMiljoeService
                                .createNyeIdenter(ExtractOpprettKriterier.extractPartner(singletonList(reqPart),
                                        request.getHarMellomnavn())).get(0))
                .collect(Collectors.toList());

        List<Person> barn = request.getRelasjoner().getBarn().stream()
                .map(reqBarn -> isNotBlank(reqBarn.getIdent()) ?
                        personRepository.findByIdent(reqBarn.getIdent()) :
                        opprettPersonerOgSjekkMiljoeService
                                .createNyeIdenter(ExtractOpprettKriterier.extractBarn(singletonList(reqBarn),
                                        request.getHarMellomnavn())).get(0))
                .collect(Collectors.toList());

        setIdenthistorikkPaaPersoner(request, singletonList(hovedperson), partnere, barn);
        setRelasjonerPaaPersoner(singletonList(hovedperson), partnere, barn, request);
        setSivilstandHistorikkPaaPersoner(request, hovedperson, partnere);
        vergemaalService.opprettVerge(request, List.of(hovedperson));
        fullmaktService.opprettFullmakt(request, List.of(hovedperson));

        List<Person> tpsfPersoner = relasjonExtractOpprettKriterier
                .addExtendedKriterumValuesToPerson(request, singletonList(hovedperson), partnere, barn);

        List<Person> lagredePersoner = savePersonBulk.execute(tpsfPersoner);

        return sortWithBestiltPersonFoerstIListe(lagredePersoner, hovedperson.getIdent());
    }

    private boolean isSivilstandUpdateForrigePartner(Person person, RsSimpleRelasjoner request) {

        if (request.getPartnere().isEmpty() || isNotBlank(request.getPartnere().get(0).getIdent())) {
            return false;
        }

        Relasjon partnerRelasjon = person.getRelasjoner().stream()
                .filter(relasjon -> PARTNER.getName().equals(relasjon.getRelasjonTypeNavn()))
                .findFirst().orElse(null);

        if (nonNull(partnerRelasjon) &&
                !partnerRelasjon.getPersonRelasjonMed().getSivilstander().isEmpty() &&
                partnerRelasjon.getPersonRelasjonMed().getSivilstander().get(0).isSivilstandGift() &&
                !request.getPartnere().get(0).getSivilstander().isEmpty()) {

            if (request.getPartnere().get(0).getSivilstander().get(0).isSivilstandGift()) {
                throw new TpsfFunctionalException(messageProvider.get("endre.person.relasjon.partner.sivilstand.gift.igjen"));

            } else {

                request.getPartnere().get(0).getSivilstander().forEach(sivilstand ->
                        setSivilstandHistory(person, partnerRelasjon.getPersonRelasjonMed(),
                                sivilstand.getSivilstand(), sivilstand.getSivilstandRegdato())
                );
                return true;
            }
        }
        return false;
    }

    private static void setSivilstandHistorikkPaaPersoner(RsPersonBestillingKriteriumRequest request, Person person, List<Person> partnere) {

        Iterator<Person> partIterator = partnere.iterator();
        request.getRelasjoner().getPartnere().forEach(parterRequest -> {

            Person partner = partIterator.next();
            if (!parterRequest.getSivilstander().isEmpty()) {
                parterRequest.getSivilstander().forEach(sivilstand ->
                        setSivilstandHistory(person, partner, sivilstand.getSivilstand(), sivilstand.getSivilstandRegdato()));
            }
        });
    }
}