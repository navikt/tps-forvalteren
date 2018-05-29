package no.nav.tps.forvalteren.service.command.testdata.opprett;

import no.nav.tps.forvalteren.domain.rs.RsPersonKriteriumRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

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


    public void filtrerPaaIdenterSomIkkeFinnesIDB(List<TestdataRequest> testdataRequests){
        filtrerPaaIdenterSomIkkeFinnesIDB.execute(testdataRequests);
    }

    public void filtererUtMiljoeUtilgjengeligeIdenterFraTestdatarequest(List<TestdataRequest> testdataRequests){
        filtererUtIdenterSomAlleredeFinnesIMiljoe.execute(testdataRequests);
    }

    public List<TestdataRequest> genererIdenterForTestdataRequests(RsPersonKriteriumRequest personKriterierRequest){
        return genererIdenterForTestdataRequests.execute(personKriterierRequest);
    }

    public void taBortOverfloedigIdenterITestdataRequest(TestdataRequest request){
        taBortOverfloedigIdenterITestdataRequest.execute(request);
    }
}
