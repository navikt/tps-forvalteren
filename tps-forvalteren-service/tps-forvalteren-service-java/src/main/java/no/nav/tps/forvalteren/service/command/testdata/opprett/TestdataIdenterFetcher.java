package no.nav.tps.forvalteren.service.command.testdata.opprett;

import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import no.nav.tps.forvalteren.common.java.message.MessageProvider;
import no.nav.tps.forvalteren.domain.rs.RsPersonKriteriumRequest;
import no.nav.tps.forvalteren.service.command.exceptions.HttpCantSatisfyRequestException;

@Service
public class TestdataIdenterFetcher {

    private static final int MAX_TRIES = 40;
    private static final Logger LOGGER = LoggerFactory.getLogger(TestdataIdenterFetcher.class);

    @Autowired
    private Testdata testdata;

    @Autowired
    private MessageProvider messageProvider;

    public List<TestdataRequest> getTestdataRequestsInnholdeneTilgjengeligeIdenter(RsPersonKriteriumRequest personKriterierListe) {
        List<TestdataRequest> testdataRequests = testdata.genererIdenterForTestdataRequests(personKriterierListe);

        testdata.filtererUtMiljoeUtilgjengeligeIdenterFraTestdatarequest(testdataRequests);

        testdata.filtrerPaaIdenterSomIkkeFinnesIDB(testdataRequests);

        taBortOverflodigeIdenterFraTestRequests(testdataRequests);

        if (!erAlleKriterieOppfylt(testdataRequests)) {
            oppdaterTestdataRequestsMedIdenterTilManglendeKriterier(testdataRequests);
        }

        return testdataRequests;
    }

    private void oppdaterTestdataRequestsMedIdenterTilManglendeKriterier(List<TestdataRequest> testdataRequests) {
        for (TestdataRequest request : testdataRequests) {
            if (!harNokIdenterForKritereIRequest(request)) {
                int counter = 0;
                while ((counter < MAX_TRIES) && !harNokIdenterForKritereIRequest(request)) {
                    RsPersonKriteriumRequest singelKriterieListe = new RsPersonKriteriumRequest();
                    singelKriterieListe.setPersonKriterierListe(new ArrayList<>());
                    singelKriterieListe.getPersonKriterierListe().add(request.getKriterium());

                    List<TestdataRequest> testdataRequestSingelList = testdata.genererIdenterForTestdataRequests(singelKriterieListe);

                    testdata.filtererUtMiljoeUtilgjengeligeIdenterFraTestdatarequest(testdataRequestSingelList);

                    testdata.filtrerPaaIdenterSomIkkeFinnesIDB(testdataRequestSingelList);

                    taBortOverflodigeIdenterFraTestRequests(testdataRequestSingelList);

                    request.getIdenterTilgjengligIMiljoe().addAll(testdataRequestSingelList.get(0).getIdenterTilgjengligIMiljoe());

                    taBortOverflodigeIdenterFraTestRequest(request);

                    counter++;
                }
                if (counter == MAX_TRIES) {
                    HttpCantSatisfyRequestException exception = new HttpCantSatisfyRequestException(messageProvider.get("rest.service.request.exception.Unsatisfied"), "api/v1/testdata/");
                    LOGGER.error(exception.getMessage(), exception);
                    throw exception;
                }
            }
        }
    }

    private void taBortOverflodigeIdenterFraTestRequest(TestdataRequest request) {
        testdata.taBortOverfloedigIdenterITestdataRequest(request);
    }

    private void taBortOverflodigeIdenterFraTestRequests(List<TestdataRequest> requests) {
        for (TestdataRequest request : requests) {
            testdata.taBortOverfloedigIdenterITestdataRequest(request);
        }
    }

    private boolean erAlleKriterieOppfylt(List<TestdataRequest> testdataRequests) {
        for (TestdataRequest request : testdataRequests) {
            if (!harNokIdenterForKritereIRequest(request)) {
                return false;
            }
        }
        return true;
    }

    private boolean harNokIdenterForKritereIRequest(TestdataRequest request) {
        return request.getIdenterTilgjengligIMiljoe().size() >= request.getKriterium().getAntall();
    }

}
