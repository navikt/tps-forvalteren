package no.nav.tps.forvalteren.service.command.testdata.opprett;

@FunctionalInterface
public interface TaBortOverfloedigIdenterITestdataRequest {

    void execute(TestdataRequest request, int antallIdenter);

}
