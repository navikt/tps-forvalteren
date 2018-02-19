package no.nav.tps.forvalteren.domain.test.provider.rs;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import no.nav.tps.forvalteren.domain.rs.RsDeathRowBulk;

public final class RsDeathRowBulkProvider {

    private RsDeathRowBulkProvider() {

    }

    public static RsDeathRowBulk.RsDeathRowBulkBuilder aDeathRowBulk() {
        List<String> identList = new ArrayList<>();
        identList.add("1111");
        identList.add("2222");
        return RsDeathRowBulk.builder()
                .identer(identList)
                .bruker("bruker")
                .doedsdato(LocalDateTime.now().minusDays(1))
                .handling("handling")
                .miljoe("miljoe")
                .status("status")
                .tidspunkt(LocalDateTime.parse("2018-01-01T00:00:00"))
                .tilstand("tilstand");
    }
}
