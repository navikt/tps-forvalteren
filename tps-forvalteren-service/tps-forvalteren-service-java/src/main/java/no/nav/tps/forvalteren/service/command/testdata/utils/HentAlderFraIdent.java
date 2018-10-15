package no.nav.tps.forvalteren.service.command.testdata.utils;

import java.time.Clock;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class HentAlderFraIdent {

    @Autowired
    private Clock clock;

    @Autowired
    private HentDatoFraIdentService hentDatoFraIdentService;

    public Integer extract(String ident, LocalDateTime doedsdato) {

        return (int)
                ChronoUnit.YEARS.between(
                        hentDatoFraIdentService.extract(ident),
                        (doedsdato == null ? LocalDateTime.now(clock) : doedsdato)
                );
    }
}