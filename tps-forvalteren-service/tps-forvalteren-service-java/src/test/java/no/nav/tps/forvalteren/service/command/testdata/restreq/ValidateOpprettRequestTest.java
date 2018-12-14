package no.nav.tps.forvalteren.service.command.testdata.restreq;

import static java.util.Arrays.asList;
import static no.nav.tps.forvalteren.common.java.message.MessageConstants.BESTILLING_VALIDERING_FOEDT_ETTER;
import static no.nav.tps.forvalteren.common.java.message.MessageConstants.BESTILLING_VALIDERING_FOEDT_FOER;
import static no.nav.tps.forvalteren.common.java.message.MessageConstants.BESTILLING_VALIDERING_UGYLDIG_INTERVALL;
import static no.nav.tps.forvalteren.common.java.message.MessageConstants.BESTILLING_VALIDERING_UGYLDIG_KJOENN;
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
    private static final String TEKST_FOEDT_ETTER = "Dato født etter kan ikke være etter år 2039.";
    private static final String TEKST_FOEDT_FOER = "Dato født før kan ikke være før år 1900.";
    private static final String TEKST_UGYLDIG_DATO_INTERVALL = "Ugyldig datointervall er oppgitt.";
    private static final String TEKST_UGYLDIG_KJOENN = "Ugyldig kjønn, en av U, K eller M forventet.";

    @Mock
    private MessageProvider messageProvider;

    @InjectMocks
    private ValidateOpprettRequest validateOpprettRequest;

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Before
    public void setup() {
        when(messageProvider.get(BESTILLING_VALIDERING_FOEDT_ETTER)).thenReturn(TEKST_FOEDT_ETTER);
        when(messageProvider.get(BESTILLING_VALIDERING_FOEDT_FOER)).thenReturn(TEKST_FOEDT_FOER);
        when(messageProvider.get(BESTILLING_VALIDERING_UGYLDIG_INTERVALL)).thenReturn(TEKST_UGYLDIG_DATO_INTERVALL);
        when(messageProvider.get(BESTILLING_VALIDERING_UGYLDIG_KJOENN)).thenReturn(TEKST_UGYLDIG_KJOENN);
    }

    @Test(expected = Test.None.class)
    public void validateEmptyRequest() {

        validateOpprettRequest.validate(new RsPersonBestillingKriteriumRequest());
    }

    @Test
    public void validateHovedpersonFoedtEtterUgyldigDato() {

        expectedException.expect(TpsfFunctionalException.class);
        expectedException.expectMessage(TEKST_FOEDT_ETTER);

        validateOpprettRequest.validate(RsPersonBestillingKriteriumRequest.builder()
                .foedtEtter(UGYLDIG_FOEDT_ETTER_DATO)
                .build());
    }

    @Test
    public void validateHovedpersonFoedtFoerUgyldigDato() {

        expectedException.expect(TpsfFunctionalException.class);
        expectedException.expectMessage(TEKST_FOEDT_FOER);

        validateOpprettRequest.validate(RsPersonBestillingKriteriumRequest.builder()
                .foedtFoer(UGYLDIG_FOEDT_FOER_DATO)
                .build());
    }

    @Test
    public void validateHovedpersonUgyldigKjoenn() {

        expectedException.expect(TpsfFunctionalException.class);
        expectedException.expectMessage(TEKST_UGYLDIG_KJOENN);

        validateOpprettRequest.validate(RsPersonBestillingKriteriumRequest.builder()
                .kjonn("P")
                .build());
    }

    @Test
    public void validateHovedpersonUgyldigDatoIntervall() {

        expectedException.expect(TpsfFunctionalException.class);
        expectedException.expectMessage(TEKST_UGYLDIG_DATO_INTERVALL);

        validateOpprettRequest.validate(RsPersonBestillingKriteriumRequest.builder()
                .foedtEtter(LocalDateTime.of(2000, 1, 1, 0, 0))
                .foedtFoer(LocalDateTime.of(1990, 1, 1, 0, 0))
                .build());
    }

    @Test
    public void validatePartnerFoedtEtterUgyldigDato() {

        expectedException.expect(TpsfFunctionalException.class);
        expectedException.expectMessage(TEKST_FOEDT_ETTER);

        validateOpprettRequest.validate(RsPersonBestillingKriteriumRequest.builder()
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
                .relasjoner(RsSimpleRelasjoner.builder()
                        .barn(asList(
                                RsSimplePersonRequest.builder()
                                        .foedtFoer(LocalDateTime.of(2020, 1, 1, 0, 0))
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
}