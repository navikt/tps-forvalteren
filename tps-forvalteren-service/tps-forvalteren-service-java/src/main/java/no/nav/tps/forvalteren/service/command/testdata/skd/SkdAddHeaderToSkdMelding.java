package no.nav.tps.forvalteren.service.command.testdata.skd;

@FunctionalInterface
public interface SkdAddHeaderToSkdMelding {

    void execute(StringBuilder skdMelding);
}
