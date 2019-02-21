package no.nav.tps.forvalteren.service.command.testdata.opprett;

import static com.google.common.collect.Lists.newArrayList;
import static org.assertj.core.util.Sets.newHashSet;

import java.util.List;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import no.nav.tps.forvalteren.domain.jpa.Person;
import no.nav.tps.forvalteren.domain.rs.RsPersonKriteriumRequest;

@Service
public class OpprettPersonerOgSjekkMiljoeService {

    @Autowired
    private FiltererUtIdenterSomAlleredeFinnesIMiljoe filtererUtIdenterSomAlleredeFinnesIMiljoe;

    @Autowired
    private FiltrerPaaIdenterSomIkkeFinnesIDB filtrerPaaIdenterSomIkkeFinnesIDB;

    @Autowired
    private EkstraherIdenterFraTestdataRequests ekstraherIdenterFraTestdataRequests;

    @Autowired
    private TestdataIdenterFetcher testdataIdenterFetcher;

    @Autowired
    private PersonNameService setNameOnPersonsService;

    @Autowired
    private OpprettPersonerService opprettPersonerFraIdenter;

    public List<Person> createEksisterendeIdenter(List<String> eksisterendeIdenter) {

        List<TestdataRequest> testdataRequests = newArrayList(TestdataRequest.builder()
                .identerGenerertForKriteria(newHashSet(eksisterendeIdenter))
                .build());
        filtererUtIdenterSomAlleredeFinnesIMiljoe.executeMotProdliktMiljoe(testdataRequests);
        filtrerPaaIdenterSomIkkeFinnesIDB.execute(testdataRequests);
        List<String> identer = ekstraherIdenterFraTestdataRequests.execute(testdataRequests);
        List<Person> personer = opprettPersonerFraIdenter.execute(identer);
        setNameOnPersonsService.execute(personer);

        return personer;
    }

    public List<Person> createNyeIdenter(RsPersonKriteriumRequest personKriterierListe, Set<String> environments) {
        List<TestdataRequest> testdataRequests =
                testdataIdenterFetcher.getTestdataRequestsInnholdeneTilgjengeligeIdenterFlereMiljoer(personKriterierListe, environments);

        List<String> identer = ekstraherIdenterFraTestdataRequests.execute(testdataRequests);
        List<Person> personerSomSkalPersisteres = opprettPersonerFraIdenter.execute(identer);

        setNameOnPersonsService.execute(personerSomSkalPersisteres);

        return personerSomSkalPersisteres;
    }
}
