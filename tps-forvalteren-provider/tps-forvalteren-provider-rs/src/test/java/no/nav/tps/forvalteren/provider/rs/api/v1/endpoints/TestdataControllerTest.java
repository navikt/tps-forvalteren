package no.nav.tps.forvalteren.provider.rs.api.v1.endpoints;

import no.nav.tps.forvalteren.domain.jpa.Person;
import no.nav.tps.forvalteren.domain.rs.RsPersonIdListe;
import no.nav.tps.forvalteren.domain.rs.RsPersonKriterieRequest;
import no.nav.tps.forvalteren.service.command.testdata.DeletePersonsByIdService;
import no.nav.tps.forvalteren.service.command.testdata.FindAllPersonService;
import no.nav.tps.forvalteren.service.command.testdata.SavePersonListService;
import no.nav.tps.forvalteren.service.command.testdata.SjekkIdenter;
import no.nav.tps.forvalteren.service.command.testdata.opprett.EkstraherIdenterFraTestdataRequests;
import no.nav.tps.forvalteren.service.command.testdata.opprett.OpprettPersoner;
import no.nav.tps.forvalteren.service.command.testdata.opprett.SetNameOnPersonsService;
import no.nav.tps.forvalteren.service.command.testdata.opprett.TestdataIdenterFetcher;
import no.nav.tps.forvalteren.service.command.testdata.opprett.TestdataRequest;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class TestdataControllerTest {

    @Mock
    private FindAllPersonService findAllPersonService;

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

    @InjectMocks
    private TestdataController testdataController;

    private static List<Person> allPersons = new ArrayList<>();

    @Test
    public void findAllPersonReturnsAll() {
        Person person = new Person();
        allPersons.add(person);
        when(findAllPersonService.execute()).thenReturn(allPersons);

        List<Person> result = testdataController.getAllPersons();

        verify(findAllPersonService).execute();

        assertThat(result, hasSize(1));
    }

    @Test
    public void createNewPersons() {
        RsPersonKriterieRequest rsPersonKriterieRequest = new RsPersonKriterieRequest();

        TestdataRequest testdataRequest = new TestdataRequest(null);
        List<TestdataRequest> testdataRequestsList = new ArrayList<>();
        testdataRequestsList.add(testdataRequest);

        List<String> identer = new ArrayList<>();
        List<Person> personerSomSkalPersisteres = new ArrayList<>();

        when(testdataIdenterFetcher.getTestdataRequestsInnholdeneTilgjengeligeIdenter(any(RsPersonKriterieRequest.class))).thenReturn(testdataRequestsList);
        when(ekstraherIdenterFraTestdataRequests.execute(testdataRequestsList)).thenReturn(identer);
        when(opprettPersonerFraIdenter.execute(identer)).thenReturn(personerSomSkalPersisteres);

        testdataController.createNewPersons(rsPersonKriterieRequest);

        verify(testdataIdenterFetcher).getTestdataRequestsInnholdeneTilgjengeligeIdenter(rsPersonKriterieRequest);
        verify(ekstraherIdenterFraTestdataRequests).execute(testdataRequestsList);
        verify(opprettPersonerFraIdenter).execute(identer);
        verify(setNameOnPersonsService).execute(personerSomSkalPersisteres);
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

        testdataController.createPersonerFraIdentliste(personIdentListe);

        verify(opprettPersonerFraIdenter).execute(personIdentListe);
        verify(setNameOnPersonsService).execute(personer);
        verify(savePersonListService).save(personer);
    }
}