package no.nav.tps.forvalteren.domain.rs;

import lombok.Getter;

@Getter
public enum Meldingsformat {
    Ajourholdsmelding("A", "Tps format"),
    Distribusjonsmelding("D", "Fagsystem format");

    Meldingsformat(String meldingFormat, String system) {
        this.meldingFormat = meldingFormat;
        this.system = system;
    }

    private String meldingFormat;

    private String system;
}
