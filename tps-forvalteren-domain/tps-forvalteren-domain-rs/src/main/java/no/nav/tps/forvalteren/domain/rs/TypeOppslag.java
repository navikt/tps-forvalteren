package no.nav.tps.forvalteren.domain.rs;

import lombok.Getter;

@Getter
public enum TypeOppslag {
    O("Oversikt hendelsestyper og Kilder (Aggregert)"),
    L("Hent liste med tilgjengelige hendelsesmeldinger"),
    H("Hent en enkelt hendelsesmelding som skal spilles av");

    TypeOppslag(String description) {
        this.description = description;
    }

    private String description;
}