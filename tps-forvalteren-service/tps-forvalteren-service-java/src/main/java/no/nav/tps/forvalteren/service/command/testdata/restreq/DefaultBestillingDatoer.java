package no.nav.tps.forvalteren.service.command.testdata.restreq;

import static java.time.LocalDateTime.now;
import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;

import java.time.LocalDateTime;

import lombok.experimental.UtilityClass;

@UtilityClass
public class DefaultBestillingDatoer {

    private static final LocalDateTime START_OF_ERA = LocalDateTime.of(1900, 1, 1, 0, 0);

    public static LocalDateTime getProcessedFoedtEtter(Integer alder, LocalDateTime foedtEtter, LocalDateTime foedtFoer, Boolean isBarn) {

        if (nonNull(alder)) {
            return now().minusYears(alder).minusYears(1);
        }
        if (isNull(foedtEtter) && isNull(foedtFoer)) {
            if (isBarn) {
                return now().minusYears(18);
            } else {
                return now().minusYears(60);
            }
        } else if (isNull(foedtEtter)) {
            return START_OF_ERA;

        } else {
            return foedtEtter;
        }
    }

    public static LocalDateTime getProcessedFoedtFoer(Integer alder, LocalDateTime foedtEtter, LocalDateTime foedtFoer, Boolean isBarn) {

        if (nonNull(alder)) {
            return now().minusYears(alder).minusMonths(3);
        }
        if (isNull(foedtEtter) && isNull(foedtFoer)) {
            if (isBarn) {
                return now();
            } else {
                return now().minusYears(30);
            }
        } else {
            if (isNull(foedtFoer)) {
                return now();

            } else {
                return foedtFoer;
            }
        }
    }

    public static LocalDateTime getForeldreProcessedFoedtEtter(Integer alder, LocalDateTime foedtEtter, LocalDateTime foedtFoer) {

        if (nonNull(alder)) {
            return now().minusYears(alder).minusYears(1);
        }
        if (isNull(foedtEtter) && isNull(foedtFoer)) {
            return now().minusYears(90);

        } else if (isNull(foedtEtter)) {
            return START_OF_ERA;

        } else {
            return foedtEtter;
        }
    }

    public static LocalDateTime getForeldreProcessedFoedtFoer(Integer alder, LocalDateTime foedtEtter, LocalDateTime foedtFoer) {

        if (nonNull(alder)) {
            return now().minusYears(alder).minusMonths(3);
        }
        if (isNull(foedtEtter) && isNull(foedtFoer)) {
            return now().minusYears(70);

        } else if (isNull(foedtFoer)) {
            return now();

        } else {
            return foedtFoer;
        }
    }
}

