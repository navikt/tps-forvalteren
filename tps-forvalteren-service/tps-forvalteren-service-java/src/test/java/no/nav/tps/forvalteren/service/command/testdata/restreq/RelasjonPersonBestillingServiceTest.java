package no.nav.tps.forvalteren.service.command.testdata.restreq;

import static java.util.Arrays.asList;
import static no.nav.tps.forvalteren.domain.rs.dolly.RsPersonBestillingRelasjonRequest.BorHos;
import static no.nav.tps.forvalteren.domain.rs.dolly.RsPersonBestillingRelasjonRequest.RsBarnRelasjonRequest;
import static no.nav.tps.forvalteren.domain.rs.dolly.RsPersonBestillingRelasjonRequest.RsPartnerRelasjonRequest;
import static no.nav.tps.forvalteren.domain.rs.dolly.RsPersonBestillingRelasjonRequest.RsRelasjoner;
import static no.nav.tps.forvalteren.domain.rs.dolly.RsPersonBestillingRelasjonRequest.builder;
import static org.assertj.core.util.Lists.newArrayList;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.anyList;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import ma.glasnost.orika.MapperFacade;
import no.nav.tps.forvalteren.domain.jpa.Adresse;
import no.nav.tps.forvalteren.domain.jpa.Gateadresse;
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

    private static final String GATENAVN_1 = "Lurumlia";
    private static final String GATEKODE_1 = "01234";
    private static final String KOMMUNENR_1 = "5555";
    private static final String GATENAVN_2 = "Hengemyra";
    private static final String GATEKODE_2 = "56789";
    private static final String KOMMUNENR_2 = "6666";

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

    @Test
    public void makeRelasjon_Ok() {

        when(personRepository.findByIdentIn(anyList())).thenReturn(asList(
                Person.builder().ident(IDENT_HOVEDPERSON).kjonn("M").build(),
                Person.builder().ident(IDENT_PARTNER).kjonn("K").build(),
                Person.builder().ident(IDENT_BARN).build()));

        when(randomAdresseService.execute(anyList(), any())).thenReturn(newArrayList(Person.builder().build()));
        argumentCaptor = ArgumentCaptor.forClass(Person.class);

        relasjonPersonBestillingService.makeRelasjon(IDENT_HOVEDPERSON, buildRequest(null, null));
        verify(personRepository).save(argumentCaptor.capture());

        assertThat(argumentCaptor.getValue().getIdent(), is(equalTo(IDENT_HOVEDPERSON)));
        assertThat(argumentCaptor.getValue().getRelasjoner(), hasSize(2));
        assertThat(argumentCaptor.getValue().getRelasjoner().get(0).getRelasjonTypeNavn(), is(equalTo("PARTNER")));
        assertThat(argumentCaptor.getValue().getRelasjoner().get(0).getPersonRelasjonMed().getIdent(), is(equalTo(IDENT_PARTNER)));
        assertThat(argumentCaptor.getValue().getRelasjoner().get(0).getPersonRelasjonMed().getRelasjoner().get(0).getRelasjonTypeNavn(), is(equalTo("PARTNER")));
        assertThat(argumentCaptor.getValue().getRelasjoner().get(0).getPersonRelasjonMed().getRelasjoner().get(0).getPersonRelasjonMed().getIdent(), is(equalTo(IDENT_HOVEDPERSON)));
        assertThat(argumentCaptor.getValue().getRelasjoner().get(0).getPersonRelasjonMed().getRelasjoner().get(1).getRelasjonTypeNavn(), is(equalTo("BARN")));
        assertThat(argumentCaptor.getValue().getRelasjoner().get(0).getPersonRelasjonMed().getRelasjoner().get(1).getPersonRelasjonMed().getIdent(), is(equalTo(IDENT_BARN)));

        assertThat(argumentCaptor.getValue().getRelasjoner().get(1).getRelasjonTypeNavn(), is(equalTo("BARN")));
        assertThat(argumentCaptor.getValue().getRelasjoner().get(1).getPersonRelasjonMed().getIdent(), is(equalTo(IDENT_BARN)));
        assertThat(argumentCaptor.getValue().getRelasjoner().get(1).getPersonRelasjonMed().getRelasjoner().get(0).getRelasjonTypeNavn(), is(equalTo("FAR")));
        assertThat(argumentCaptor.getValue().getRelasjoner().get(1).getPersonRelasjonMed().getRelasjoner().get(0).getPersonRelasjonMed().getIdent(), is(equalTo(IDENT_HOVEDPERSON)));
        assertThat(argumentCaptor.getValue().getRelasjoner().get(1).getPersonRelasjonMed().getRelasjoner().get(1).getRelasjonTypeNavn(), is(equalTo("MOR")));
        assertThat(argumentCaptor.getValue().getRelasjoner().get(1).getPersonRelasjonMed().getRelasjoner().get(1).getPersonRelasjonMed().getIdent(), is(equalTo(IDENT_PARTNER)));
    }

    @Test
    public void makeAdresse_HarFellesAdresse() {

        Adresse adresse1 = Gateadresse.builder()
                .adresse(GATENAVN_1)
                .gatekode(GATEKODE_1)
                .build();
        adresse1.setKommunenr(KOMMUNENR_1);
        when(personRepository.findByIdentIn(anyList())).thenReturn(asList(
                Person.builder().ident(IDENT_HOVEDPERSON)
                        .boadresse(newArrayList(adresse1))
                        .build(),
                Person.builder().ident(IDENT_PARTNER)
                        .boadresse(newArrayList(Gateadresse.builder()
                                .adresse(GATENAVN_2)
                                .gatekode(GATEKODE_2)
                                .build()))
                        .build(),
                Person.builder().ident(IDENT_BARN).build()));

        when(mapperFacade.map(adresse1, Gateadresse.class)).thenReturn((Gateadresse) adresse1);

        argumentCaptor = ArgumentCaptor.forClass(Person.class);

        relasjonPersonBestillingService.makeRelasjon(IDENT_HOVEDPERSON, buildRequest(true, null));
        verify(personRepository).save(argumentCaptor.capture());

        assertThat(((Gateadresse) argumentCaptor.getValue().getBoadresse().get(0)).getAdresse(), is(equalTo(GATENAVN_1)));
        assertThat(((Gateadresse) argumentCaptor.getValue().getBoadresse().get(0)).getGatekode(), is(equalTo(GATEKODE_1)));
        assertThat(argumentCaptor.getValue().getBoadresse().get(0).getKommunenr(), is(equalTo(KOMMUNENR_1)));

        assertThat(((Gateadresse) argumentCaptor.getValue().getRelasjoner().get(0).getPersonRelasjonMed().getBoadresse().get(1)).getAdresse(), is(equalTo(GATENAVN_1)));
        assertThat(((Gateadresse) argumentCaptor.getValue().getRelasjoner().get(0).getPersonRelasjonMed().getBoadresse().get(1)).getGatekode(), is(equalTo(GATEKODE_1)));
        assertThat(argumentCaptor.getValue().getRelasjoner().get(0).getPersonRelasjonMed().getBoadresse().get(1).getKommunenr(), is(equalTo(KOMMUNENR_1)));
    }

    @Test
    public void makeAdresse_HarUlikAdresse() {

        Adresse adresse1 = Gateadresse.builder()
                .adresse(GATENAVN_1)
                .gatekode(GATEKODE_1)
                .build();
        adresse1.setKommunenr(KOMMUNENR_1);
        when(personRepository.findByIdentIn(anyList())).thenReturn(asList(
                Person.builder().ident(IDENT_HOVEDPERSON)
                        .boadresse(newArrayList(adresse1))
                        .build(),
                Person.builder().ident(IDENT_PARTNER)
                        .boadresse(newArrayList(Gateadresse.builder()
                                .adresse(GATENAVN_2)
                                .gatekode(GATEKODE_2)
                                .build()))
                        .build(),
                Person.builder().ident(IDENT_BARN).build()));

        when(mapperFacade.map(adresse1, Gateadresse.class)).thenReturn((Gateadresse) adresse1);

        argumentCaptor = ArgumentCaptor.forClass(Person.class);

        relasjonPersonBestillingService.makeRelasjon(IDENT_HOVEDPERSON, buildRequest(false, null));
        verify(personRepository).save(argumentCaptor.capture());

        assertThat(((Gateadresse) argumentCaptor.getValue().getBoadresse().get(0)).getAdresse(), is(equalTo(GATENAVN_1)));
        assertThat(((Gateadresse) argumentCaptor.getValue().getBoadresse().get(0)).getGatekode(), is(equalTo(GATEKODE_1)));
        assertThat(argumentCaptor.getValue().getBoadresse().get(0).getKommunenr(), is(equalTo(KOMMUNENR_1)));

        assertThat(((Gateadresse) argumentCaptor.getValue().getRelasjoner().get(0).getPersonRelasjonMed().getBoadresse().get(0)).getAdresse(), is(equalTo(GATENAVN_2)));
        assertThat(((Gateadresse) argumentCaptor.getValue().getRelasjoner().get(0).getPersonRelasjonMed().getBoadresse().get(0)).getGatekode(), is(equalTo(GATEKODE_2)));
    }

    @Test
    public void makeAdresse_SkalHaUlikAdresse() {

        Adresse adresse1 = Gateadresse.builder()
                .adresse(GATENAVN_1)
                .gatekode(GATEKODE_1)
                .build();
        adresse1.setKommunenr(KOMMUNENR_1);
        Adresse adresse2 = Gateadresse.builder()
                .adresse(GATENAVN_2)
                .gatekode(GATEKODE_2)
                .build();
        adresse2.setKommunenr(KOMMUNENR_2);
        Person partner = Person.builder().ident(IDENT_PARTNER)
                .boadresse(newArrayList(adresse1, adresse2))
                .build();
        when(personRepository.findByIdentIn(anyList())).thenReturn(asList(
                Person.builder().ident(IDENT_HOVEDPERSON)
                        .boadresse(newArrayList(adresse1))
                        .build(),
                partner,
                Person.builder().ident(IDENT_BARN).build()));

        when(mapperFacade.map(adresse1, Gateadresse.class)).thenReturn((Gateadresse) adresse1);

        argumentCaptor = ArgumentCaptor.forClass(Person.class);

        relasjonPersonBestillingService.makeRelasjon(IDENT_HOVEDPERSON, buildRequest(false, null));
        verify(personRepository).save(argumentCaptor.capture());

        assertThat(((Gateadresse) argumentCaptor.getValue().getBoadresse().get(0)).getAdresse(), is(equalTo(GATENAVN_1)));
        assertThat(((Gateadresse) argumentCaptor.getValue().getBoadresse().get(0)).getGatekode(), is(equalTo(GATEKODE_1)));
        assertThat(argumentCaptor.getValue().getBoadresse().get(0).getKommunenr(), is(equalTo(KOMMUNENR_1)));

        assertThat(((Gateadresse) argumentCaptor.getValue().getRelasjoner().get(0).getPersonRelasjonMed().getBoadresse().get(1)).getAdresse(), is(equalTo(GATENAVN_2)));
        assertThat(((Gateadresse) argumentCaptor.getValue().getRelasjoner().get(0).getPersonRelasjonMed().getBoadresse().get(1)).getGatekode(), is(equalTo(GATEKODE_2)));
        assertThat(argumentCaptor.getValue().getRelasjoner().get(0).getPersonRelasjonMed().getBoadresse().get(1).getKommunenr(), is(equalTo(KOMMUNENR_2)));
    }

    @Test
    public void makeAdresse_BarnBorOss() {

        Adresse adresse1 = Gateadresse.builder()
                .adresse(GATENAVN_1)
                .gatekode(GATEKODE_1)
                .build();
        adresse1.setKommunenr(KOMMUNENR_1);
        Adresse adresse2 = Gateadresse.builder()
                .adresse(GATENAVN_2)
                .gatekode(GATEKODE_2)
                .build();
        adresse2.setKommunenr(KOMMUNENR_2);

        when(personRepository.findByIdentIn(anyList())).thenReturn(asList(
                Person.builder().ident(IDENT_HOVEDPERSON)
                        .boadresse(newArrayList(adresse1))
                        .build(),
                Person.builder().ident(IDENT_PARTNER)
                        .boadresse(newArrayList(adresse1))
                        .build(),
                Person.builder().ident(IDENT_BARN)
                        .boadresse(newArrayList(adresse2))
                        .build()));

        when(mapperFacade.map(adresse1, Gateadresse.class)).thenReturn((Gateadresse) adresse1);

        argumentCaptor = ArgumentCaptor.forClass(Person.class);

        relasjonPersonBestillingService.makeRelasjon(IDENT_HOVEDPERSON, buildRequest(true, BorHos.OSS));
        verify(personRepository).save(argumentCaptor.capture());

        assertThat(((Gateadresse) argumentCaptor.getValue().getBoadresse().get(0)).getAdresse(), is(equalTo(GATENAVN_1)));
        assertThat(((Gateadresse) argumentCaptor.getValue().getBoadresse().get(0)).getGatekode(), is(equalTo(GATEKODE_1)));
        assertThat(argumentCaptor.getValue().getBoadresse().get(0).getKommunenr(), is(equalTo(KOMMUNENR_1)));

        assertThat(argumentCaptor.getValue().getRelasjoner().get(1).getRelasjonTypeNavn(), is(equalTo("BARN")));
        assertThat(((Gateadresse) argumentCaptor.getValue().getRelasjoner().get(1).getPersonRelasjonMed().getBoadresse().get(1)).getAdresse(), is(equalTo(GATENAVN_1)));
        assertThat(((Gateadresse) argumentCaptor.getValue().getRelasjoner().get(1).getPersonRelasjonMed().getBoadresse().get(1)).getGatekode(), is(equalTo(GATEKODE_1)));
        assertThat(argumentCaptor.getValue().getRelasjoner().get(1).getPersonRelasjonMed().getBoadresse().get(1).getKommunenr(), is(equalTo(KOMMUNENR_1)));
    }

    @Test
    public void makeAdresse_BarnBorDeg() {

        Adresse adresse1 = Gateadresse.builder()
                .adresse(GATENAVN_1)
                .gatekode(GATEKODE_1)
                .build();
        adresse1.setKommunenr(KOMMUNENR_1);
        Adresse adresse2 = Gateadresse.builder()
                .adresse(GATENAVN_2)
                .gatekode(GATEKODE_2)
                .build();
        adresse2.setKommunenr(KOMMUNENR_2);

        when(personRepository.findByIdentIn(anyList())).thenReturn(asList(
                Person.builder().ident(IDENT_HOVEDPERSON)
                        .boadresse(newArrayList(adresse1))
                        .build(),
                Person.builder().ident(IDENT_PARTNER)
                        .boadresse(newArrayList(adresse2))
                        .build(),
                Person.builder().ident(IDENT_BARN)
                        .boadresse(newArrayList(adresse1))
                        .build()));

        when(mapperFacade.map(adresse2, Gateadresse.class)).thenReturn((Gateadresse) adresse2);

        argumentCaptor = ArgumentCaptor.forClass(Person.class);

        relasjonPersonBestillingService.makeRelasjon(IDENT_HOVEDPERSON, buildRequest(false, BorHos.DEG));
        verify(personRepository).save(argumentCaptor.capture());

        assertThat(((Gateadresse) argumentCaptor.getValue().getBoadresse().get(0)).getAdresse(), is(equalTo(GATENAVN_1)));
        assertThat(((Gateadresse) argumentCaptor.getValue().getBoadresse().get(0)).getGatekode(), is(equalTo(GATEKODE_1)));
        assertThat(argumentCaptor.getValue().getBoadresse().get(0).getKommunenr(), is(equalTo(KOMMUNENR_1)));

        assertThat(argumentCaptor.getValue().getRelasjoner().get(1).getRelasjonTypeNavn(), is(equalTo("BARN")));
        assertThat(((Gateadresse) argumentCaptor.getValue().getRelasjoner().get(1).getPersonRelasjonMed().getBoadresse().get(1)).getAdresse(), is(equalTo(GATENAVN_2)));
        assertThat(((Gateadresse) argumentCaptor.getValue().getRelasjoner().get(1).getPersonRelasjonMed().getBoadresse().get(1)).getGatekode(), is(equalTo(GATEKODE_2)));
        assertThat(argumentCaptor.getValue().getRelasjoner().get(1).getPersonRelasjonMed().getBoadresse().get(1).getKommunenr(), is(equalTo(KOMMUNENR_2)));
    }

    private static RsPersonBestillingRelasjonRequest buildRequest(Boolean harFellesAdresse, BorHos borhos) {

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
                                .borHos(borhos)
                                .build()))
                        .build())
                .build();
    }
}