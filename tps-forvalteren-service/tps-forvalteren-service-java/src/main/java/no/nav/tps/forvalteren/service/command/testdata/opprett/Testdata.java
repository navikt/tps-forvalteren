package no.nav.tps.forvalteren.service.command.testdata.opprett;

import no.nav.tps.forvalteren.domain.rs.RsPersonKriterieRequest;
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
    private FiltererUtMiljoeUtilgjengeligeIdenterFraTestdatarequest filtererUtMiljoeUtilgjengeligeIdenterFraTestdatarequest;

    @Autowired
    private FiltrerPaaIdenterSomIkkeFinnesIDB filtrerPaaIdenterSomIkkeFinnesIDB;


    void filtrerPaaIdenterSomIkkeFinnesIDB(List<TestdataRequest> testdataRequests){
        filtrerPaaIdenterSomIkkeFinnesIDB.execute(testdataRequests);
    }

    void filtererUtMiljoeUtilgjengeligeIdenterFraTestdatarequest(List<TestdataRequest> testdataRequests){
        filtererUtMiljoeUtilgjengeligeIdenterFraTestdatarequest.execute(testdataRequests);
    }

    List<TestdataRequest> genererIdenterForTestdataRequests(RsPersonKriterieRequest personKriterierRequest){
        return genererIdenterForTestdataRequests.execute(personKriterierRequest);
    }

    void taBortOverfloedigIdenterITestdataRequest(TestdataRequest request){
        taBortOverfloedigIdenterITestdataRequest.execute(request);
    }
}
