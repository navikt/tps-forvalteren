package no.nav.tps.forvalteren.service.command.testdata;

import static java.util.Collections.singletonList;
import static no.nav.tps.forvalteren.domain.service.Sivilstand.ENKE_ELLER_ENKEMANN;
import static no.nav.tps.forvalteren.domain.service.Sivilstand.GIFT;
import static no.nav.tps.forvalteren.domain.service.Sivilstand.REGISTRERT_PARTNER;
import static no.nav.tps.forvalteren.domain.service.Sivilstand.SEPARERT;
import static no.nav.tps.forvalteren.domain.service.Sivilstand.SKILT;
import static no.nav.tps.forvalteren.domain.service.Sivilstand.UGIFT;
import static org.assertj.core.util.Lists.newArrayList;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import no.nav.tps.forvalteren.common.java.message.MessageProvider;
import no.nav.tps.forvalteren.domain.rs.RsPartnerRequest;
import no.nav.tps.forvalteren.domain.rs.RsSivilstand;
import no.nav.tps.forvalteren.domain.rs.dolly.RsPersonBestillingKriteriumRequest;
import no.nav.tps.forvalteren.service.command.exceptions.TpsfFunctionalException;
import no.nav.tps.forvalteren.service.command.testdata.restreq.ValidateSivilstandService;

@RunWith(MockitoJUnitRunner.class)
public class ValidateSivilstandServiceTest {

    private static final String UGYLDIG = "TULL";

    @Mock
    private MessageProvider messageProvider;

    @InjectMocks
    private ValidateSivilstandService validateSivilstandService;

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Before
    public void setup() {

        when(messageProvider.get("bestilling.input.validation.sivilstand.partnere"))
                .thenReturn("Kun EN av relasjonene \"partner\" eller \"partnere\" benyttes ved bestilling");
        when(messageProvider.get("bestilling.input.validation.sivilstand.required"))
                .thenReturn("Sivilstand og dato-sivilstand må være angitt på historikk.");
        when(messageProvider.get("bestilling.input.validation.sivilstand.partner"))
                .thenReturn("Partner er ikke støttet etter 1. januar 2009.");
        when(messageProvider.get("bestilling.input.validation.sivilstand.ugift"))
                .thenReturn("Sivilstand kan ikke være UGIFT etter annen sivilstand.");
        when(messageProvider.get("bestilling.input.validation.sivilstand.separert"))
                .thenReturn("Sivilstand SEPARERT kan ikke komme etter SKILT.");
        when(messageProvider.get("bestilling.input.validation.sivilstand.samme"))
                .thenReturn("Samme sivilstand er ikke lov for påfølgende innslag.");
        when(messageProvider.get("bestilling.input.validation.sivilstand.enke.ugift"))
                .thenReturn("Sivilstand ENKE_ELLER_ENKEMANN er ugyldig uten å ha vært gift.");
        when(messageProvider.get("bestilling.input.validation.sivilstand.skilt.ugift"))
                .thenReturn("Sivilstand SKILT er ugyldig uten å ha vært gift.");
        when(messageProvider.get("bestilling.input.validation.sivilstand.separert.ugift"))
                .thenReturn("Sivilstand SEPARERT er ugyldig uten å ha vært gift.");
        when(messageProvider.get("bestilling.input.validation.sivilstand.samboer"))
                .thenReturn("Samboer er ikke støttet som sivilstand på historikk.");
        when(messageProvider.get("bestilling.input.validation.sivilstand.ugyldig-kode", UGYLDIG))
                .thenReturn("Sivilstand '" + UGYLDIG + "' er ugyldig.");
        when(messageProvider.get("bestilling.input.validation.sivilstand.datoer"))
                .thenReturn("Dato-sivilstand kan ikke overlappe forrige dato-sivilstand -- må minst være to dager i mellom.");
        when(messageProvider.get("bestilling.input.validation.sivilstand.regdato.required.multiple"))
                .thenReturn("Ved flere partnere må sivilstand regdato oppgis per partner.");
        when(messageProvider.get("bestilling.input.validation.sivilstand.regdato.multiple"))
                .thenReturn("Ved flere sivilstander må regdato oppgis per sivilstand.");
    }

    @Test
    public void validate_throwErrorWhenPartnerAndPartnereBothSet() {

        expectedException.expect(TpsfFunctionalException.class);
        expectedException.expectMessage("Kun EN av relasjonene \"partner\" eller \"partnere\" benyttes ved bestilling");

        RsPartnerRequest partnerRequest = new RsPartnerRequest();
        partnerRequest.setSivilstander(singletonList(RsSivilstand.builder().sivilstand(REGISTRERT_PARTNER.getKodeverkskode())
                .sivilstandRegdato(LocalDateTime.of(2016, 1, 1, 0, 0))
                .build()));

        RsPartnerRequest partnerRequest1 = new RsPartnerRequest();
        partnerRequest1.setSivilstander(singletonList(RsSivilstand.builder().sivilstand(REGISTRERT_PARTNER.getKodeverkskode())
                .sivilstandRegdato(LocalDateTime.of(2016, 1, 1, 0, 0))
                .build()));

        RsPersonBestillingKriteriumRequest request = new RsPersonBestillingKriteriumRequest();
        request.getRelasjoner().getPartnere().add(partnerRequest);
        request.getRelasjoner().setPartner(partnerRequest1);

        validateSivilstandService.validateStatus(request);
    }

    @Test
    public void validate_throwErrorWhenRegistretPartnerOccursAfterYear2009() {

        expectedException.expect(TpsfFunctionalException.class);
        expectedException.expectMessage("Partner er ikke støttet etter 1. januar 2009.");

        RsPartnerRequest partnerRequest = new RsPartnerRequest();
        partnerRequest.setSivilstander(singletonList(RsSivilstand.builder().sivilstand(REGISTRERT_PARTNER.getKodeverkskode())
                .sivilstandRegdato(LocalDateTime.of(2016, 1, 1, 0, 0))
                .build()));

        RsPersonBestillingKriteriumRequest request = new RsPersonBestillingKriteriumRequest();
        request.getRelasjoner().getPartnere().add(partnerRequest);

        validateSivilstandService.validateStatus(request);
    }

    @Test
    public void validate_throwErrorWhenSivilstandUgiftSucceedsSivilstandGift() {

        expectedException.expect(TpsfFunctionalException.class);
        expectedException.expectMessage("Sivilstand kan ikke være UGIFT etter annen sivilstand.");

        RsPartnerRequest partnerRequest = new RsPartnerRequest();
        partnerRequest.setSivilstander(newArrayList(RsSivilstand.builder().sivilstand(UGIFT.getKodeverkskode())
                        .sivilstandRegdato(LocalDateTime.of(2018, 1, 1, 0, 0))
                        .build(),
                RsSivilstand.builder()
                        .sivilstand(GIFT.getKodeverkskode())
                        .sivilstandRegdato(LocalDateTime.of(2016, 1, 1, 0, 0))
                        .build()
        ));

        RsPersonBestillingKriteriumRequest request = new RsPersonBestillingKriteriumRequest();
        request.getRelasjoner().getPartnere().add(partnerRequest);

        validateSivilstandService.validateStatus(request);
    }

    @Test
    public void validate_throwErrorWhenSeparertAfterSkilt() {

        expectedException.expect(TpsfFunctionalException.class);
        expectedException.expectMessage("Sivilstand SEPARERT kan ikke komme etter SKILT.");

        RsPartnerRequest partnerRequest = new RsPartnerRequest();
        partnerRequest.setSivilstander(newArrayList(RsSivilstand.builder().sivilstand(GIFT.getKodeverkskode())
                        .sivilstandRegdato(LocalDateTime.of(2019, 1, 1, 0, 0))
                        .build(),
                RsSivilstand.builder()
                        .sivilstand(SEPARERT.getKodeverkskode())
                        .sivilstandRegdato(LocalDateTime.of(2017, 1, 1, 0, 0))
                        .build(),
                RsSivilstand.builder()
                        .sivilstand(SKILT.getKodeverkskode())
                        .sivilstandRegdato(LocalDateTime.of(2014, 1, 1, 0, 0))
                        .build()
        ));
        RsPersonBestillingKriteriumRequest request = new RsPersonBestillingKriteriumRequest();
        request.getRelasjoner().getPartnere().add(partnerRequest);

        validateSivilstandService.validateStatus(request);
    }

    @Test
    public void validate_throwErrorWhenEnkeWithoutEverBeingMarried() {

        expectedException.expect(TpsfFunctionalException.class);
        expectedException.expectMessage("Sivilstand ENKE_ELLER_ENKEMANN er ugyldig uten å ha vært gift.");

        RsPartnerRequest partnerRequest = new RsPartnerRequest();
        partnerRequest.setSivilstander(newArrayList(
                RsSivilstand.builder().sivilstand(ENKE_ELLER_ENKEMANN.getKodeverkskode())
                        .sivilstandRegdato(LocalDateTime.of(2018, 1, 1, 0, 0))
                        .build()
        ));

        RsPersonBestillingKriteriumRequest request = new RsPersonBestillingKriteriumRequest();
        request.getRelasjoner().getPartnere().add(partnerRequest);

        validateSivilstandService.validateStatus(request);
    }

    @Test
    public void validate_throwErrorWhenSkiltWithoutEverBeingMarried() {

        expectedException.expect(TpsfFunctionalException.class);
        expectedException.expectMessage("Sivilstand SKILT er ugyldig uten å ha vært gift.");

        RsPartnerRequest partnerRequest = new RsPartnerRequest();
        partnerRequest.setSivilstander(newArrayList(
                RsSivilstand.builder().sivilstand(SKILT.getKodeverkskode())
                        .sivilstandRegdato(LocalDateTime.of(2018, 1, 1, 0, 0))
                        .build()
        ));

        RsPersonBestillingKriteriumRequest request = new RsPersonBestillingKriteriumRequest();
        request.getRelasjoner().getPartnere().add(partnerRequest);

        validateSivilstandService.validateStatus(request);
    }

    @Test
    public void validate_throwErrorWhenSeparertWithoutEverBeingMarried() {

        expectedException.expect(TpsfFunctionalException.class);
        expectedException.expectMessage("Sivilstand SEPARERT er ugyldig uten å ha vært gift.");

        RsPartnerRequest partnerRequest = new RsPartnerRequest();
        partnerRequest.setSivilstander(newArrayList(
                RsSivilstand.builder().sivilstand(SEPARERT.getKodeverkskode())
                        .sivilstandRegdato(LocalDateTime.of(2018, 1, 1, 0, 0))
                        .build()
        ));

        RsPersonBestillingKriteriumRequest request = new RsPersonBestillingKriteriumRequest();
        request.getRelasjoner().getPartnere().add(partnerRequest);

        validateSivilstandService.validateStatus(request);
    }

    @Test
    public void validate_throwErrorWhenTwoIdenticalSivilstandFollows() {

        expectedException.expect(TpsfFunctionalException.class);
        expectedException.expectMessage("Samme sivilstand er ikke lov for påfølgende innslag.");

        RsPartnerRequest partnerRequest = new RsPartnerRequest();
        partnerRequest.setSivilstander(newArrayList(
                RsSivilstand.builder().sivilstand(GIFT.getKodeverkskode())
                        .sivilstandRegdato(LocalDateTime.of(2018, 1, 1, 0, 0))
                        .build(),
                RsSivilstand.builder().sivilstand(GIFT.getKodeverkskode())
                        .sivilstandRegdato(LocalDateTime.of(2016, 1, 1, 0, 0))
                        .build()
        ));

        RsPersonBestillingKriteriumRequest request = new RsPersonBestillingKriteriumRequest();
        request.getRelasjoner().getPartnere().add(partnerRequest);

        validateSivilstandService.validateStatus(request);
    }

    @Test
    public void validate_throwErrorWhenSivilstandUnknown() {

        expectedException.expect(TpsfFunctionalException.class);
        expectedException.expectMessage("Sivilstand '" + UGYLDIG + "' er ugyldig.");

        RsPartnerRequest partnerRequest = new RsPartnerRequest();
        partnerRequest.setSivilstander(newArrayList(
                RsSivilstand.builder().sivilstand(UGYLDIG)
                        .sivilstandRegdato(LocalDateTime.of(2016, 1, 1, 0, 0))
                        .build()
        ));

        RsPersonBestillingKriteriumRequest request = new RsPersonBestillingKriteriumRequest();
        request.getRelasjoner().getPartnere().add(partnerRequest);

        validateSivilstandService.validateStatus(request);
    }

    @Test
    public void validate_throwErrorWhenDatesOverlap() {

        expectedException.expect(TpsfFunctionalException.class);
        expectedException.expectMessage("Dato-sivilstand kan ikke overlappe forrige dato-sivilstand -- må minst være to dager i mellom.");

        RsPartnerRequest partnerRequest = new RsPartnerRequest();
        partnerRequest.setSivilstander(newArrayList(
                RsSivilstand.builder().sivilstand(GIFT.getKodeverkskode())
                        .sivilstandRegdato(LocalDateTime.of(2019, 1, 1, 0, 0))
                        .build(),
                RsSivilstand.builder().sivilstand(SKILT.getKodeverkskode())
                        .sivilstandRegdato(LocalDateTime.of(2019, 1, 2, 0, 0))
                        .build()
        ));

        RsPersonBestillingKriteriumRequest request = new RsPersonBestillingKriteriumRequest();
        request.getRelasjoner().getPartnere().add(partnerRequest);

        validateSivilstandService.validateStatus(request);
    }

    @Test
    public void validate_throwErrorWhenDatesOverlapMultiplePartners() {

        expectedException.expect(TpsfFunctionalException.class);
        expectedException.expectMessage("Dato-sivilstand kan ikke overlappe forrige dato-sivilstand -- må minst være to dager i mellom.");

        RsPartnerRequest partner1Request = new RsPartnerRequest();
        partner1Request.setSivilstander(newArrayList(
                RsSivilstand.builder().sivilstand(SKILT.getKodeverkskode())
                        .sivilstandRegdato(LocalDateTime.of(2018, 1, 1, 0, 0))
                        .build(),
                RsSivilstand.builder().sivilstand(GIFT.getKodeverkskode())
                        .sivilstandRegdato(LocalDateTime.of(2019, 1, 2, 0, 0))
                        .build()
        ));

        RsPartnerRequest partner2Request = new RsPartnerRequest();
        partner2Request.setSivilstander(newArrayList(
                RsSivilstand.builder().sivilstand(SKILT.getKodeverkskode())
                        .sivilstandRegdato(LocalDateTime.of(2017, 1, 1, 0, 0))
                        .build(),
                RsSivilstand.builder().sivilstand(GIFT.getKodeverkskode())
                        .sivilstandRegdato(LocalDateTime.of(2019, 1, 2, 0, 0))
                        .build()
        ));


        RsPersonBestillingKriteriumRequest request = new RsPersonBestillingKriteriumRequest();
        request.getRelasjoner().getPartnere().addAll(newArrayList(partner1Request, partner2Request));

        validateSivilstandService.validateStatus(request);
    }



    @Test
    public void validate_throwErrorWhenDateIsMisssingSamePartner() {

        expectedException.expect(TpsfFunctionalException.class);
        expectedException.expectMessage("Ved flere sivilstander må regdato oppgis per sivilstand.");

        RsPartnerRequest partnerRequest = new RsPartnerRequest();
        partnerRequest.setSivilstander(newArrayList(
                RsSivilstand.builder().sivilstand(GIFT.getKodeverkskode())
                        .sivilstandRegdato(LocalDateTime.of(2019, 1, 1, 0, 0))
                        .build(),
                RsSivilstand.builder().sivilstand(SKILT.getKodeverkskode())
                        .build()
        ));

        RsPersonBestillingKriteriumRequest request = new RsPersonBestillingKriteriumRequest();
        request.getRelasjoner().getPartnere().add(partnerRequest);

        validateSivilstandService.validateStatus(request);
    }

    @Test
    public void validate_throwErrorWhenDateIsMisssingNewPartner() {

        expectedException.expect(TpsfFunctionalException.class);
        expectedException.expectMessage("Ved flere partnere må sivilstand regdato oppgis per partner.");

        RsPartnerRequest partnerRequest = new RsPartnerRequest();
        partnerRequest.setSivilstander(singletonList(
                RsSivilstand.builder().sivilstand(GIFT.getKodeverkskode())
                        .sivilstandRegdato(LocalDateTime.of(2019, 1, 1, 0, 0))
                        .build()
        ));
        RsPartnerRequest partner2Request = new RsPartnerRequest();
        partner2Request.setSivilstander(singletonList(
                RsSivilstand.builder().sivilstand(GIFT.getKodeverkskode())
                        .build()
        ));

        RsPersonBestillingKriteriumRequest request = new RsPersonBestillingKriteriumRequest();
        request.getRelasjoner().getPartnere().addAll(newArrayList(partnerRequest, partner2Request));

        validateSivilstandService.validateStatus(request);
    }
}