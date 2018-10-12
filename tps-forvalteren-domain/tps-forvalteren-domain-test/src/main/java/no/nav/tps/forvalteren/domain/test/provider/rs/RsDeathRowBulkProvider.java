package no.nav.tps.forvalteren.domain.test.provider.rs;

import java.time.LocalDateTime;
import java.util.Arrays;

import no.nav.tps.forvalteren.domain.rs.RsDeathRowBulk;

public final class RsDeathRowBulkProvider {

    private RsDeathRowBulkProvider() {

    }

    public static RsDeathRowBulk.RsDeathRowBulkBuilder aDeathRowBulk() {

        return RsDeathRowBulk.builder()
                .identer(Arrays.asList("1111", "2222"))
                .bruker("bruker")
                .doedsdato(LocalDateTime.now().minusDays(1))
                .handling("handling")
                .miljoe("miljoe")
                .status("status")
                .tidspunkt(LocalDateTime.parse("2018-01-01T00:00:00"))
                .tilstand("tilstand");
    }
}
