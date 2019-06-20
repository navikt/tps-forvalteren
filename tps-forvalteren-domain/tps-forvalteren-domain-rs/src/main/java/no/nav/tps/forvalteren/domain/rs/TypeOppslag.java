package no.nav.tps.forvalteren.domain.rs;

import lombok.Getter;

@Getter
public enum TypeOppslag {
    O("Oversikt hendelsestyper og Kilder (Aggregert)"),
    L("Liste med tilgjengelige hendelsesmeldinger"),
    H("Hendelsesmelding som skal spilles av");

    TypeOppslag(String description) {
        this.description = description;
    }

    private String description;
}