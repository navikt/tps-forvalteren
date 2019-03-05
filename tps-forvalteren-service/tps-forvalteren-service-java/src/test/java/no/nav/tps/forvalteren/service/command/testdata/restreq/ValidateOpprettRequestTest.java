package no.nav.tps.forvalteren.service.command.testdata.restreq;

import static java.util.Arrays.asList;
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
    }

    @Test(expected = Test.None.class)
    public void validateEmptyRequest() {

        validateOpprettRequest.validate(RsPersonBestillingKriteriumRequest.builder().antall(1).build());
    }

    @Test
    public void validateHovedpersonFoedtEtterUgyldigDato() {

        expectedException.expect(TpsfFunctionalException.class);
        expectedException.expectMessage(TEKST_FOEDT_ETTER);

        validateOpprettRequest.validate(RsPersonBestillingKriteriumRequest.builder()
                .antall(1)
                .foedtEtter(UGYLDIG_FOEDT_ETTER_DATO)
                .build());
    }

    @Test
    public void validateHovedpersonFoedtFoerUgyldigDato() {

        expectedException.expect(TpsfFunctionalException.class);
        expectedException.expectMessage(TEKST_FOEDT_FOER);

        validateOpprettRequest.validate(RsPersonBestillingKriteriumRequest.builder()
                .antall(1)
                .foedtFoer(UGYLDIG_FOEDT_FOER_DATO)
                .build());
    }

    @Test
    public void validateHovedpersonUgyldigKjoenn() {

        expectedException.expect(TpsfFunctionalException.class);
        expectedException.expectMessage(TEKST_UGYLDIG_KJOENN);

        validateOpprettRequest.validate(RsPersonBestillingKriteriumRequest.builder()
                .antall(1)
                .kjonn("P")
                .build());
    }

    @Test
    public void validateHovedpersonUgyldigDatoIntervall() {

        expectedException.expect(TpsfFunctionalException.class);
        expectedException.expectMessage(TEKST_UGYLDIG_DATO_INTERVALL);

        validateOpprettRequest.validate(RsPersonBestillingKriteriumRequest.builder()
                .antall(1)
                .foedtEtter(LocalDateTime.of(2000, 1, 1, 0, 0))
                .foedtFoer(LocalDateTime.of(1990, 1, 1, 0, 0))
                .build());
    }

    @Test
    public void validatePartnerFoedtEtterUgyldigDato() {

        expectedException.expect(TpsfFunctionalException.class);
        expectedException.expectMessage(TEKST_FOEDT_ETTER);

        validateOpprettRequest.validate(RsPersonBestillingKriteriumRequest.builder()
                .antall(1)
                .relasjoner(RsSimpleRelasjoner.builder()
                        .partner(RsSimplePersonRequest.builder()
                                .foedtEtter(UGYLDIG_FOEDT_ETTER_DATO)
                                .build())
                        .build())
                .build());
    }

    @Test
    public void validatePartnerFoedtFoerUgyldigDato() {

        expectedException.expect(TpsfFunctionalException.class);
        expectedException.expectMessage(TEKST_FOEDT_FOER);

        validateOpprettRequest.validate(RsPersonBestillingKriteriumRequest.builder()
                .antall(1)
                .relasjoner(RsSimpleRelasjoner.builder()
                        .partner(RsSimplePersonRequest.builder()
                                .foedtFoer(UGYLDIG_FOEDT_FOER_DATO)
                                .build())
                        .build())
                .build());
    }

    @Test
    public void validatePartnerUgyldigDatoIntervall() {

        expectedException.expect(TpsfFunctionalException.class);
        expectedException.expectMessage(TEKST_UGYLDIG_DATO_INTERVALL);

        validateOpprettRequest.validate(RsPersonBestillingKriteriumRequest.builder()
                .antall(1)
                .relasjoner(RsSimpleRelasjoner.builder()
                        .partner(RsSimplePersonRequest.builder()
                                .foedtEtter(LocalDateTime.of(2000, 1, 1, 0, 0))
                                .foedtFoer(LocalDateTime.of(1990, 1, 1, 0, 0))
                                .build())
                        .build())
                .build());
    }

    @Test
    public void validatePartnerUgyldigKjoenn() {

        expectedException.expect(TpsfFunctionalException.class);
        expectedException.expectMessage(TEKST_UGYLDIG_KJOENN);

        validateOpprettRequest.validate(RsPersonBestillingKriteriumRequest.builder()
                .antall(1)
                .relasjoner(RsSimpleRelasjoner.builder()
                        .partner(RsSimplePersonRequest.builder()
                                .kjonn("T")
                                .build())
                        .build())
                .build());
    }

    @Test
    public void validateBarnFoedtEtterUgyldigDato() {

        expectedException.expect(TpsfFunctionalException.class);
        expectedException.expectMessage(TEKST_FOEDT_ETTER);

        validateOpprettRequest.validate(RsPersonBestillingKriteriumRequest.builder()
                .antall(1)
                .relasjoner(RsSimpleRelasjoner.builder()
                        .barn(asList(
                                RsSimplePersonRequest.builder()
                                        .foedtEtter(LocalDateTime.of(2020, 1, 1, 0, 0))
                                        .build(),
                                RsSimplePersonRequest.builder()
                                        .foedtEtter(UGYLDIG_FOEDT_ETTER_DATO)
                                        .build())
                        )
                        .build())
                .build());
    }

    @Test
    public void validateBarnFoedtFoerUgyldigDato() {

        expectedException.expect(TpsfFunctionalException.class);
        expectedException.expectMessage(TEKST_FOEDT_FOER);

        validateOpprettRequest.validate(RsPersonBestillingKriteriumRequest.builder()
                .antall(1)
                .relasjoner(RsSimpleRelasjoner.builder()
                        .barn(asList(
                                RsSimplePersonRequest.builder()
                                        .foedtFoer(LocalDateTime.of(2020, 1, 1, 0, 0))
                                        .build(),
                                RsSimplePersonRequest.builder()
                                        .foedtFoer(UGYLDIG_FOEDT_FOER_DATO)
                                        .build())
                        )
                        .build())
                .build());
    }

    @Test
    public void validateBarnUgyldigDatoIntervall() {

        expectedException.expect(TpsfFunctionalException.class);
        expectedException.expectMessage(TEKST_UGYLDIG_DATO_INTERVALL);

        validateOpprettRequest.validate(RsPersonBestillingKriteriumRequest.builder()
                .antall(1)
                .relasjoner(RsSimpleRelasjoner.builder()
                        .barn(asList(
                                RsSimplePersonRequest.builder()
                                        .foedtFoer(LocalDateTime.of(1980, 1, 1, 0, 0))
                                        .build(),
                                RsSimplePersonRequest.builder()
                                        .foedtEtter(LocalDateTime.of(2000, 1, 1, 0, 0))
                                        .foedtFoer(LocalDateTime.of(1990, 1, 1, 0, 0))
                                        .build())
                        )
                        .build())
                .build());
    }

    @Test
    public void validateBarnUgyldigKjoenn() {

        expectedException.expect(TpsfFunctionalException.class);
        expectedException.expectMessage(TEKST_UGYLDIG_KJOENN);

        validateOpprettRequest.validate(RsPersonBestillingKriteriumRequest.builder()
                .antall(1)
                .relasjoner(RsSimpleRelasjoner.builder()
                        .barn(asList(
                                RsSimplePersonRequest.builder()
                                        .kjonn("L")
                                        .build(),
                                RsSimplePersonRequest.builder()
                                        .kjonn("V")
                                        .build())
                        )
                        .build())
                .build());
    }

    @Test(expected = Test.None.class)
    public void validateBarnGyldigKjoenn() {

        validateOpprettRequest.validate(RsPersonBestillingKriteriumRequest.builder()
                .antall(1)
                .relasjoner(RsSimpleRelasjoner.builder()
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
                        .build())
                .build());
    }

    @Test
    public void validateUgyldigAntall() {

        expectedException.expect(TpsfFunctionalException.class);
        expectedException.expectMessage(TEKST_UGYLDIG_ANTALL);

        validateOpprettRequest.validate(RsPersonBestillingKriteriumRequest.builder().build());
    }
}