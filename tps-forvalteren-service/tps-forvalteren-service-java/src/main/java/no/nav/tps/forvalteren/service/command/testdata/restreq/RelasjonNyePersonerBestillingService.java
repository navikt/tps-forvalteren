package no.nav.tps.forvalteren.service.command.testdata.restreq;

import static java.util.Collections.singletonList;
import static java.util.Objects.nonNull;
import static no.nav.tps.forvalteren.domain.service.RelasjonType.PARTNER;

import java.util.ArrayList;
import java.util.List;
import javax.transaction.Transactional;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import no.nav.tps.forvalteren.common.java.message.MessageProvider;
import no.nav.tps.forvalteren.domain.jpa.Person;
import no.nav.tps.forvalteren.domain.jpa.Relasjon;
import no.nav.tps.forvalteren.domain.rs.RsPersonKriteriumRequest;
import no.nav.tps.forvalteren.domain.rs.RsSimpleRelasjoner;
import no.nav.tps.forvalteren.domain.rs.dolly.RsPersonBestillingKriteriumRequest;
import no.nav.tps.forvalteren.service.command.exceptions.TpsfFunctionalException;
import no.nav.tps.forvalteren.service.command.testdata.SavePersonBulk;
import no.nav.tps.forvalteren.service.command.testdata.opprett.OpprettPersonerOgSjekkMiljoeService;

/**
 * Denne klasse benyttes for å legge til nye personer <br>
 * nye partner(e) og nye barn eksisterer ikke fra før og opprettes ved denne bestillingen
 */
@Service
@Transactional
@RequiredArgsConstructor
public class RelasjonNyePersonerBestillingService extends PersonerBestillingService {

    private final SavePersonBulk savePersonBulk;
    private final OpprettPersonerOgSjekkMiljoeService opprettPersonerOgSjekkMiljoeService;
    private final RelasjonExtractOpprettKriterier relasjonExtractOpprettKriterier;
    private final MessageProvider messageProvider;

    public List<Person> makeRelasjoner(RsPersonBestillingKriteriumRequest request, Person hovedperson) {

        if (nonNull(request.getRelasjoner().getPartner())) {
            request.getRelasjoner().getPartnere().add(request.getRelasjoner().getPartner());
        }

        boolean isSivilstandUpdate = isSivilstandUpdateForrigePartner(hovedperson, request.getRelasjoner());
        if (isSivilstandUpdate) {
            request.getRelasjoner().getPartnere().remove(0); // denne "partner" innehold oppdatering av sivilstand på forrige
        }

        List<Person> partnere = new ArrayList();
        if (!request.getRelasjoner().getPartnere().isEmpty()) {
            RsPersonKriteriumRequest kriteriePartner = ExtractOpprettKriterier.extractPartner(request);
            partnere = savePersonBulk.execute(opprettPersonerOgSjekkMiljoeService.createNyeIdenter(kriteriePartner));
        }

        List<Person> barn = new ArrayList();
        if (!request.getRelasjoner().getBarn().isEmpty()) {
            RsPersonKriteriumRequest kriterieBarn = ExtractOpprettKriterier.extractBarn(request);
            barn = savePersonBulk.execute(opprettPersonerOgSjekkMiljoeService.createNyeIdenter(kriterieBarn));
        }

        setIdenthistorikkPaaPersoner(request, singletonList(hovedperson), partnere, barn);
        setRelasjonerPaaPersoner(singletonList(hovedperson), partnere, barn, request);
        setSivilstandHistorikkPaaPersoner(request, singletonList(hovedperson));

        List<Person> tpsfPersoner = relasjonExtractOpprettKriterier
                .addExtendedKriterumValuesToPerson(request, singletonList(hovedperson), partnere, barn);

        List<Person> lagredePersoner = savePersonBulk.execute(tpsfPersoner);

        return sortWithBestiltPersonFoerstIListe(lagredePersoner, hovedperson.getIdent());
    }

    private boolean isSivilstandUpdateForrigePartner(Person person, RsSimpleRelasjoner request) {

        if (request.getPartnere().isEmpty()) {
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
                        setSivilstandHistory(person, partnerRelasjon,
                                sivilstand.getSivilstand(), sivilstand.getSivilstandRegdato())
                );
                return true;
            }
        }
        return false;
    }
}