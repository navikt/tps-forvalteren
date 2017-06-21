package no.nav.tps.forvalteren.service.command.testdata.opprett;

import no.nav.tps.forvalteren.domain.rs.RsPersonKriterieRequest;

import java.util.List;

public interface TestdataIdenterFetcher {

    List<TestdataRequest> getTestdataRequestsInnholdeneTilgjengeligeIdenter(RsPersonKriterieRequest personKriterierListe);

}
