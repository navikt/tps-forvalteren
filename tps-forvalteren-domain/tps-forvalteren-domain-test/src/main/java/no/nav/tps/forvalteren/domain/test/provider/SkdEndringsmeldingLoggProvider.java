package no.nav.tps.forvalteren.domain.test.provider;

import java.time.LocalDateTime;

import no.nav.tps.forvalteren.domain.jpa.SkdEndringsmeldingLogg;

public class SkdEndringsmeldingLoggProvider {

    public static SkdEndringsmeldingLogg.SkdEndringsmeldingLoggBuilder aSkdEndringsmeldingLogg() {
        return SkdEndringsmeldingLogg.builder()
                .beskrivelse("Meldinger for test")
                .endringsmelding("LANG MELDING")
                .environment("u5")
                .innsendtAv("ABC1234")
                .innsendtDato(LocalDateTime.now())
                .meldingsgruppeId(1337L);
    }
}
