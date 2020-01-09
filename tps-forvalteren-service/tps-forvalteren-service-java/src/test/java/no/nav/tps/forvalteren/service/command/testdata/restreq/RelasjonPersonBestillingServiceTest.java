package no.nav.tps.forvalteren.service.command.testdata.restreq;

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
import static org.mockito.Mockito.anyList;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import ma.glasnost.orika.MapperFacade;
import no.nav.tps.forvalteren.domain.jpa.Person;
import no.nav.tps.forvalteren.domain.rs.RsSivilstandRequest;
import no.nav.tps.forvalteren.domain.rs.dolly.RsPersonBestillingRelasjonRequest;
import no.nav.tps.forvalteren.repository.jpa.PersonRepository;
import no.nav.tps.forvalteren.service.command.testdata.opprett.RandomAdresseService;

@RunWith(MockitoJUnitRunner.class)
public class RelasjonPersonBestillingServiceTest {

    private static final String IDENT_HOVEDPERSON = "11111111111";
    private static final String IDENT_PARTNER = "22222222222";
    private static final String IDENT_BARN = "33333333333";

    @Mock
    private MapperFacade mapperFacade;

    @Mock
    private PersonRepository personRepository;

    @Mock
    private RandomAdresseService randomAdresseService;

    @Mock
    private ValidateRelasjonerService validateRelasjonerService;

    @InjectMocks
    private RelasjonPersonBestillingService relasjonPersonBestillingService;

    private ArgumentCaptor<Person> argumentCaptor;

    @Before
    public void setup() {

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