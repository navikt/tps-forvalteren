package no.nav.tps.forvalteren.service.command.testdatamal;

import java.util.List;

import no.nav.tps.forvalteren.domain.jpa.Person;
import no.nav.tps.forvalteren.domain.rs.RsPersonMal;
import no.nav.tps.forvalteren.service.command.testdata.SetGruppeIdAndSavePersonBulkTx;
import no.nav.tps.forvalteren.service.command.testdata.opprett.EkstraherIdenterFraTestdataRequests;
import no.nav.tps.forvalteren.service.command.testdata.opprett.OpprettPersoner;
import no.nav.tps.forvalteren.service.command.testdata.opprett.SetNameOnPersonsService;
import no.nav.tps.forvalteren.service.command.testdata.opprett.TestdataIdenterFetcher;
import no.nav.tps.forvalteren.service.command.testdata.opprett.TestdataRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CreateTestdataPerson {

    @Autowired
    private TestdataIdenterFetcher testdataIdenterFetcher;

    @Autowired
    private EkstraherIdenterFraTestdataRequests ekstraherIdenterFraTestdataRequests;

    @Autowired
    private OpprettPersoner opprettPersoner;

    @Autowired
    private SetNameOnPersonsService setNameOnPersonsService;

    @Autowired
    private SetGruppeIdAndSavePersonBulkTx setGruppeIdAndSavePersonBulkTx;

    public void execute(Long gruppeId, RsPersonMal rsPersonMal, int antallPersoner) {

//        List<TestdataRequest> testdataRequests = testdataIdenterFetcher.getTestdataRequestsInnholdeneTilgjengeligeIdenter(personKriterierListe);
//
//        List<String> identer = ekstraherIdenterFraTestdataRequests.execute(testdataRequests);
//        List<Person> personerSomSkalPersisteres = opprettPersonerFraIdenter.execute(identer);
//
//        if (personKriterierListe.isWithAdresse()) {
//            setDummyAdresseOnPersons.execute(personerSomSkalPersisteres);
//        }
//        setNameOnPersonsService.execute(personerSomSkalPersisteres);
//        setGruppeIdAndSavePersonBulkTx.execute(personerSomSkalPersisteres, gruppeId);

        List<TestdataRequest> testdataRequests = testdataIdenterFetcher.getTestdataRequestsInnholdeneTilgjengeligeIdenter(rsPersonMal, antallPersoner);

        List<String> identer = ekstraherIdenterFraTestdataRequests.execute(testdataRequests);
        List<Person> personerSomSkalPersisteres = opprettPersoner.execute(identer);

        setNameOnPersonsService.execute(personerSomSkalPersisteres);
        setGruppeIdAndSavePersonBulkTx.execute(personerSomSkalPersisteres, gruppeId);


    }
}
