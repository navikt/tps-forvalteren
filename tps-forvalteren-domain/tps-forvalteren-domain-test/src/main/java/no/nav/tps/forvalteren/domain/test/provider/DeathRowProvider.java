package no.nav.tps.forvalteren.domain.test.provider;

import java.time.LocalDate;

import no.nav.tps.forvalteren.domain.jpa.DeathRow;

public class DeathRowProvider {
    
    public static DeathRow.DeathRowBuilder aDeathRow() {
        return DeathRow.builder()
                .action("Ny dødsdato")
                .doedsdato(LocalDate.now())
                .environment("u5")
                .ident("99887712345")
                .status("finnes i TPS")
                .tilstand("Ikke sendt inn");
    }
}
