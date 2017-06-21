package no.nav.tps.forvalteren.service.command.testdata.opprett;

import java.util.List;

@FunctionalInterface
public interface EkstraherIdenterFraTestdataRequests {

    List<String> execute(List<TestdataRequest> testdataRequests);

}
