package no.nav.tps.forvalteren.service.command.testdata.utils;

import java.security.SecureRandom;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class DateGenerator {

    private static final SecureRandom secureRandom = new SecureRandom();

    public static LocalDate genererRandomDatoInnenforIntervalInclusiveDatoEtterExclusiveDatoFoer(LocalDate dateEtter, LocalDate dateFoer){

        long time = ChronoUnit.DAYS.between(dateEtter, dateFoer);
        long rand = 0 + (long)(secureRandom.nextDouble()*time);
        LocalDate newDate = dateEtter.plusDays(rand);

        return newDate;
    }
}
