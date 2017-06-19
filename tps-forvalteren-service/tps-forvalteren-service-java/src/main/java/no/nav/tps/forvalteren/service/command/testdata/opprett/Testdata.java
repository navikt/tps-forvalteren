package no.nav.tps.forvalteren.service.command.testdata.opprett;

import no.nav.tps.forvalteren.domain.rs.RsPersonKriterieRequest;

import java.util.List;

public interface Testdata {

    void filtrerPaaIdenterSomIkkeFinnesIDB(List<TestdataRequest> testdataRequests);

    void filtererUtMiljoeUtilgjengeligeIdenterFraTestdatarequest(List<TestdataRequest> testdataRequests);

    List<TestdataRequest> genererIdenterForTestdataRequests(RsPersonKriterieRequest personKriterierRequest);

    void taBortOverfloedigIdenterITestdataRequest(TestdataRequest request);

}
