package no.nav.tps.forvalteren.service.command.testdata;

import no.nav.tps.forvalteren.domain.jpa.Person;
import no.nav.tps.forvalteren.domain.rs.RsPersonKriterieRequest;
import no.nav.tps.forvalteren.domain.rs.RsPersonKriterier;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class OpprettTestdataPersonerTest {

    private RsPersonKriterieRequest rsPersonKriterieRequest = new RsPersonKriterieRequest();
    private HashSet<String> dummyFiktiveIdenterFraGenerator = new HashSet<>();
    private HashSet<String> dummyFiktiveIdenterFraGenerator2 = new HashSet<>();
    private HashSet<String> dummyTilgjengeligeFiktiveIdenter = new HashSet<>();
    private HashSet<String> dummyTilgjengeligeFiktiveIdenter2 = new HashSet<>();
    private String dummyId1 = "dummy1";
    private String dummyId2 = "dummy2";
    private String dummyId3 = "dummy2";
    private String dummyId4 = "dummy2";
    private String dummyId5 = "dummy2";

    @Mock
    private FiktiveIdenterGenerator fiktiveIdenterGeneratorMock;

    @Mock
    private FilterPaaIdenterTilgjengeligeIMiljo filterPaaIdenterTilgjengeligeIMiljoMock;

    @Mock
    private SetNameOnPersonsService setNameOnPersonsServiceMock;

    @Mock
    private SavePersonListService savePersonListServiceMock;

    @Mock
    private FindAllExistingIdenterInDB findAllExistingIdenterInDB;

    @InjectMocks
    private OpprettTestdataPersoner opprettTestdataPersoner;

    @Test
    public void manOppretterLikeMangePersonerSomManSpoerOmHvisAltGaarGreit() throws Exception {
        RsPersonKriterier rsPersonKriterier = new RsPersonKriterier();
        rsPersonKriterier.setAntall(1);
        RsPersonKriterier rsPersonKriterier2 = new RsPersonKriterier();
        rsPersonKriterier2.setAntall(1);

        List<RsPersonKriterier> personKriteriers = new ArrayList<>();
        personKriteriers.add(rsPersonKriterier);
        personKriteriers.add(rsPersonKriterier2);

        rsPersonKriterieRequest.setPersonKriterierListe(personKriteriers);

        dummyFiktiveIdenterFraGenerator.add(dummyId1);
        dummyTilgjengeligeFiktiveIdenter.add(dummyId1);
        dummyFiktiveIdenterFraGenerator2.add(dummyId2);
        dummyTilgjengeligeFiktiveIdenter.add(dummyId2);

        when(fiktiveIdenterGeneratorMock.genererFiktiveIdenter(any(RsPersonKriterier.class))).thenReturn(dummyFiktiveIdenterFraGenerator, dummyFiktiveIdenterFraGenerator2);
        when(filterPaaIdenterTilgjengeligeIMiljoMock.filtrer(any(Set.class))).thenReturn(dummyTilgjengeligeFiktiveIdenter, dummyTilgjengeligeFiktiveIdenter2);
        when(findAllExistingIdenterInDB.filtrer(any(Set.class))).thenReturn(dummyTilgjengeligeFiktiveIdenter, dummyFiktiveIdenterFraGenerator2);

        List<Person> personer = opprettTestdataPersoner.hentIdenterSomSkalBliPersoner(rsPersonKriterieRequest);

        assertThat(personer, hasSize(2));
        assertTrue(personer.get(0).getIdent().equals(dummyId1));
        assertTrue(personer.get(1).getIdent().equals(dummyId2));
    }

    @Test
    public void hvisEtNummerIkkeErTilgjengeligSaaBlirDetTattBortOgIkkeBruktTilAaOpprettePerson() throws Exception {
        RsPersonKriterier rsPersonKriterier = new RsPersonKriterier();
        rsPersonKriterier.setAntall(1);

        List<RsPersonKriterier> personKriteriers = new ArrayList<>();
        personKriteriers.add(rsPersonKriterier);

        rsPersonKriterieRequest.setPersonKriterierListe(personKriteriers);

        dummyFiktiveIdenterFraGenerator.add(dummyId2);
        dummyFiktiveIdenterFraGenerator.add(dummyId1);

        dummyTilgjengeligeFiktiveIdenter.add(dummyId1);

        when(fiktiveIdenterGeneratorMock.genererFiktiveIdenter(any(RsPersonKriterier.class))).thenReturn(dummyFiktiveIdenterFraGenerator);
        when(filterPaaIdenterTilgjengeligeIMiljoMock.filtrer(any(Set.class))).thenReturn(dummyTilgjengeligeFiktiveIdenter);
        when(findAllExistingIdenterInDB.filtrer(any(Set.class))).thenReturn(dummyTilgjengeligeFiktiveIdenter, dummyFiktiveIdenterFraGenerator2);

        List<Person> personer = opprettTestdataPersoner.hentIdenterSomSkalBliPersoner(rsPersonKriterieRequest);

        assertThat(personer, hasSize(1));
        assertTrue(personer.get(0).getIdent().equals(dummyId1));
    }

    @Test
    public void hvisManIkkeFinnerNokTilgjengeligeIdenterFoersteForsoekSaaSpoerManIgjenTilManFaarNokNummer() throws Exception {
        RsPersonKriterier rsPersonKriterier = new RsPersonKriterier();
        rsPersonKriterier.setAntall(1);

        List<RsPersonKriterier> personKriteriers = new ArrayList<>();
        personKriteriers.add(rsPersonKriterier);

        rsPersonKriterieRequest.setPersonKriterierListe(personKriteriers);

        dummyFiktiveIdenterFraGenerator.add(dummyId1);
        dummyFiktiveIdenterFraGenerator2.add(dummyId2);

        dummyTilgjengeligeFiktiveIdenter.add(dummyId2);

        when(fiktiveIdenterGeneratorMock.genererFiktiveIdenter(any(RsPersonKriterier.class)))
                                            .thenReturn(dummyFiktiveIdenterFraGenerator,
                                                        dummyFiktiveIdenterFraGenerator2);

        when(filterPaaIdenterTilgjengeligeIMiljoMock.filtrer(any(Set.class))).thenReturn(dummyTilgjengeligeFiktiveIdenter);

        List<Person> personer = opprettTestdataPersoner.hentIdenterSomSkalBliPersoner(rsPersonKriterieRequest);

        assertThat(personer, hasSize(1));
        assertTrue(personer.get(0).getIdent().equals(dummyId2));
    }

    @Test
    public void hvisManIkkeFinnerNokTilgjengeligeIdenterFor1AvKriterieneSaaFinnerManNyeIdenterForDetKriterie() throws Exception {
        //TODO Uferdig. Er naa en annen test.
        RsPersonKriterier rsPersonKriterier = new RsPersonKriterier();
        rsPersonKriterier.setAntall(1);

        List<RsPersonKriterier> personKriteriers = new ArrayList<>();
        personKriteriers.add(rsPersonKriterier);

        rsPersonKriterieRequest.setPersonKriterierListe(personKriteriers);

        dummyFiktiveIdenterFraGenerator.add(dummyId1);
        dummyFiktiveIdenterFraGenerator2.add(dummyId2);

        dummyTilgjengeligeFiktiveIdenter.add(dummyId2);

        when(fiktiveIdenterGeneratorMock.genererFiktiveIdenter(any(RsPersonKriterier.class)))
                .thenReturn(dummyFiktiveIdenterFraGenerator,
                        dummyFiktiveIdenterFraGenerator2);

        when(filterPaaIdenterTilgjengeligeIMiljoMock.filtrer(any(Set.class))).thenReturn(dummyTilgjengeligeFiktiveIdenter);

        List<Person> personer = opprettTestdataPersoner.hentIdenterSomSkalBliPersoner(rsPersonKriterieRequest);

        assertThat(personer, hasSize(1));
        assertTrue(personer.get(0).getIdent().equals(dummyId2));
    }

    @Test
    public void hvisFlereKriterierGenererSammeFiktiveIdenterSaaBlirDeTattBortFraEnAvKriteriene() throws Exception {

    }

    @Test
    public void hvisManIkkeFinnerNokTilgjengeligeIdenterPaa20ForsoekSaaKastestException() throws Exception {

    }

    @Test
    public void identerSomFinnesIDatabasenBlirIkkeBrukt() {
        RsPersonKriterier rsPersonKriterier = new RsPersonKriterier();
        rsPersonKriterier.setAntall(1);
        RsPersonKriterier rsPersonKriterier2 = new RsPersonKriterier();
        rsPersonKriterier2.setAntall(1);

        List<RsPersonKriterier> personKriteriers = new ArrayList<>();
        personKriteriers.add(rsPersonKriterier);
        personKriteriers.add(rsPersonKriterier2);

        rsPersonKriterieRequest.setPersonKriterierListe(personKriteriers);

        dummyFiktiveIdenterFraGenerator.add(dummyId1);
        dummyTilgjengeligeFiktiveIdenter.add(dummyId1);
        dummyFiktiveIdenterFraGenerator2.add(dummyId2);
        dummyTilgjengeligeFiktiveIdenter.add(dummyId2);

        when(fiktiveIdenterGeneratorMock.genererFiktiveIdenter(any(RsPersonKriterier.class))).thenReturn(dummyFiktiveIdenterFraGenerator, dummyFiktiveIdenterFraGenerator2);
        when(filterPaaIdenterTilgjengeligeIMiljoMock.filtrer(any(Set.class))).thenReturn(dummyTilgjengeligeFiktiveIdenter, dummyTilgjengeligeFiktiveIdenter2);
        when(findAllExistingIdenterInDB.filtrer(any(Set.class))).thenReturn(dummyTilgjengeligeFiktiveIdenter, dummyFiktiveIdenterFraGenerator2);

        List<Person> personer = opprettTestdataPersoner.hentIdenterSomSkalBliPersoner(rsPersonKriterieRequest);

        assertThat(personer, hasSize(2));
        assertTrue(personer.get(0).getIdent().equals(dummyId1));
        assertTrue(personer.get(1).getIdent().equals(dummyId2));
    }











}