package no.nav.tps.forvalteren.service.command.testdata.opprett;

import java.util.List;

import no.nav.tps.forvalteren.domain.rs.RsPersonMal;

@FunctionalInterface
public interface GenererIdenterForTestdataRequests {

    List<TestdataRequest> execute(RsPersonMal rsPersonMal, int antallIdenter);

}
