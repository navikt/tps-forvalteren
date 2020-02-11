package no.nav.tps.forvalteren.domain.rs;

import lombok.Getter;

@Getter
public enum Meldingsformat {
    AJOURHOLDSMELDING("A", "SkdFormat"),
    DISTRIBUSJONSMELDING("D", "FagsystemFormat");

    private String meldingFormat;
    private String system;

    Meldingsformat(String meldingFormat, String system) {
        this.meldingFormat = meldingFormat;
        this.system = system;
    }
}
