package no.nav.tps.forvalteren.service.command.testdatamal;

import java.util.List;

import no.nav.tps.forvalteren.domain.rs.RsPersonMal;
import no.nav.tps.forvalteren.service.command.testdata.opprett.TestdataIdenterFetcher;
import no.nav.tps.forvalteren.service.command.testdata.opprett.TestdataRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CreateTestdataPerson {

    @Autowired
    private TestdataIdenterFetcher testdataIdenterFetcher;

    public void execute(Long gruppeId, RsPersonMal rsPersonMal, int antallPersoner) {



    }
}
