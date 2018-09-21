package no.nav.tps.forvalteren.service.command.testdata.opprett;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import no.nav.tps.forvalteren.domain.rs.RsPersonKriteriumRequest;
import no.nav.tps.forvalteren.domain.rs.RsPersonMalRequest;

@Service
public class Testdata {

    @Autowired
    private TaBortOverfloedigIdenterITestdataRequest taBortOverfloedigIdenterITestdataRequest;

    @Autowired
    private GenererIdenterForTestdataRequests genererIdenterForTestdataRequests;

    @Autowired
    private FiltererUtIdenterSomAlleredeFinnesIMiljoe filtererUtIdenterSomAlleredeFinnesIMiljoe;

    @Autowired
    private FiltrerPaaIdenterSomIkkeFinnesIDB filtrerPaaIdenterSomIkkeFinnesIDB;

    public void filtrerPaaIdenterSomIkkeFinnesIDB(List<TestdataRequest> testdataRequests) {
        filtrerPaaIdenterSomIkkeFinnesIDB.execute(testdataRequests);
    }

    public void filtererUtMiljoeUtilgjengeligeIdenterFraTestdatarequest(List<TestdataRequest> testdataRequests) {
        filtererUtIdenterSomAlleredeFinnesIMiljoe.execute(testdataRequests);
    }

    public List<TestdataRequest> genererIdenterForTestdataRequests(RsPersonMalRequest inputPersonRequest) {
        return genererIdenterForTestdataRequests.execute(inputPersonRequest);
    }

    public List<TestdataRequest> genererIdenterForTestdataRequests(RsPersonKriteriumRequest personKriterierRequest) {
        return genererIdenterForTestdataRequests.execute(personKriterierRequest);
    }

    public void taBortOverfloedigIdenterITestdataRequest(TestdataRequest request) {
        taBortOverfloedigIdenterITestdataRequest.execute(request);
    }
}