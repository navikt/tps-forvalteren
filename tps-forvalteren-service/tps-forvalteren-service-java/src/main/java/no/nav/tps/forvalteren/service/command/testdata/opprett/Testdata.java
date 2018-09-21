package no.nav.tps.forvalteren.service.command.testdata.opprett;

import java.util.List;

import no.nav.tps.forvalteren.domain.rs.RsPersonMalRequest;

public interface Testdata {

    void filtrerPaaIdenterSomIkkeFinnesIDB(List<TestdataRequest> testdataRequests);

    void filtererUtMiljoeUtilgjengeligeIdenterFraTestdatarequest(List<TestdataRequest> testdataRequests);

    List<TestdataRequest> genererIdenterForTestdataRequests(RsPersonMalRequest inputPersonRequest);

    void taBortOverfloedigIdenterITestdataRequest(TestdataRequest request);
}
