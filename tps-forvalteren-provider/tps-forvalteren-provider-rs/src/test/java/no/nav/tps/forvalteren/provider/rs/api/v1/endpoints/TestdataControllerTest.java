package no.nav.tps.forvalteren.provider.rs.api.v1.endpoints;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anySet;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import ma.glasnost.orika.MapperFacade;
import no.nav.tps.forvalteren.domain.jpa.Gruppe;
import no.nav.tps.forvalteren.domain.jpa.Person;
import no.nav.tps.forvalteren.domain.jpa.SkdEndringsmeldingGruppe;
import no.nav.tps.forvalteren.domain.rs.RsGruppe;
import no.nav.tps.forvalteren.domain.rs.RsPerson;
import no.nav.tps.forvalteren.domain.rs.RsPersonIdListe;
import no.nav.tps.forvalteren.domain.rs.RsPersonKriterier;
import no.nav.tps.forvalteren.domain.rs.RsPersonKriteriumRequest;
import no.nav.tps.forvalteren.domain.rs.RsSimpleGruppe;
import no.nav.tps.forvalteren.domain.rs.skd.RsSkdEndringsmeldingGruppe;
import no.nav.tps.forvalteren.service.command.testdata.DeleteGruppeById;
import no.nav.tps.forvalteren.service.command.testdata.DeletePersonerByIdIn;
import no.nav.tps.forvalteren.service.command.testdata.FindAlleGrupperOrderByIdAsc;
import no.nav.tps.forvalteren.service.command.testdata.FindGruppeById;
import no.nav.tps.forvalteren.service.command.testdata.SaveGruppe;
import no.nav.tps.forvalteren.service.command.testdata.SavePersonListService;
import no.nav.tps.forvalteren.service.command.testdata.SetGruppeIdAndSavePersonBulkTx;
import no.nav.tps.forvalteren.service.command.testdata.SjekkIdenterService;
import no.nav.tps.forvalteren.service.command.testdata.TestdataGruppeToSkdEndringsmeldingGruppe;
import no.nav.tps.forvalteren.service.command.testdata.opprett.EkstraherIdenterFraTestdataRequests;
import no.nav.tps.forvalteren.service.command.testdata.opprett.OpprettPersonerService;
import no.nav.tps.forvalteren.service.command.testdata.opprett.PersonNameService;
import no.nav.tps.forvalteren.service.command.testdata.opprett.RandomAdresseService;
import no.nav.tps.forvalteren.service.command.testdata.opprett.SetGruppeIdOnPersons;
import no.nav.tps.forvalteren.service.command.testdata.opprett.TestdataIdenterFetcher;
import no.nav.tps.forvalteren.service.command.testdata.opprett.TestdataRequest;
import no.nav.tps.forvalteren.service.command.testdata.skd.LagreTilTpsService;

@RunWith(MockitoJUnitRunner.class)
public class TestdataControllerTest {

    @Mock
    private MapperFacade mapper;

    @Mock
    private PersonNameService personNameService;

    @Mock
    private OpprettPersonerService opprettPersonerServiceFraIdenter;

    @Mock
    private EkstraherIdenterFraTestdataRequests ekstraherIdenterFraTestdataRequests;

    @Mock
    private TestdataIdenterFetcher testdataIdenterFetcher;

    @Mock
    private SavePersonListService savePersonListService;

    @Mock
    private SjekkIdenterService sjekkIdenterService;

    @Mock
    private SetGruppeIdOnPersons setGruppeIdOnPersons;

    @Mock
    private FindAlleGrupperOrderByIdAsc findAlleGrupperOrderByIdAsc;

    @Mock
    private FindGruppeById findGruppeById;

    @Mock
    private DeletePersonerByIdIn deletePersonerByIdIn;

    @Mock
    private SaveGruppe saveGruppe;

    @Mock
    private LagreTilTpsService lagreTilTpsService;

    @Mock
    private DeleteGruppeById deleteGruppeById;

    @Mock
    private TestdataGruppeToSkdEndringsmeldingGruppe testdataGruppeToSkdEndringsmeldingGruppe;

    @Mock
    private SetGruppeIdAndSavePersonBulkTx setGruppeIdAndSavePersonBulkTx;

    @Mock
    private RandomAdresseService randomAdresseService;

    @InjectMocks
    private TestdataController testdataController;

    private static final Long GRUPPE_ID = 0L;

    @Test
    public void createNewPersonsFromKriterier() {
        RsPersonKriteriumRequest rsPersonKriteriumRequest = new RsPersonKriteriumRequest();

        TestdataRequest testdataRequest = new TestdataRequest((RsPersonKriterier) null);
        List<TestdataRequest> testdataRequestsList = new ArrayList<>();
        testdataRequestsList.add(testdataRequest);

        List<String> identer = new ArrayList<>();
        List<Person> personerSomSkalPersisteres = new ArrayList<>();

        when(testdataIdenterFetcher.getTestdataRequestsInnholdeneTilgjengeligeIdenter(any(RsPersonKriteriumRequest.class))).thenReturn(testdataRequestsList);
        when(ekstraherIdenterFraTestdataRequests.execute(testdataRequestsList)).thenReturn(identer);
        when(opprettPersonerServiceFraIdenter.execute(identer)).thenReturn(personerSomSkalPersisteres);

        testdataController.createNewPersonsFromKriterier(GRUPPE_ID, rsPersonKriteriumRequest);

        verify(testdataIdenterFetcher).getTestdataRequestsInnholdeneTilgjengeligeIdenter(rsPersonKriteriumRequest);
        verify(ekstraherIdenterFraTestdataRequests).execute(testdataRequestsList);
        verify(opprettPersonerServiceFraIdenter).execute(identer);
        verify(personNameService).execute(personerSomSkalPersisteres);
        verify(setGruppeIdAndSavePersonBulkTx).execute(personerSomSkalPersisteres, GRUPPE_ID);
    }

    @Test
    public void deletePersons() {
        RsPersonIdListe rsPersonIdListe = new RsPersonIdListe();
        rsPersonIdListe.setIds(new ArrayList<>());

        testdataController.deletePersons(rsPersonIdListe);

        verify(deletePersonerByIdIn).execute(rsPersonIdListe.getIds());
    }

    @Test
    public void updatePersons() {
        List<RsPerson> rsPersonListe = new ArrayList<>();
        List<Person> personListe = new ArrayList<>();

        when(mapper.mapAsList(rsPersonListe, Person.class)).thenReturn(personListe);

        testdataController.updatePersons(rsPersonListe);

        verify(savePersonListService).execute(personListe);
        verify(mapper).mapAsList(rsPersonListe, Person.class);
    }

    @Test
    public void checkIdentList() {
        List<String> personIdentListe = new ArrayList<>();

        testdataController.checkIdentList(personIdentListe);

        verify(sjekkIdenterService).finnGyldigeOgLedigeIdenter(personIdentListe);
    }

    @Test
    public void createPersonerFraIdentliste() {
        List<String> personIdentListe = new ArrayList<>();
        List<Person> personer = new ArrayList<>();

        when(opprettPersonerServiceFraIdenter.execute(personIdentListe)).thenReturn(personer);

        testdataController.createPersonerFraIdentliste(GRUPPE_ID, personIdentListe);

        verify(opprettPersonerServiceFraIdenter).execute(personIdentListe);
        verify(personNameService).execute(personer);
        verify(setGruppeIdOnPersons).setGruppeId(personer, GRUPPE_ID);
        verify(savePersonListService).execute(personer);
    }

    @Test
    public void lagreTilTips() {
        testdataController.lagreTilTPS(GRUPPE_ID, new ArrayList<>());

        verify(lagreTilTpsService).execute(eq(GRUPPE_ID), anySet());
    }

    @Test
    public void getGrupper() {
        Gruppe gruppe = new Gruppe();
        RsSimpleGruppe rsSimpleGruppe = new RsSimpleGruppe();

        List<Gruppe> grupper = new ArrayList<>(Arrays.asList(gruppe));
        List<RsSimpleGruppe> rsSimpleGrupper = new ArrayList<>(Arrays.asList(rsSimpleGruppe));

        when(findAlleGrupperOrderByIdAsc.execute()).thenReturn(grupper);
        when(mapper.mapAsList(grupper, RsSimpleGruppe.class)).thenReturn(rsSimpleGrupper);

        testdataController.getGrupper();

        verify(findAlleGrupperOrderByIdAsc).execute();
        verify(mapper).mapAsList(grupper, RsSimpleGruppe.class);
    }

    @Test
    public void getGruppe() {
        Gruppe gruppe = new Gruppe();
        RsGruppe rsGruppe = new RsGruppe();

        when(findGruppeById.execute(GRUPPE_ID)).thenReturn(gruppe);
        when(mapper.map(gruppe, RsGruppe.class)).thenReturn(rsGruppe);

        testdataController.getGruppe(GRUPPE_ID);

        verify(findGruppeById).execute(GRUPPE_ID);
        verify(mapper).map(gruppe, RsGruppe.class);
    }

    @Test
    public void createGruppe() {
        RsSimpleGruppe rsGruppe = new RsSimpleGruppe();
        Gruppe gruppe = new Gruppe();

        when(mapper.map(rsGruppe, Gruppe.class)).thenReturn(gruppe);

        testdataController.createGruppe(rsGruppe);

        verify(saveGruppe).execute(gruppe);
    }

    @Test
    public void deleteGruppe() {
        testdataController.deleteGruppe(GRUPPE_ID);

        verify(deleteGruppeById).execute(GRUPPE_ID);
    }

    @Test
    public void testdataGruppeToSkdEndringsmeldingGruppe() {
        SkdEndringsmeldingGruppe gruppe = new SkdEndringsmeldingGruppe();
        when(testdataGruppeToSkdEndringsmeldingGruppe.execute(GRUPPE_ID)).thenReturn(gruppe);
        testdataController.testdataGruppeToSkdEndringsmeldingGruppe(GRUPPE_ID);
        verify(testdataGruppeToSkdEndringsmeldingGruppe).execute(GRUPPE_ID);
        verify(mapper).map(gruppe, RsSkdEndringsmeldingGruppe.class);
    }
}