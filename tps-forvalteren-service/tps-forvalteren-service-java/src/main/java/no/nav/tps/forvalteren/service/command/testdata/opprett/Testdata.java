package no.nav.tps.forvalteren.service.command.testdata.opprett;

import no.nav.tps.forvalteren.domain.rs.RsPersonKriteriumRequest;
import no.nav.tps.forvalteren.domain.rs.RsPersonMal;
import no.nav.tps.forvalteren.domain.rs.RsPersonMalRequest;

import java.util.List;

public interface Testdata {

    void filtrerPaaIdenterSomIkkeFinnesIDB(List<TestdataRequest> testdataRequests);

    void filtererUtMiljoeUtilgjengeligeIdenterFraTestdatarequest(List<TestdataRequest> testdataRequests);

    List<TestdataRequest> genererIdenterForTestdataRequests(RsPersonMalRequest inputPersonRequest);

    void taBortOverfloedigIdenterITestdataRequest(TestdataRequest request);
}
