package no.nav.tps.forvalteren.service.command.testdata.restreq;

import static com.google.common.collect.Lists.newArrayList;
import static java.util.Arrays.asList;
import static no.nav.tps.forvalteren.domain.rs.dolly.RsPersonBestillingRelasjonRequest.BarnType;
import static no.nav.tps.forvalteren.domain.rs.dolly.RsPersonBestillingRelasjonRequest.RsBarnRelasjonRequest;
import static no.nav.tps.forvalteren.domain.rs.dolly.RsPersonBestillingRelasjonRequest.RsPartnerRelasjonRequest;
import static no.nav.tps.forvalteren.domain.rs.dolly.RsPersonBestillingRelasjonRequest.RsRelasjoner;
import static no.nav.tps.forvalteren.domain.rs.dolly.RsPersonBestillingRelasjonRequest.builder;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.anyList;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import ma.glasnost.orika.MapperFacade;
import no.nav.tps.forvalteren.common.java.message.MessageProvider;
import no.nav.tps.forvalteren.domain.jpa.Person;
import no.nav.tps.forvalteren.domain.jpa.Relasjon;
import no.nav.tps.forvalteren.domain.rs.RsSivilstandRequest;
import no.nav.tps.forvalteren.domain.rs.dolly.RsPersonBestillingRelasjonRequest;
import no.nav.tps.forvalteren.repository.jpa.PersonRepository;
import no.nav.tps.forvalteren.service.command.exceptions.TpsfFunctionalException;
import no.nav.tps.forvalteren.service.command.testdata.opprett.RandomAdresseService;

@RunWith(MockitoJUnitRunner.class)
public class RelasjonPersonBestillingServiceTest {

    private static final String IDENT_HOVEDPERSON = "11111111111";
    private static final String IDENT_PARTNER = "22222222222";
    private static final String IDENT_BARN = "33333333333";
    private static final String BARN_FINNES_ALLEREDE = "Barn finnes fra før";
    private static final String PARTNER_FINNES_ALLEREDE = "Partner finnes fra før";
    private static final String BARN_HAR_FORELDRE = "Barn har allerede foreldre";

    @Mock
    private MapperFacade mapperFacade;

    @Mock
    private PersonRepository personRepository;

    @Mock
    private RandomAdresseService randomAdresseService;

    @Mock
    private MessageProvider messageProvider;

    @InjectMocks
    private RelasjonPersonBestillingService relasjonPersonBestillingService;

    private ArgumentCaptor<Person> argumentCaptor;

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Before
    public void setup() {

        when(messageProvider.get(eq("bestilling.relasjon.input.validation.duplikat.partner"), anyString())).thenReturn(PARTNER_FINNES_ALLEREDE);
        when(messageProvider.get(eq("bestilling.relasjon.input.validation.duplikat.barn"), anyString())).thenReturn(BARN_FINNES_ALLEREDE);
        when(messageProvider.get(eq("bestilling.relasjon.input.validation.barn.har.foreldre"), anyString())).thenReturn(BARN_HAR_FORELDRE);

        when(personRepository.findByIdentIn(anyList())).thenReturn(asList(
                Person.builder().ident(IDENT_HOVEDPERSON).kjonn("M").build(),
                Person.builder().ident(IDENT_PARTNER).kjonn("K").build(),
                Person.builder().ident(IDENT_BARN).build()));
    }

    @Test
    public void makeRelasjonOk() {

        RsPersonBestillingRelasjonRequest relasjonRequest = buildRequest();
        argumentCaptor = ArgumentCaptor.forClass(Person.class);

        relasjonPersonBestillingService.makeRelasjon(IDENT_HOVEDPERSON, relasjonRequest);
        verify(personRepository).save(argumentCaptor.capture());

        assertThat(argumentCaptor.getValue().getIdent(), is(equalTo(IDENT_HOVEDPERSON)));
        assertThat(argumentCaptor.getValue().getRelasjoner(), hasSize(2));
        assertThat(argumentCaptor.getValue().getRelasjoner().get(0).getRelasjonTypeNavn(), is(equalTo("PARTNER")));
        assertThat(argumentCaptor.getValue().getRelasjoner().get(0).getPersonRelasjonMed().getIdent(), is(equalTo(IDENT_PARTNER)));
        assertThat(argumentCaptor.getValue().getRelasjoner().get(0).getPersonRelasjonMed().getRelasjoner().get(0).getRelasjonTypeNavn(), is(equalTo("PARTNER")));
        assertThat(argumentCaptor.getValue().getRelasjoner().get(0).getPersonRelasjonMed().getRelasjoner().get(0).getPersonRelasjonMed().getIdent(), is(equalTo(IDENT_HOVEDPERSON)));
        assertThat(argumentCaptor.getValue().getRelasjoner().get(0).getPersonRelasjonMed().getRelasjoner().get(1).getRelasjonTypeNavn(), is(equalTo("FOEDSEL")));
        assertThat(argumentCaptor.getValue().getRelasjoner().get(0).getPersonRelasjonMed().getRelasjoner().get(1).getPersonRelasjonMed().getIdent(), is(equalTo(IDENT_BARN)));

        assertThat(argumentCaptor.getValue().getRelasjoner().get(1).getRelasjonTypeNavn(), is(equalTo("FOEDSEL")));
        assertThat(argumentCaptor.getValue().getRelasjoner().get(1).getPersonRelasjonMed().getIdent(), is(equalTo(IDENT_BARN)));
        assertThat(argumentCaptor.getValue().getRelasjoner().get(1).getPersonRelasjonMed().getRelasjoner().get(0).getRelasjonTypeNavn(), is(equalTo("FAR")));
        assertThat(argumentCaptor.getValue().getRelasjoner().get(1).getPersonRelasjonMed().getRelasjoner().get(0).getPersonRelasjonMed().getIdent(), is(equalTo(IDENT_HOVEDPERSON)));
        assertThat(argumentCaptor.getValue().getRelasjoner().get(1).getPersonRelasjonMed().getRelasjoner().get(1).getRelasjonTypeNavn(), is(equalTo("MOR")));
        assertThat(argumentCaptor.getValue().getRelasjoner().get(1).getPersonRelasjonMed().getRelasjoner().get(1).getPersonRelasjonMed().getIdent(), is(equalTo(IDENT_PARTNER)));
    }

    @Test
    public void makeRelasjonPartnerFinnesFraFoer() {

        RsPersonBestillingRelasjonRequest relasjonRequest = buildRequest();

        when(personRepository.findByIdentIn(anyList())).thenReturn(asList(
                Person.builder().ident(IDENT_HOVEDPERSON).kjonn("M")
                        .relasjoner(newArrayList(Relasjon.builder()
                                .relasjonTypeNavn("PARTNER")
                                .personRelasjonMed(Person.builder()
                                        .ident(IDENT_PARTNER)
                                        .build())
                                .build()))
                        .build(),
                Person.builder().ident(IDENT_PARTNER).kjonn("K").build(),
                Person.builder().ident(IDENT_BARN).build()));

        expectedException.expect(TpsfFunctionalException.class);
        expectedException.expectMessage(PARTNER_FINNES_ALLEREDE);

        relasjonPersonBestillingService.makeRelasjon(IDENT_HOVEDPERSON, relasjonRequest);
    }

    @Test
    public void makeRelasjonBarnFinnesFraFoer() {

        RsPersonBestillingRelasjonRequest relasjonRequest = buildRequest();

        when(personRepository.findByIdentIn(anyList())).thenReturn(asList(
                Person.builder().ident(IDENT_HOVEDPERSON).kjonn("M")
                        .relasjoner(newArrayList(Relasjon.builder()
                                .relasjonTypeNavn("FOEDSEL")
                                .personRelasjonMed(Person.builder()
                                        .ident(IDENT_BARN)
                                        .build())
                                .build()))
                        .build(),
                Person.builder().ident(IDENT_PARTNER).kjonn("K").build(),
                Person.builder().ident(IDENT_BARN).build()));

        expectedException.expect(TpsfFunctionalException.class);
        expectedException.expectMessage(BARN_FINNES_ALLEREDE);

        relasjonPersonBestillingService.makeRelasjon(IDENT_HOVEDPERSON, relasjonRequest);
    }

    @Test
    public void makeRelasjonBarnHarAlleredeForeldre() {

        RsPersonBestillingRelasjonRequest relasjonRequest = buildRequest();

        when(personRepository.findByIdentIn(anyList())).thenReturn(asList(
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
                        .build()));

        expectedException.expect(TpsfFunctionalException.class);
        expectedException.expectMessage(BARN_HAR_FORELDRE);

        relasjonPersonBestillingService.makeRelasjon(IDENT_HOVEDPERSON, relasjonRequest);
    }

    @Test
    public void makeRelasjonBarnHarAlleredeForeldreMenGjelderAdopsjon() {

        RsPersonBestillingRelasjonRequest relasjonRequest = buildRequest();

        when(personRepository.findByIdentIn(anyList())).thenReturn(asList(
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
                        .build()));

        expectedException.expect(TpsfFunctionalException.class);
        expectedException.expectMessage(BARN_HAR_FORELDRE);

        relasjonPersonBestillingService.makeRelasjon(IDENT_HOVEDPERSON, relasjonRequest);
    }

    private static RsPersonBestillingRelasjonRequest buildRequest() {

        return builder()
                .relasjoner(RsRelasjoner.builder()
                        .partner(asList(RsPartnerRelasjonRequest.builder()
                                .ident(IDENT_PARTNER)
                                .harFellesAdresse(true)
                                .sivilstander(asList(RsSivilstandRequest.builder()
                                        .sivilstand("GIFT")
                                        .sivilstandRegdato(LocalDateTime.now())
                                        .build()))
                                .build()))
                        .barn(asList(RsBarnRelasjonRequest.builder()
                                .ident(IDENT_BARN)
                                .partnerIdent(IDENT_PARTNER)
                                .barnType(BarnType.FELLES)
                                .build()))
                        .build())
                .build();
    }
}