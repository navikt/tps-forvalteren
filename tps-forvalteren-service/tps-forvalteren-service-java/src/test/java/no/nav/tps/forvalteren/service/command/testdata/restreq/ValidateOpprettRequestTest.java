package no.nav.tps.forvalteren.service.command.testdata.restreq;

import static java.util.Arrays.asList;
import static java.util.Collections.singletonList;
import static no.nav.tps.forvalteren.domain.rs.skd.IdentType.BOST;
import static no.nav.tps.forvalteren.domain.rs.skd.IdentType.DNR;
import static no.nav.tps.forvalteren.domain.rs.skd.IdentType.FNR;
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
import no.nav.tps.forvalteren.domain.rs.RsSimplePersonRequest;
import no.nav.tps.forvalteren.domain.rs.RsSimpleRelasjoner;
import no.nav.tps.forvalteren.domain.rs.dolly.RsPersonBestillingKriteriumRequest;
import no.nav.tps.forvalteren.service.command.exceptions.TpsfFunctionalException;

@RunWith(MockitoJUnitRunner.class)
public class ValidateOpprettRequestTest {

    private static final LocalDateTime UGYLDIG_FOEDT_FOER_DATO = LocalDateTime.of(1899, 12, 31, 23, 59);
    private static final LocalDateTime UGYLDIG_FOEDT_ETTER_DATO = LocalDateTime.of(2040, 1, 1, 0, 0);
    private static final String TEKST_FOEDT_ETTER = "Dato født etter kan ikke være etter dagens dato.";
    private static final String TEKST_FOEDT_FOER = "Dato født før kan ikke være før år 1900.";
    private static final String TEKST_UGYLDIG_DATO_INTERVALL = "Ugyldig datointervall er oppgitt.";
    private static final String TEKST_UGYLDIG_KJOENN = "Ugyldig kjønn, en av U, K eller M forventet.";
    private static final String TEKST_UGYLDIG_ANTALL = "Enten antall eller eksisterende identer må spesifieseres.";
    private static final String TEKST_UGYLDIG_IDENTTYPE = "Ugyldig identtype for utvandring. Kun personer med FNR kan utvandre.";

    @Mock
    private MessageProvider messageProvider;

    @InjectMocks
    private ValidateOpprettRequest validateOpprettRequest;

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Before
    public void setup() {
        when(messageProvider.get("bestilling.input.validation.dato.foedt.etter")).thenReturn(TEKST_FOEDT_ETTER);
        when(messageProvider.get("bestilling.input.validation.dato.foedt.foer")).thenReturn(TEKST_FOEDT_FOER);
        when(messageProvider.get("bestilling.input.validation.ugyldig.intervall")).thenReturn(TEKST_UGYLDIG_DATO_INTERVALL);
        when(messageProvider.get("bestilling.input.validation.ugyldig.kjoenn")).thenReturn(TEKST_UGYLDIG_KJOENN);
        when(messageProvider.get("bestilling.input.validation.ugyldig.antall")).thenReturn(TEKST_UGYLDIG_ANTALL);
        when(messageProvider.get("bestilling.input.validation.ugyldig.identtype")).thenReturn(TEKST_UGYLDIG_IDENTTYPE);
    }

    @Test(expected = Test.None.class)
    public void validateEmptyRequest() {
        RsPersonBestillingKriteriumRequest request = new RsPersonBestillingKriteriumRequest();
        request.setAntall(1);
        validateOpprettRequest.validate(request);
    }

    @Test
    public void validateHovedpersonFoedtEtterUgyldigDato() {

        expectedException.expect(TpsfFunctionalException.class);
        expectedException.expectMessage(TEKST_FOEDT_ETTER);

        RsPersonBestillingKriteriumRequest request = new RsPersonBestillingKriteriumRequest();
        request.setAntall(1);
        request.setFoedtEtter(UGYLDIG_FOEDT_ETTER_DATO);

        validateOpprettRequest.validate(request);
    }

    @Test
    public void validateHovedpersonFoedtFoerUgyldigDato() {

        expectedException.expect(TpsfFunctionalException.class);
        expectedException.expectMessage(TEKST_FOEDT_FOER);

        RsPersonBestillingKriteriumRequest request = new RsPersonBestillingKriteriumRequest();
        request.setAntall(1);
        request.setFoedtFoer(UGYLDIG_FOEDT_FOER_DATO);

        validateOpprettRequest.validate(request);
    }

    @Test
    public void validateHovedpersonUgyldigKjoenn() {

        expectedException.expect(TpsfFunctionalException.class);
        expectedException.expectMessage(TEKST_UGYLDIG_KJOENN);

        RsPersonBestillingKriteriumRequest request = new RsPersonBestillingKriteriumRequest();
        request.setAntall(1);
        request.setKjonn("P");

        validateOpprettRequest.validate(request);
    }

    @Test
    public void validateHovedpersonUgyldigDatoIntervall() {

        expectedException.expect(TpsfFunctionalException.class);
        expectedException.expectMessage(TEKST_UGYLDIG_DATO_INTERVALL);

        RsPersonBestillingKriteriumRequest request = new RsPersonBestillingKriteriumRequest();
        request.setAntall(1);
        request.setFoedtEtter(LocalDateTime.of(2000, 1, 1, 0, 0));
        request.setFoedtFoer(LocalDateTime.of(1990, 1, 1, 0, 0));

        validateOpprettRequest.validate(request);
    }

    @Test
    public void validatePartnerFoedtEtterUgyldigDato() {

        expectedException.expect(TpsfFunctionalException.class);
        expectedException.expectMessage(TEKST_FOEDT_ETTER);

        RsPersonBestillingKriteriumRequest request = new RsPersonBestillingKriteriumRequest();
        request.setAntall(1);
        request.setRelasjoner(RsSimpleRelasjoner.builder()
                .partner(RsSimplePersonRequest.builder()
                        .foedtEtter(UGYLDIG_FOEDT_ETTER_DATO)
                        .build())
                .build());

        validateOpprettRequest.validate(request);
    }

    @Test
    public void validatePartnerFoedtFoerUgyldigDato() {

        expectedException.expect(TpsfFunctionalException.class);
        expectedException.expectMessage(TEKST_FOEDT_FOER);

        RsPersonBestillingKriteriumRequest request = new RsPersonBestillingKriteriumRequest();
        request.setAntall(1);
        request.setRelasjoner(RsSimpleRelasjoner.builder()
                .partner(RsSimplePersonRequest.builder()
                        .foedtFoer(UGYLDIG_FOEDT_FOER_DATO)
                        .build())
                .build());

        validateOpprettRequest.validate(request);
    }

    @Test
    public void validatePartnerUgyldigDatoIntervall() {

        expectedException.expect(TpsfFunctionalException.class);
        expectedException.expectMessage(TEKST_UGYLDIG_DATO_INTERVALL);

        RsPersonBestillingKriteriumRequest request = new RsPersonBestillingKriteriumRequest();
        request.setAntall(1);
        request.setRelasjoner(RsSimpleRelasjoner.builder()
                .partner(RsSimplePersonRequest.builder()
                        .foedtEtter(LocalDateTime.of(2000, 1, 1, 0, 0))
                        .foedtFoer(LocalDateTime.of(1990, 1, 1, 0, 0))
                        .build())
                .build());

        validateOpprettRequest.validate(request);
    }

    @Test
    public void validatePartnerUgyldigKjoenn() {

        expectedException.expect(TpsfFunctionalException.class);
        expectedException.expectMessage(TEKST_UGYLDIG_KJOENN);

        RsPersonBestillingKriteriumRequest request = new RsPersonBestillingKriteriumRequest();
        request.setAntall(1);
        request.setRelasjoner(RsSimpleRelasjoner.builder()
                .partner(RsSimplePersonRequest.builder()
                        .kjonn("T")
                        .build())
                .build());

        validateOpprettRequest.validate(request);
    }

    @Test
    public void validateBarnFoedtEtterUgyldigDato() {

        expectedException.expect(TpsfFunctionalException.class);
        expectedException.expectMessage(TEKST_FOEDT_ETTER);

        RsPersonBestillingKriteriumRequest request = new RsPersonBestillingKriteriumRequest();
        request.setAntall(1);
        request.setRelasjoner(RsSimpleRelasjoner.builder()
                .barn(asList(
                        RsSimplePersonRequest.builder()
                                .foedtEtter(LocalDateTime.of(2020, 1, 1, 0, 0))
                                .build(),
                        RsSimplePersonRequest.builder()
                                .foedtEtter(UGYLDIG_FOEDT_ETTER_DATO)
                                .build())
                )
                .build());

        validateOpprettRequest.validate(request);
    }

    @Test
    public void validateBarnFoedtFoerUgyldigDato() {

        expectedException.expect(TpsfFunctionalException.class);
        expectedException.expectMessage(TEKST_FOEDT_FOER);

        RsPersonBestillingKriteriumRequest request = new RsPersonBestillingKriteriumRequest();
        request.setAntall(1);
        request.setRelasjoner(RsSimpleRelasjoner.builder()
                .barn(asList(
                        RsSimplePersonRequest.builder()
                                .foedtFoer(LocalDateTime.of(2020, 1, 1, 0, 0))
                                .build(),
                        RsSimplePersonRequest.builder()
                                .foedtFoer(UGYLDIG_FOEDT_FOER_DATO)
                                .build())
                )
                .build());

        validateOpprettRequest.validate(request);
    }

    @Test
    public void validateBarnUgyldigDatoIntervall() {

        expectedException.expect(TpsfFunctionalException.class);
        expectedException.expectMessage(TEKST_UGYLDIG_DATO_INTERVALL);

        RsPersonBestillingKriteriumRequest request = new RsPersonBestillingKriteriumRequest();
        request.setAntall(1);
        request.setRelasjoner(RsSimpleRelasjoner.builder()
                .barn(asList(
                        RsSimplePersonRequest.builder()
                                .foedtFoer(LocalDateTime.of(1980, 1, 1, 0, 0))
                                .build(),
                        RsSimplePersonRequest.builder()
                                .foedtEtter(LocalDateTime.of(2000, 1, 1, 0, 0))
                                .foedtFoer(LocalDateTime.of(1990, 1, 1, 0, 0))
                                .build())
                )
                .build());
        validateOpprettRequest.validate(request);
    }

    @Test
    public void validateBarnUgyldigKjoenn() {

        expectedException.expect(TpsfFunctionalException.class);
        expectedException.expectMessage(TEKST_UGYLDIG_KJOENN);

        RsPersonBestillingKriteriumRequest request = new RsPersonBestillingKriteriumRequest();
        request.setAntall(1);
        request.setRelasjoner(RsSimpleRelasjoner.builder()
                .barn(asList(
                        RsSimplePersonRequest.builder()
                                .kjonn("L")
                                .build(),
                        RsSimplePersonRequest.builder()
                                .kjonn("V")
                                .build())
                )
                .build());

        validateOpprettRequest.validate(request);
    }

    @Test(expected = Test.None.class)
    public void validateBarnGyldigKjoenn() {

        RsPersonBestillingKriteriumRequest request = new RsPersonBestillingKriteriumRequest();
        request.setAntall(1);
        request.setRelasjoner(RsSimpleRelasjoner.builder()
                .barn(asList(
                        RsSimplePersonRequest.builder()
                                .kjonn("U")
                                .build(),
                        RsSimplePersonRequest.builder()
                                .kjonn("K")
                                .build(),
                        RsSimplePersonRequest.builder()
                                .kjonn("M")
                                .build())
                )
                .build());
        validateOpprettRequest.validate(request);
    }

    @Test
    public void validateUgyldigAntall() {

        expectedException.expect(TpsfFunctionalException.class);
        expectedException.expectMessage(TEKST_UGYLDIG_ANTALL);

        validateOpprettRequest.validate(new RsPersonBestillingKriteriumRequest());
    }

    @Test
    public void validateUgyldigIdenttypeWithUtvandretTilLand_BOST() {

        expectedException.expect(TpsfFunctionalException.class);
        expectedException.expectMessage(TEKST_UGYLDIG_IDENTTYPE);

        RsPersonBestillingKriteriumRequest request = new RsPersonBestillingKriteriumRequest();
        request.setAntall(1);
        request.setIdenttype(BOST.name());
        request.setUtvandretTilLand("AUS");

        validateOpprettRequest.validate(request);
    }

    @Test
    public void validateUgyldigIdenttypeWithUtvandretTilLand_DNR() {

        expectedException.expect(TpsfFunctionalException.class);
        expectedException.expectMessage(TEKST_UGYLDIG_IDENTTYPE);

        RsPersonBestillingKriteriumRequest request = new RsPersonBestillingKriteriumRequest();
        request.setAntall(1);
        request.setIdenttype(DNR.name());
        request.setUtvandretTilLand("GER");

        validateOpprettRequest.validate(request);
    }

    @Test
    public void validateUgyldigIdenttypeWithUtvandretTilLand_Barn_BOST() {

        expectedException.expect(TpsfFunctionalException.class);
        expectedException.expectMessage(TEKST_UGYLDIG_IDENTTYPE);

        RsPersonBestillingKriteriumRequest request = new RsPersonBestillingKriteriumRequest();
        request.setAntall(1);
        request.setIdenttype(FNR.name());
        request.setUtvandretTilLand("FIN");
        request.setRelasjoner(RsSimpleRelasjoner.builder()
                .barn(singletonList(RsSimplePersonRequest.builder()
                        .identtype("BOST")
                        .utvandretTilLand("BUL")
                        .build()
                )).build());

        validateOpprettRequest.validate(request);
    }

    @Test
    public void validateUgyldigIdenttypeWithUtvandretTilLand_Partner_DNR() {

        expectedException.expect(TpsfFunctionalException.class);
        expectedException.expectMessage(TEKST_UGYLDIG_IDENTTYPE);

        RsPersonBestillingKriteriumRequest request = new RsPersonBestillingKriteriumRequest();
        request.setAntall(1);
        request.setUtvandretTilLand("CAN");
        request.setRelasjoner(RsSimpleRelasjoner.builder()
                .partner(RsSimplePersonRequest.builder()
                        .identtype("DNR")
                        .utvandretTilLand("AUS")
                        .build()
                ).build());

        validateOpprettRequest.validate(request);
    }
}