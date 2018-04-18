package no.nav.tps.forvalteren.service.command.testdata.opprett;

import no.nav.tps.forvalteren.domain.rs.RsPersonKriteriumRequest;
import no.nav.tps.forvalteren.domain.rs.RsPersonMal;

import java.util.List;

public interface Testdata {

    void filtrerPaaIdenterSomIkkeFinnesIDB(List<TestdataRequest> testdataRequests);

    void filtererUtMiljoeUtilgjengeligeIdenterFraTestdatarequest(List<TestdataRequest> testdataRequests);

    List<TestdataRequest> genererIdenterForTestdataRequests(RsPersonMal personMalRequest, int antallIdenter);

    void taBortOverfloedigIdenterITestdataRequest(TestdataRequest request, int antallIdenter);

}
