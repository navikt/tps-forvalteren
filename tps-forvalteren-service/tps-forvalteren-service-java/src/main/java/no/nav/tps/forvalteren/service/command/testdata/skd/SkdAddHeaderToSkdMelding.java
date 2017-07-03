package no.nav.tps.forvalteren.service.command.testdata.skd;

@FunctionalInterface
public interface SkdAddHeaderToSkdMelding {

    StringBuilder execute(StringBuilder skdMelding);
}
