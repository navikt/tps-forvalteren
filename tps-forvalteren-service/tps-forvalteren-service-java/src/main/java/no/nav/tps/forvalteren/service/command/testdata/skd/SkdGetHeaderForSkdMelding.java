package no.nav.tps.forvalteren.service.command.testdata.skd;

@FunctionalInterface
public interface SkdGetHeaderForSkdMelding {

    String execute(SkdMeldingTrans1 skdMelding);
}
