package no.nav.tps.forvalteren.service.command.testdata.restreq;

import static java.time.LocalDateTime.of;
import static java.util.Collections.singletonList;
import static no.nav.tps.forvalteren.domain.jpa.InnvandretUtvandret.InnUtvandret.INNVANDRET;
import static org.assertj.core.util.Lists.newArrayList;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import ma.glasnost.orika.MapperFacade;
import no.nav.tps.forvalteren.common.message.MessageProvider;
import no.nav.tps.forvalteren.domain.jpa.Gateadresse;
import no.nav.tps.forvalteren.domain.jpa.InnvandretUtvandret;
import no.nav.tps.forvalteren.domain.jpa.Person;
import no.nav.tps.forvalteren.domain.jpa.Statsborgerskap;
import no.nav.tps.forvalteren.domain.rs.dolly.RsPersonBestillingKriteriumRequest;
import no.nav.tps.forvalteren.repository.jpa.PersonRepository;
import no.nav.tps.forvalteren.service.command.exceptions.TpsfFunctionalException;
import no.nav.tps.forvalteren.service.command.testdata.opprett.RandomAdresseService;
import no.nav.tps.forvalteren.service.command.testdata.utils.HentDatoFraIdentService;

@RunWith(MockitoJUnitRunner.class)
public class EndrePersonBestillingServiceTest {

    private static final String IDENT = "12028039031";
    private static final String IDENTTYPE = "FNR";
    private static final LocalDateTime INNVANDRING_DATO = of(1993, 8, 14, 0, 0);
    private static final LocalDateTime TIDLIG_FLYTTEDATO = of(1990, 12, 1, 0, 0);
    private static final LocalDateTime FLYTTEDATO = of(2016, 12, 1, 0, 0);

    @Mock
    private PersonRepository personRepository;

    @Mock
    private RelasjonNyePersonerBestillingService relasjonNyePersonerBestillingService;

    @Mock
    private MessageProvider messageProvider;

    @Mock
    private MapperFacade mapperFacade;

    @Mock
    private RandomAdresseService randomAdresseService;

    @Mock
    private HentDatoFraIdentService hentDatoFraIdentService;

    @InjectMocks
    private EndrePersonBestillingService endrePersonBestillingService;

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Before
    public void setup() {

        when(messageProvider.get("endre.person.innutvandring.validation.flyttedato"))
                .thenReturn("Flyttedato kan ikke vaere tidligere enn dato paa forrige bevegelse");
        when(messageProvider.get("endre.person.innutvandring.validation.samme.aksjon"))
                .thenReturn("To like hendelser paa hverandre med innvandring eller utvandring er ikke tillatt");
        when(messageProvider.get("endre.person.innutvandring.validation.identtype"))
                .thenReturn("Person som utvandrer maa ha identtype FNR");
        when(messageProvider.get(eq("endre.person.statsborgerskap.validation.eksisterer.allerede"), anyString()))
                .thenReturn("Statsborgerskap eksisterer allerede");
    }

    @Test
    public void utvandringPaPerson_forTidligFlyttedato() {

        when(personRepository.findByIdent(IDENT)).thenReturn(Person.builder()
                .ident(IDENT)
                .identtype(IDENTTYPE)
                .innvandretUtvandret(newArrayList(InnvandretUtvandret.builder()
                        .innutvandret(INNVANDRET)
                        .landkode("AUS")
                        .flyttedato(INNVANDRING_DATO)
                        .build()))
                .build());

        RsPersonBestillingKriteriumRequest request = new RsPersonBestillingKriteriumRequest();
        request.setUtvandretTilLand("SAU");
        request.setUtvandretTilLandFlyttedato(TIDLIG_FLYTTEDATO);

        expectedException.expect(TpsfFunctionalException.class);
        expectedException.expectMessage("Flyttedato kan ikke vaere tidligere enn dato paa forrige bevegelse");

        endrePersonBestillingService.execute(IDENT, request);
    }

    @Test
    public void setInnvandringPaPerson_gjentattOperasjon() {

        when(personRepository.findByIdent(IDENT)).thenReturn(Person.builder()
                .ident(IDENT)
                .identtype(IDENTTYPE)
                .innvandretUtvandret(newArrayList(InnvandretUtvandret.builder()
                        .innutvandret(INNVANDRET)
                        .landkode("AUS")
                        .flyttedato(INNVANDRING_DATO)
                        .build()))
                .build());

        RsPersonBestillingKriteriumRequest request = new RsPersonBestillingKriteriumRequest();
        request.setInnvandretFraLand("NRG");
        request.setInnvandretFraLandFlyttedato(FLYTTEDATO);

        expectedException.expect(TpsfFunctionalException.class);
        expectedException.expectMessage("To like hendelser paa hverandre med innvandring eller utvandring er ikke tillatt");

        endrePersonBestillingService.execute(IDENT, request);
    }

    @Test
    public void utenInnvandringUtvandring_personIkkeEndret() {

        Person person = Person.builder()
                .ident(IDENT)
                .identtype(IDENTTYPE)
                .build();
        when(personRepository.findByIdent(IDENT)).thenReturn(person);
        when(randomAdresseService.hentRandomAdresse(1, null)).thenReturn(singletonList(Gateadresse.builder().build()));

        RsPersonBestillingKriteriumRequest request = new RsPersonBestillingKriteriumRequest();

        endrePersonBestillingService.execute(IDENT, request);

        assertThat(person.getInnvandretUtvandret(), hasSize(0));
    }

    @Test
    public void leggTilEksisterendeStatsborgerskap_skalFeile() {

        Person person = Person.builder()
                .ident(IDENT)
                .identtype(IDENTTYPE)
                .statsborgerskap(singletonList(Statsborgerskap.builder()
                        .statsborgerskap("FIN")
                        .statsborgerskapRegdato(FLYTTEDATO)
                        .build()))
                .build();
        when(personRepository.findByIdent(IDENT)).thenReturn(person);

        RsPersonBestillingKriteriumRequest request = new RsPersonBestillingKriteriumRequest();
        request.setStatsborgerskap("FIN");

        expectedException.expect(TpsfFunctionalException.class);
        expectedException.expectMessage("Statsborgerskap eksisterer allerede");

        endrePersonBestillingService.execute(IDENT, request);
    }

    @Test
    public void utvandreDnrPerson_skalFeile() {

        when(personRepository.findByIdent(IDENT)).thenReturn(Person.builder()
                .ident(IDENT)
                .innvandretUtvandret(newArrayList(InnvandretUtvandret.builder()
                        .innutvandret(INNVANDRET)
                        .landkode("AUS")
                        .flyttedato(INNVANDRING_DATO)
                        .build()))
                .build());

        expectedException.expect(TpsfFunctionalException.class);
        expectedException.expectMessage("Person som utvandrer maa ha identtype FNR");

        RsPersonBestillingKriteriumRequest request = new RsPersonBestillingKriteriumRequest();
        request.setUtvandretTilLand("SAU");
        request.setUtvandretTilLandFlyttedato(TIDLIG_FLYTTEDATO);

        endrePersonBestillingService.execute(IDENT, request);
    }
}