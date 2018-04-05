package no.nav.tps.forvalteren.service.command.testdata.skd;

@FunctionalInterface
public interface SkdAddHeaderToSkdMelding { //TODO Fjern denne, og overfør dens logikk til SkdGetHeaderForSkdMelding

    StringBuilder execute(StringBuilder skdMelding);
}
