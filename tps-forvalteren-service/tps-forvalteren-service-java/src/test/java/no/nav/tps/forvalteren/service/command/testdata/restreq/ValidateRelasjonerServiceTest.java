package no.nav.tps.forvalteren.service.command.testdata.restreq;

import static com.google.common.collect.Lists.newArrayList;
import static java.util.Arrays.asList;
import static no.nav.tps.forvalteren.domain.rs.dolly.RsPersonBestillingRelasjonRequest.BorHos;
import static no.nav.tps.forvalteren.domain.rs.dolly.RsPersonBestillingRelasjonRequest.RsBarnRelasjonRequest;
import static no.nav.tps.forvalteren.domain.rs.dolly.RsPersonBestillingRelasjonRequest.RsPartnerRelasjonRequest;
import static no.nav.tps.forvalteren.domain.rs.dolly.RsPersonBestillingRelasjonRequest.RsRelasjoner;
import static no.nav.tps.forvalteren.domain.rs.dolly.RsPersonBestillingRelasjonRequest.builder;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.stream.Collectors;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import no.nav.tps.forvalteren.common.java.message.MessageProvider;
import no.nav.tps.forvalteren.domain.jpa.Person;
import no.nav.tps.forvalteren.domain.jpa.Relasjon;
import no.nav.tps.forvalteren.domain.rs.RsSivilstandRequest;
import no.nav.tps.forvalteren.domain.rs.dolly.RsPersonBestillingRelasjonRequest;
import no.nav.tps.forvalteren.domain.rs.skd.IdentType;
import no.nav.tps.forvalteren.domain.service.DiskresjonskoderType;
import no.nav.tps.forvalteren.service.command.exceptions.TpsfFunctionalException;

@RunWith(MockitoJUnitRunner.class)
public class ValidateRelasjonerServiceTest {

    private static final String IDENT_HOVEDPERSON = "11111111111";
    private static final String IDENT_PARTNER = "22222222222";
    private static final String IDENT_BARN = "33333333333";

    private static final String BARN_FINNES_ALLEREDE = "Barn finnes fra før";
    private static final String PARTNER_FINNES_ALLEREDE = "Partner finnes fra før";
    private static final String BARN_HAR_FORELDRE = "Barn har allerede foreldre";
    private static final String FELLES_ADRESSE_IKKE_FNR = "Kun personer med FNR kan ha felles adresse";
    private static final String FELLES_ADRESSE_KODE6 = "Personer med kode6 kan ikke ha felles adresse";
    private static final String FELLES_ADRESSE_UFB = "Personer uten fast bopel kan ikke ha felles adresse";
    private static final String FELLES_ADRESSE_FORSVUNNET = "Personer som er forsvunnet kan ikke ha felles adresse";
    private static final String EKSISTENSSJEKK_HOVEDPERSON_FEILER = "Hovedperson eksisterer ikke";
    private static final String EKSISTENSSJEKK_PARTNER_FEILER = "Partner eksisterer ikke";
    private static final String EKSISTENSSJEKK_BARN_FEILER = "Barn eksisterer ikke";
    private static final String FELLES_ADRESSE_BOR_HOS_DEG = "Kombinasjonen felles adresse og barn bor hos DEG er ikke mulig";

    @Mock
    private MessageProvider messageProvider;

    @InjectMocks
    private ValidateRelasjonerService validateRelasjonerService;

    private Map<String, Person> personer;

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Before
    public void setup() {

        when(messageProvider.get(eq("bestilling.relasjon.input.validation.duplikat.partner"), anyString())).thenReturn(PARTNER_FINNES_ALLEREDE);
        when(messageProvider.get(eq("bestilling.relasjon.input.validation.duplikat.barn"), anyString())).thenReturn(BARN_FINNES_ALLEREDE);
        when(messageProvider.get(eq("bestilling.relasjon.input.validation.barn.har.foreldre"), anyString())).thenReturn(BARN_HAR_FORELDRE);

        when(messageProvider.get("bestilling.relasjon.input.validation.felles.adresse.ikke.fnr")).thenReturn(FELLES_ADRESSE_IKKE_FNR);
        when(messageProvider.get("bestilling.relasjon.input.validation.felles.adresse.kode6")).thenReturn(FELLES_ADRESSE_KODE6);
        when(messageProvider.get("bestilling.relasjon.input.validation.felles.adresse.ufb")).thenReturn(FELLES_ADRESSE_UFB);
        when(messageProvider.get("bestilling.relasjon.input.validation.felles.adresse.forsvunnet")).thenReturn(FELLES_ADRESSE_FORSVUNNET);

        when(messageProvider.get(eq("bestilling.relasjon.input.validation.hovedperson.ekistere.ikke"), anyString())).thenReturn(EKSISTENSSJEKK_HOVEDPERSON_FEILER);
        when(messageProvider.get(eq("bestilling.relasjon.input.validation.partner.ekistere.ikke"), anyString())).thenReturn(EKSISTENSSJEKK_PARTNER_FEILER);
        when(messageProvider.get(eq("bestilling.relasjon.input.validation.barn.ekistere.ikke"), anyString())).thenReturn(EKSISTENSSJEKK_BARN_FEILER);

        when(messageProvider.get(eq("bestilling.relasjon.input.validation.felles.adresse.barn.bor.hos.deg"), anyString())).thenReturn(FELLES_ADRESSE_BOR_HOS_DEG);
    }

    @Test
    public void makeRelasjonPartnerFinnesFraFoer() {

        RsPersonBestillingRelasjonRequest relasjonRequest = buildRequest(null, null);

        personer = asList(
                Person.builder().ident(IDENT_HOVEDPERSON).kjonn("M")
                        .relasjoner(newArrayList(Relasjon.builder()
                                .relasjonTypeNavn("PARTNER")
                                .personRelasjonMed(Person.builder()
                                        .ident(IDENT_PARTNER)
                                        .build())
                                .build()))
                        .build(),
                Person.builder().ident(IDENT_PARTNER).kjonn("K").build(),
                Person.builder().ident(IDENT_BARN).build())
                .stream().collect(Collectors.toMap(Person::getIdent, person -> person));

        expectedException.expect(TpsfFunctionalException.class);
        expectedException.expectMessage(PARTNER_FINNES_ALLEREDE);

        validateRelasjonerService.isGyldig(IDENT_HOVEDPERSON, relasjonRequest, personer);
    }

    @Test
    public void makeRelasjonBarnFinnesFraFoer() {

        RsPersonBestillingRelasjonRequest relasjonRequest = buildRequest(null, null);

        personer = asList(
                Person.builder().ident(IDENT_HOVEDPERSON).kjonn("M")
                        .relasjoner(newArrayList(Relasjon.builder()
                                .relasjonTypeNavn("FOEDSEL")
                                .personRelasjonMed(Person.builder()
                                        .ident(IDENT_BARN)
                                        .build())
                                .build()))
                        .build(),
                Person.builder().ident(IDENT_PARTNER).kjonn("K").build(),
                Person.builder().ident(IDENT_BARN).build())
                .stream().collect(Collectors.toMap(Person::getIdent, person -> person));

        expectedException.expect(TpsfFunctionalException.class);
        expectedException.expectMessage(BARN_FINNES_ALLEREDE);

        validateRelasjonerService.isGyldig(IDENT_HOVEDPERSON, relasjonRequest, personer);
    }

    @Test
    public void makeRelasjonBarnHarAlleredeForeldre() {

        RsPersonBestillingRelasjonRequest relasjonRequest = buildRequest(null, null);

        personer = asList(
                Person.builder().ident(IDENT_HOVEDPERSON).kjonn("M").build(),
                Person.builder().ident(IDENT_PARTNER).kjonn("K").build(),
                Person.builder().ident(IDENT_BARN).relasjoner(newArrayList(
                        Relasjon.builder()
                                .relasjonTypeNavn("MOR")
                                .personRelasjonMed(Person.builder()
                                        .relasjoner(newArrayList(
                                                Relasjon.builder()
                                                        .relasjonTypeNavn("FOEDSEL")
                                                        .personRelasjonMed(Person.builder().ident(IDENT_BARN).build())
                                                        .build()))
                                        .build())
                                .build(),
                        Relasjon.builder()
                                .relasjonTypeNavn("FAR")
                                .personRelasjonMed(Person.builder()
                                        .relasjoner(newArrayList(
                                                Relasjon.builder()
                                                        .relasjonTypeNavn("FOEDSEL")
                                                        .personRelasjonMed(Person.builder().ident(IDENT_BARN).build())
                                                        .build()))
                                        .build())
                                .build()))
                        .build())
                .stream().collect(Collectors.toMap(Person::getIdent, person -> person));

        expectedException.expect(TpsfFunctionalException.class);
        expectedException.expectMessage(BARN_HAR_FORELDRE);

        validateRelasjonerService.isGyldig(IDENT_HOVEDPERSON, relasjonRequest, personer);
    }

    @Test
    public void makeRelasjonBarnHarAlleredeForeldreMenGjelderAdopsjon() {

        RsPersonBestillingRelasjonRequest relasjonRequest = buildRequest(null, null);

        personer = asList(
                Person.builder().ident(IDENT_HOVEDPERSON).kjonn("M").build(),
                Person.builder().ident(IDENT_PARTNER).kjonn("K").build(),
                Person.builder().ident(IDENT_BARN).relasjoner(newArrayList(
                        Relasjon.builder()
                                .relasjonTypeNavn("MOR")
                                .personRelasjonMed(Person.builder()
                                        .relasjoner(newArrayList(
                                                Relasjon.builder()
                                                        .relasjonTypeNavn("FOEDSEL")
                                                        .personRelasjonMed(Person.builder().ident(IDENT_BARN).build())
                                                        .build()))
                                        .build())
                                .build(),
                        Relasjon.builder()
                                .relasjonTypeNavn("FAR")
                                .personRelasjonMed(Person.builder()
                                        .relasjoner(newArrayList(
                                                Relasjon.builder()
                                                        .relasjonTypeNavn("FOEDSEL")
                                                        .personRelasjonMed(Person.builder().ident(IDENT_BARN).build())
                                                        .build()))
                                        .build())
                                .build()))
                        .build())
                .stream().collect(Collectors.toMap(Person::getIdent, person -> person));

        expectedException.expect(TpsfFunctionalException.class);
        expectedException.expectMessage(BARN_HAR_FORELDRE);

        validateRelasjonerService.isGyldig(IDENT_HOVEDPERSON, relasjonRequest, personer);
    }

    @Test
    public void makeRelasjonPartnerFellesAdresse_IkkeFNR() {

        RsPersonBestillingRelasjonRequest relasjonRequest = buildRequest(true, null);

        personer = asList(
                Person.builder().ident(IDENT_HOVEDPERSON).identtype(IdentType.FNR.name()).build(),
                Person.builder().ident(IDENT_PARTNER).identtype(IdentType.DNR.name()).build(),
                Person.builder().ident(IDENT_BARN).build())
                .stream().collect(Collectors.toMap(Person::getIdent, person -> person));

        expectedException.expect(TpsfFunctionalException.class);
        expectedException.expectMessage(FELLES_ADRESSE_IKKE_FNR);

        validateRelasjonerService.isGyldig(IDENT_HOVEDPERSON, relasjonRequest, personer);
    }

    @Test
    public void makeRelasjonPartnerFellesAdresse_Kode6() {

        RsPersonBestillingRelasjonRequest relasjonRequest = buildRequest(true, null);

        personer = asList(
                Person.builder().ident(IDENT_HOVEDPERSON).build(),
                Person.builder().ident(IDENT_PARTNER).spesreg(DiskresjonskoderType.SPSF.name()).build(),
                Person.builder().ident(IDENT_BARN).build())
                .stream().collect(Collectors.toMap(Person::getIdent, person -> person));

        expectedException.expect(TpsfFunctionalException.class);
        expectedException.expectMessage(FELLES_ADRESSE_KODE6);

        validateRelasjonerService.isGyldig(IDENT_HOVEDPERSON, relasjonRequest, personer);
    }

    @Test
    public void makeRelasjonPartnerFellesAdresse_Ufb() {

        RsPersonBestillingRelasjonRequest relasjonRequest = buildRequest(true, null);

        personer = asList(
                Person.builder().ident(IDENT_HOVEDPERSON).build(),
                Person.builder().ident(IDENT_PARTNER).spesreg(DiskresjonskoderType.UFB.name()).build(),
                Person.builder().ident(IDENT_BARN).build())
                .stream().collect(Collectors.toMap(Person::getIdent, person -> person));

        expectedException.expect(TpsfFunctionalException.class);
        expectedException.expectMessage(FELLES_ADRESSE_UFB);

        validateRelasjonerService.isGyldig(IDENT_HOVEDPERSON, relasjonRequest, personer);
    }

    @Test
    public void makeRelasjonPartnerFellesAdresse_Forsvunnet() {

        RsPersonBestillingRelasjonRequest relasjonRequest = buildRequest(true, null);

        personer = asList(
                Person.builder().ident(IDENT_HOVEDPERSON).build(),
                Person.builder().ident(IDENT_PARTNER).forsvunnetDato(LocalDateTime.now()).build(),
                Person.builder().ident(IDENT_BARN).build())
                .stream().collect(Collectors.toMap(Person::getIdent, person -> person));

        expectedException.expect(TpsfFunctionalException.class);
        expectedException.expectMessage(FELLES_ADRESSE_FORSVUNNET);

        validateRelasjonerService.isGyldig(IDENT_HOVEDPERSON, relasjonRequest, personer);
    }

    @Test
    public void makeRelasjonPartnerEksistenssjekk_DbHovedpersonIkkeFunnet() {

        RsPersonBestillingRelasjonRequest relasjonRequest = buildRequest(null, null);

        personer = asList(
                Person.builder().ident(IDENT_PARTNER).forsvunnetDato(LocalDateTime.now()).build(),
                Person.builder().ident(IDENT_BARN).build())
                .stream().collect(Collectors.toMap(Person::getIdent, person -> person));

        expectedException.expect(TpsfFunctionalException.class);
        expectedException.expectMessage(EKSISTENSSJEKK_HOVEDPERSON_FEILER);

        validateRelasjonerService.isGyldig(IDENT_HOVEDPERSON, relasjonRequest, personer);
    }

    @Test
    public void makeRelasjonPartnerEksistenssjekk_DbPartnerIkkeFunnet() {

        RsPersonBestillingRelasjonRequest relasjonRequest = buildRequest(null, null);

        personer = asList(
                Person.builder().ident(IDENT_HOVEDPERSON).build(),
                Person.builder().ident(IDENT_BARN).build())
                .stream().collect(Collectors.toMap(Person::getIdent, person -> person));

        expectedException.expect(TpsfFunctionalException.class);
        expectedException.expectMessage(EKSISTENSSJEKK_PARTNER_FEILER);

        validateRelasjonerService.isGyldig(IDENT_HOVEDPERSON, relasjonRequest, personer);
    }

    @Test
    public void makeRelasjonPartnerEksistenssjekk_DbBarnIkkeFunnet() {

        RsPersonBestillingRelasjonRequest relasjonRequest = buildRequest(null, null);

        personer = asList(
                Person.builder().ident(IDENT_HOVEDPERSON).build(),
                Person.builder().ident(IDENT_PARTNER).build())
                .stream().collect(Collectors.toMap(Person::getIdent, person -> person));

        expectedException.expect(TpsfFunctionalException.class);
        expectedException.expectMessage(EKSISTENSSJEKK_BARN_FEILER);

        validateRelasjonerService.isGyldig(IDENT_HOVEDPERSON, relasjonRequest, personer);
    }

    @Test
    public void makeRelasjon_PartnerFellesAdresseMenBorHosDeg() {

        RsPersonBestillingRelasjonRequest relasjonRequest = buildRequest(true, BorHos.DEG);

        personer = asList(
                Person.builder().ident(IDENT_HOVEDPERSON).build(),
                Person.builder().ident(IDENT_PARTNER).build(),
                Person.builder().ident(IDENT_BARN).build())
                .stream().collect(Collectors.toMap(Person::getIdent, person -> person));

        expectedException.expect(TpsfFunctionalException.class);
        expectedException.expectMessage(FELLES_ADRESSE_BOR_HOS_DEG);

        validateRelasjonerService.isGyldig(IDENT_HOVEDPERSON, relasjonRequest, personer);
    }

    private static RsPersonBestillingRelasjonRequest buildRequest(Boolean harFellesAdresse, BorHos borHos) {

        return builder()
                .relasjoner(RsRelasjoner.builder()
                        .partner(asList(RsPartnerRelasjonRequest.builder()
                                .ident(IDENT_PARTNER)
                                .harFellesAdresse(harFellesAdresse)
                                .sivilstander(asList(RsSivilstandRequest.builder()
                                        .sivilstand("GIFT")
                                        .sivilstandRegdato(LocalDateTime.now())
                                        .build()))
                                .build()))
                        .barn(asList(RsBarnRelasjonRequest.builder()
                                .ident(IDENT_BARN)
                                .partnerIdent(IDENT_PARTNER)
                                .borHos(borHos)
                                .build()))
                        .build())
                .build();
    }
}