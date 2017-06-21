package no.nav.tps.forvalteren.service.command.testdata.opprett.implementation;

import no.nav.tps.forvalteren.domain.rs.RsPersonKriterieRequest;
import no.nav.tps.forvalteren.service.command.testdata.opprett.TestdataRequest;
import no.nav.tps.forvalteren.service.command.testdata.opprett.FiltererUtMiljoeUtilgjengeligeIdenterFraTestdatarequest;
import no.nav.tps.forvalteren.service.command.testdata.opprett.Testdata;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DefaultTestdata implements Testdata {

    @Autowired
    private DefaultTaBortOverfloedigIdenterITestdataRequest taBortOverfloedigIdenterITestdataRequest;

    @Autowired
    private DefaultGenererIdenterForTestdataRequests genererIdenterForTestdataRequests;

    @Autowired
    private FiltererUtMiljoeUtilgjengeligeIdenterFraTestdatarequest filtererUtMiljoeUtilgjengeligeIdenterFraTestdatarequest;

    @Autowired
    private DefaultFiltrerPaaIdenterSomIkkeFinnesIDB filtrerPaaIdenterSomIkkeFinnesIDB;


    public void filtrerPaaIdenterSomIkkeFinnesIDB(List<TestdataRequest> testdataRequests){
        filtrerPaaIdenterSomIkkeFinnesIDB.execute(testdataRequests);
    }

    public void filtererUtMiljoeUtilgjengeligeIdenterFraTestdatarequest(List<TestdataRequest> testdataRequests){
        filtererUtMiljoeUtilgjengeligeIdenterFraTestdatarequest.execute(testdataRequests);
    }

    public List<TestdataRequest> genererIdenterForTestdataRequests(RsPersonKriterieRequest personKriterierRequest){
        return genererIdenterForTestdataRequests.execute(personKriterierRequest);
    }

    public void taBortOverfloedigIdenterITestdataRequest(TestdataRequest request){
        taBortOverfloedigIdenterITestdataRequest.execute(request);
    }
}
