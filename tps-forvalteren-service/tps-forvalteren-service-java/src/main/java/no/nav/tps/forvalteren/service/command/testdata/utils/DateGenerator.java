package no.nav.tps.forvalteren.service.command.testdata.utils;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

public final class DateGenerator {

    private static final SecureRandom secureRandom = new SecureRandom();

    private DateGenerator() {
    }

    public static LocalDateTime genererRandomDatoInnenforIntervalInclusiveDatoEtterExclusiveDatoFoer(LocalDateTime dateEtter, LocalDateTime dateFoer) {

        long time = ChronoUnit.DAYS.between(dateEtter, dateFoer);
        long rand = 0 + (long) (secureRandom.nextDouble() * time);

        return dateEtter.plusDays(rand);
    }
}