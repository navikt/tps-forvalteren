package no.nav.tps.forvalteren.provider.rs.api.v1.endpoints;

import ma.glasnost.orika.MapperFacade;
import no.nav.tps.forvalteren.domain.jpa.Gruppe;
import no.nav.tps.forvalteren.domain.jpa.Person;
import no.nav.tps.forvalteren.domain.rs.RsGruppe;
import no.nav.tps.forvalteren.domain.rs.RsPersonIdListe;
import no.nav.tps.forvalteren.domain.rs.RsPersonKriterieRequest;
import no.nav.tps.forvalteren.domain.rs.RsSimpleGruppe;
import no.nav.tps.forvalteren.repository.jpa.GruppeRepository;
import no.nav.tps.forvalteren.service.command.testdata.DeletePersonsByIdService;
import no.nav.tps.forvalteren.service.command.testdata.SavePersonListService;
import no.nav.tps.forvalteren.service.command.testdata.SjekkIdenter;
import no.nav.tps.forvalteren.service.command.testdata.opprett.EkstraherIdenterFraTestdataRequests;
import no.nav.tps.forvalteren.service.command.testdata.opprett.OpprettPersoner;
import no.nav.tps.forvalteren.service.command.testdata.opprett.SetGruppeIdOnPersons;
import no.nav.tps.forvalteren.service.command.testdata.opprett.SetNameOnPersonsService;
import no.nav.tps.forvalteren.service.command.testdata.opprett.TestdataIdenterFetcher;
import no.nav.tps.forvalteren.service.command.testdata.opprett.TestdataRequest;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class TestdataControllerTest {

    @Mock
    private MapperFacade mapper;

    @Mock
    private DeletePersonsByIdService deletePersonsByIdService;

    @Mock
    private SetNameOnPersonsService setNameOnPersonsService;

    @Mock
    private OpprettPersoner opprettPersonerFraIdenter;

    @Mock
    private EkstraherIdenterFraTestdataRequests ekstraherIdenterFraTestdataRequests;

    @Mock
    private TestdataIdenterFetcher testdataIdenterFetcher;

    @Mock
    private SavePersonListService savePersonListService;

    @Mock
    private SjekkIdenter sjekkIdenter;

    @Mock
    private SetGruppeIdOnPersons setGruppeIdOnPersons;

    @Mock
    private GruppeRepository gruppeRepository;

    @InjectMocks
    private TestdataController testdataController;

    private static final Long GRUPPE_ID = 0L;

    @Test
    public void createNewPersonsFromKriterier() {
        RsPersonKriterieRequest rsPersonKriterieRequest = new RsPersonKriterieRequest();

        TestdataRequest testdataRequest = new TestdataRequest(null);
        List<TestdataRequest> testdataRequestsList = new ArrayList<>();
        testdataRequestsList.add(testdataRequest);

        List<String> identer = new ArrayList<>();
        List<Person> personerSomSkalPersisteres = new ArrayList<>();

        when(testdataIdenterFetcher.getTestdataRequestsInnholdeneTilgjengeligeIdenter(any(RsPersonKriterieRequest.class))).thenReturn(testdataRequestsList);
        when(ekstraherIdenterFraTestdataRequests.execute(testdataRequestsList)).thenReturn(identer);
        when(opprettPersonerFraIdenter.execute(identer)).thenReturn(personerSomSkalPersisteres);

        testdataController.createNewPersonsFromKriterier(GRUPPE_ID, rsPersonKriterieRequest);

        verify(testdataIdenterFetcher).getTestdataRequestsInnholdeneTilgjengeligeIdenter(rsPersonKriterieRequest);
        verify(ekstraherIdenterFraTestdataRequests).execute(testdataRequestsList);
        verify(opprettPersonerFraIdenter).execute(identer);
        verify(setNameOnPersonsService).execute(personerSomSkalPersisteres);
        verify(setGruppeIdOnPersons).setGruppeId(personerSomSkalPersisteres, GRUPPE_ID);
        verify(savePersonListService).save(personerSomSkalPersisteres);

    }

    @Test
    public void deletePersons() {
        RsPersonIdListe rsPersonIdListe = new RsPersonIdListe();
        rsPersonIdListe.setIds(new ArrayList<>());

        testdataController.deletePersons(rsPersonIdListe);

        verify(deletePersonsByIdService).execute(rsPersonIdListe.getIds());
    }

    @Test
    public void updatePersons() {
        List<Person> personListe = new ArrayList<>();

        testdataController.updatePersons(personListe);

        verify(savePersonListService).save(personListe);
    }

    @Test
    public void checkIdentList() {
        List<String> personIdentListe = new ArrayList<>();

        testdataController.checkIdentList(personIdentListe);

        verify(sjekkIdenter).finnGyldigeOgLedigeIdenter(personIdentListe);
    }

    @Test
    public void createPersonerFraIdentliste() {
        List<String> personIdentListe = new ArrayList<>();
        List<Person> personer = new ArrayList<>();

        when(opprettPersonerFraIdenter.execute(personIdentListe)).thenReturn(personer);

        testdataController.createPersonerFraIdentliste(GRUPPE_ID, personIdentListe);

        verify(opprettPersonerFraIdenter).execute(personIdentListe);
        verify(setNameOnPersonsService).execute(personer);
        verify(setGruppeIdOnPersons).setGruppeId(personer, GRUPPE_ID);
        verify(savePersonListService).save(personer);
    }

    @Test
    public void getGrupper() {
        Gruppe gruppe = new Gruppe();
        RsSimpleGruppe rsSimpleGruppe = new RsSimpleGruppe();

        List<Gruppe> grupper = new ArrayList<>(Arrays.asList(gruppe));
        List<RsSimpleGruppe> rsSimpleGrupper = new ArrayList<>(Arrays.asList(rsSimpleGruppe));

        when(gruppeRepository.findAllByOrderByIdAsc()).thenReturn(grupper);
        when(mapper.mapAsList(grupper, RsSimpleGruppe.class)).thenReturn(rsSimpleGrupper);

        testdataController.getGrupper();

        verify(gruppeRepository).findAllByOrderByIdAsc();
        verify(mapper).mapAsList(grupper, RsSimpleGruppe.class);

    }

    @Test
    public void getGruppe() {
        Gruppe gruppe = new Gruppe();
        RsGruppe rsGruppe = new RsGruppe();

        when(gruppeRepository.findById(GRUPPE_ID)).thenReturn(gruppe);
        when(mapper.map(gruppe, RsGruppe.class)).thenReturn(rsGruppe);

        testdataController.getGruppe(GRUPPE_ID);

        verify(gruppeRepository).findById(GRUPPE_ID);
        verify(mapper).map(gruppe, RsGruppe.class);
    }

    @Test
    public void createGruppe() {
        RsSimpleGruppe rsGruppe = new RsSimpleGruppe();
        Gruppe gruppe = new Gruppe();

        when(mapper.map(rsGruppe, Gruppe.class)).thenReturn(gruppe);

        testdataController.createGruppe(rsGruppe);

        verify(gruppeRepository).save(gruppe);
    }

}