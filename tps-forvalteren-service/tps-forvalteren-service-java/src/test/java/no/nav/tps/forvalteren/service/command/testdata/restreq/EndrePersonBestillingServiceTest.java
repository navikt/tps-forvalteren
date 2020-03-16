package no.nav.tps.forvalteren.service.command.testdata.restreq;

import static no.nav.tps.forvalteren.domain.jpa.InnvandretUtvandret.InnUtvandret.INNVANDRET;
import static org.assertj.core.util.Lists.newArrayList;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;
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

import no.nav.tps.forvalteren.common.java.message.MessageProvider;
import no.nav.tps.forvalteren.domain.jpa.InnvandretUtvandret;
import no.nav.tps.forvalteren.domain.jpa.Person;
import no.nav.tps.forvalteren.domain.rs.dolly.RsPersonBestillingKriteriumRequest;
import no.nav.tps.forvalteren.repository.jpa.PersonRepository;
import no.nav.tps.forvalteren.service.command.exceptions.TpsfFunctionalException;
import no.nav.tps.forvalteren.service.command.testdata.utils.HentDatoFraIdentService;

@RunWith(MockitoJUnitRunner.class)
public class EndrePersonBestillingServiceTest {

    private static final String IDENT = "12028039031";
    private static final String IDENTTYPE = "FNR";
    private static final LocalDateTime FOEDSEL_DATO = LocalDateTime.of(1980, 2, 12, 0, 0);
    private static final LocalDateTime INNVANDRING_DATO = LocalDateTime.of(1993, 8, 14, 0, 0);
    private static final LocalDateTime TIDLIG_FLYTTEDATO = LocalDateTime.of(1990, 12, 1, 0, 0);
    private static final LocalDateTime FLYTTEDATO = LocalDateTime.of(2016, 12, 1, 0, 0);
    private static final LocalDateTime INNVANDRING_DATO_2 = LocalDateTime.of(2018, 12, 1, 0, 0);

    @Mock
    private PersonRepository personRepository;

    @Mock
    private HentDatoFraIdentService hentDatoFraIdentService;

    @Mock
    private RelasjonNyePersonerBestillingService relasjonNyePersonerBestillingService;

    @Mock
    private MessageProvider messageProvider;

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

        when(hentDatoFraIdentService.extract(IDENT)).thenReturn(FOEDSEL_DATO);
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

        RsPersonBestillingKriteriumRequest request = new RsPersonBestillingKriteriumRequest();

        endrePersonBestillingService.execute(IDENT, request);

        assertThat(person.getInnvandretUtvandret(), hasSize(0));
    }

    @Test
    public void leggeTilFlereInnvandringUtvandringer_OK() {

        Person person = Person.builder()
                .ident(IDENT)
                .identtype(IDENTTYPE)
                .innvandretUtvandret(newArrayList(InnvandretUtvandret.builder()
                        .innutvandret(INNVANDRET)
                        .landkode("AUS")
                        .flyttedato(INNVANDRING_DATO)
                        .build()))
                .build();
        when(personRepository.findByIdent(IDENT)).thenReturn(person);

        RsPersonBestillingKriteriumRequest request = new RsPersonBestillingKriteriumRequest();
        request.setUtvandretTilLand("NRG");
        request.setUtvandretTilLandFlyttedato(FLYTTEDATO);
        request.setInnvandretFraLand("NRG");
        request.setInnvandretFraLandFlyttedato(INNVANDRING_DATO_2);

        endrePersonBestillingService.execute(IDENT, request);

        assertThat(person.getInnvandretUtvandret(), hasSize(3));
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