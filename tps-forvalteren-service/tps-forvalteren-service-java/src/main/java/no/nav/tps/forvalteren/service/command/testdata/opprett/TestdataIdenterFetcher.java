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
    private TestdataService testdataService;

    @Autowired
    private MessageProvider messageProvider;

    public List<TestdataRequest> getTestdataRequestsInnholdeneTilgjengeligeIdenter(RsPersonKriteriumRequest personKriterierListe) {
        List<TestdataRequest> testdataRequests = lagTestdatarequester(personKriterierListe);

        if (!erAlleKriteriaOppfylt(testdataRequests)) {
            oppdaterTestdataRequestsMedIdenterTilManglendeKriterier(testdataRequests, false, null);
        }

        return testdataRequests;
    }

    public List<TestdataRequest> getTestdataRequestsInnholdeneTilgjengeligeIdenterFlereMiljoer(RsPersonKriteriumRequest personKriterierListe, List<String> miljoer) {
        List<TestdataRequest> testdataRequests = lagTestdatarequester(personKriterierListe);

        if (!erAlleKriteriaOppfylt(testdataRequests)) {
            oppdaterTestdataRequestsMedIdenterTilManglendeKriterier(testdataRequests, true, miljoer);
        }

        return testdataRequests;
    }

    public List<TestdataRequest> getTestdataRequestsInnholdeneTilgjengeligeIdenterAlleMiljoer(RsPersonKriteriumRequest personKriterierListe) {
        List<TestdataRequest> testdataRequests = lagTestdatarequester(personKriterierListe);

        if (!erAlleKriteriaOppfylt(testdataRequests)) {
            oppdaterTestdataRequestsMedIdenterTilManglendeKriterier(testdataRequests, true, null);
        }

        return testdataRequests;
    }

    private List<TestdataRequest> lagTestdatarequester(RsPersonKriteriumRequest personKriterierListe){
        List<TestdataRequest> testdataRequests = testdataService.genererIdenterForTestdataRequests(personKriterierListe);

        testdataService.filtererUtMiljoeUtilgjengeligeIdenterFraTestdatarequestAlleMiljoer(testdataRequests, null);

        testdataService.filtrerPaaIdenterSomIkkeFinnesIDB(testdataRequests);

        taBortOverflodigeIdenterFraTestRequests(testdataRequests);

        return testdataRequests;
    }

    private void oppdaterTestdataRequestsMedIdenterTilManglendeKriterier(List<TestdataRequest> testdataRequests, boolean alleMiljoer, List<String> miljoer) {
        for (TestdataRequest request : testdataRequests) {
            if (!harNokIdenterForKritereIRequest(request)) {
                int counter = 0;
                while ((counter < MAX_TRIES) && !harNokIdenterForKritereIRequest(request)) {
                    RsPersonKriteriumRequest singelKriterieListe = new RsPersonKriteriumRequest();
                    singelKriterieListe.setPersonKriterierListe(new ArrayList<>());
                    singelKriterieListe.getPersonKriterierListe().add(request.getKriterium());

                    List<TestdataRequest> testdataRequestSingelList = testdataService.genererIdenterForTestdataRequests(singelKriterieListe);

                    if(alleMiljoer){
                        testdataService.filtererUtMiljoeUtilgjengeligeIdenterFraTestdatarequestAlleMiljoer(testdataRequestSingelList, miljoer);
                    } else {
                        testdataService.filtererUtMiljoeUtilgjengeligeIdenterFraTestdatarequest(testdataRequestSingelList);
                    }

                    testdataService.filtrerPaaIdenterSomIkkeFinnesIDB(testdataRequestSingelList);

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
        testdataService.taBortOverfloedigIdenterITestdataRequest(request);
    }

    private void taBortOverflodigeIdenterFraTestRequests(List<TestdataRequest> requests) {
        for (TestdataRequest request : requests) {
            testdataService.taBortOverfloedigIdenterITestdataRequest(request);
        }
    }

    private boolean erAlleKriteriaOppfylt(List<TestdataRequest> testdataRequests) {
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
