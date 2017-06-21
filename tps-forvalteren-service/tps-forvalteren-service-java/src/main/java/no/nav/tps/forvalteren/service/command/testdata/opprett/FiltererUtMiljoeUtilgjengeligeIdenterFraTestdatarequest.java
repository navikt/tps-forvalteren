package no.nav.tps.forvalteren.service.command.testdata.opprett;

import java.util.List;

@FunctionalInterface
public interface FiltererUtMiljoeUtilgjengeligeIdenterFraTestdatarequest {

    void execute(List<TestdataRequest> testdataRequests);

}
