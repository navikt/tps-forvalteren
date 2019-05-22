package no.nav.tps.forvalteren.service.command.testdata.opprett;

import static org.assertj.core.util.Sets.newHashSet;

import java.util.List;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.google.common.collect.Sets;

import no.nav.tps.forvalteren.domain.jpa.Person;
import no.nav.tps.forvalteren.domain.rs.RsPersonKriteriumRequest;
import no.nav.tps.forvalteren.service.IdentpoolService;
import no.nav.tps.forvalteren.service.command.testdata.FiltrerPaaIdenterTilgjengeligIMiljo;

@Service
public class OpprettPersonerOgSjekkMiljoeService {

    private static final String PRODLIKE_ENV = "q0";

    @Autowired
    private FindIdenterNotUsedInDB findIdenterNotUsedInDB;

    @Autowired
    private FiltrerPaaIdenterTilgjengeligIMiljo filtrerPaaIdenterTilgjengeligIMiljo;

    @Autowired
    private EkstraherIdenterFraTestdataRequests ekstraherIdenterFraTestdataRequests;

    @Autowired
    private TestdataIdenterFetcher testdataIdenterFetcher;

    @Autowired
    private PersonNameService setNameOnPersonsService;

    @Autowired
    private OpprettPersonerService opprettPersonerFraIdenter;

    @Autowired
    private IdentpoolService identpoolService;

    public List<Person> createEksisterendeIdenter(List<String> eksisterendeIdenter) {

        Set<String> ledigeIdenterDB = findIdenterNotUsedInDB.filtrer(newHashSet(eksisterendeIdenter));

        Set<String> ledigeIdenterMiljo = filtrerPaaIdenterTilgjengeligIMiljo.filtrer(ledigeIdenterDB, Sets.newHashSet(PRODLIKE_ENV));

        Set<String> ledigeIdenterKorrigert = identpoolService.whitelistAjustmentOfIdents(eksisterendeIdenter, ledigeIdenterDB, ledigeIdenterMiljo);

        List<Person> personer = opprettPersonerFraIdenter.execute(ledigeIdenterKorrigert);
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
