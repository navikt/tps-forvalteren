package no.nav.tps.forvalteren.service.command.testdata.opprett.implementation;

import java.util.ArrayList;
import java.util.List;

import no.nav.tps.forvalteren.common.java.message.MessageProvider;
import no.nav.tps.forvalteren.domain.rs.RsPersonMal;
import no.nav.tps.forvalteren.domain.rs.RsPersonMalRequest;
import no.nav.tps.forvalteren.service.command.exceptions.HttpCantSatisfyRequestException;
import no.nav.tps.forvalteren.service.command.testdata.opprett.TestdataIdenterFetcher;
import no.nav.tps.forvalteren.service.command.testdata.opprett.TestdataRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DefaultTestdataIdenterFetcher implements TestdataIdenterFetcher {

    private static final int MAX_TRIES = 40;
    private static final Logger LOGGER = LoggerFactory.getLogger(DefaultTestdataIdenterFetcher.class);

    @Autowired
    private DefaultTestdata testdata;

    @Autowired
    private MessageProvider messageProvider;

    public List<TestdataRequest> getTestdataRequestsInnholdeneTilgjengeligeIdenter(RsPersonMalRequest inputPersonRequest) {
        List<TestdataRequest> testdataRequests = testdata.genererIdenterForTestdataRequests(inputPersonRequest);

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
                        RsPersonMalRequest singlePersonMalRequest = new RsPersonMalRequest();
                        singlePersonMalRequest.setInputPersonMalRequest(new ArrayList<>());
                        singlePersonMalRequest.getInputPersonMalRequest().add(request.getInputPerson());

                        List<TestdataRequest> testdataRequestSingelList = testdata.genererIdenterForTestdataRequests(singlePersonMalRequest);

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
            return request.getIdenterTilgjengligIMiljoe().size() >= request.getInputPerson().getAntallIdenter();
        }

}
