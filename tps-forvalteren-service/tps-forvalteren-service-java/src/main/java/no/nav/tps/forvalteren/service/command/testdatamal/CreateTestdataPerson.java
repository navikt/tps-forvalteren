package no.nav.tps.forvalteren.service.command.testdatamal;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import no.nav.tps.forvalteren.domain.jpa.Person;
import no.nav.tps.forvalteren.domain.rs.RsPersonMalRequest;
import no.nav.tps.forvalteren.service.command.testdata.SetGruppeIdAndSavePersonBulkTx;
import no.nav.tps.forvalteren.service.command.testdata.opprett.EkstraherIdenterFraTestdataRequests;
import no.nav.tps.forvalteren.service.command.testdata.opprett.OpprettPersonerService;
import no.nav.tps.forvalteren.service.command.testdata.opprett.PersonNameService;
import no.nav.tps.forvalteren.service.command.testdata.opprett.SetValuesFromMalOnPersonsService;
import no.nav.tps.forvalteren.service.command.testdata.opprett.TestdataIdenterFetcher;
import no.nav.tps.forvalteren.service.command.testdata.opprett.TestdataRequest;

@Service
public class CreateTestdataPerson {

    @Autowired
    private TestdataIdenterFetcher testdataIdenterFetcher;

    @Autowired
    private EkstraherIdenterFraTestdataRequests ekstraherIdenterFraTestdataRequests;

    @Autowired
    private OpprettPersonerService opprettPersoner;

    @Autowired
    private PersonNameService setNameOnPersonsService;

    @Autowired
    private SetGruppeIdAndSavePersonBulkTx setGruppeIdAndSavePersonBulkTx;

    @Autowired
    private SetValuesFromMalOnPersonsService setValuesFromMalOnPersonsService;

    public void execute(Long gruppeId, RsPersonMalRequest inputPersonRequest) {

        List<TestdataRequest> testdataRequests = testdataIdenterFetcher.getTestdataRequestsInnholdeneTilgjengeligeIdenter(inputPersonRequest);

        List<String> identer = ekstraherIdenterFraTestdataRequests.execute(testdataRequests);
        List<Person> personerSomSkalPersisteres = opprettPersoner.execute(identer);

        setNameOnPersonsService.execute(personerSomSkalPersisteres);
        setValuesFromMalOnPersonsService.execute(personerSomSkalPersisteres, inputPersonRequest);
        setGruppeIdAndSavePersonBulkTx.execute(personerSomSkalPersisteres, gruppeId);
    }
}
