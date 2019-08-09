package no.nav.tps.forvalteren.service.command.testdata.restreq;

import static java.time.LocalDateTime.now;
import static java.util.Objects.isNull;

import java.time.LocalDateTime;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class DefaultBestillingDatoer {

    private static final LocalDateTime START_OF_ERA = LocalDateTime.of(1900, 1, 1, 0, 0);

    public static LocalDateTime getProcessedFoedtEtter(LocalDateTime foedtEtter, LocalDateTime foedtFoer, Boolean isBarn) {

        if (isNull(foedtEtter) && isNull(foedtFoer)) {
            if (isBarn) {
                return now().minusYears(18);
            } else
                return now().minusYears(60);

        } else if (isNull(foedtEtter)) {
            return START_OF_ERA;

        } else {
            return foedtEtter;
        }
    }

    public static LocalDateTime getProcessedFoedtFoer(LocalDateTime foedtEtter, LocalDateTime foedtFoer, Boolean isBarn) {

        if (isNull(foedtEtter) && isNull(foedtFoer)) {
            if (isBarn) {
                return now();
            } else
                return now().minusYears(30);

        } else {
            if (isNull(foedtFoer)) {
                return now();

            } else {
                return foedtFoer;
            }
        }
    }
}
