package no.nav.tps.forvalteren.service.command.testdata.utils;

import static org.junit.Assert.assertTrue;

import java.time.LocalDate;
import java.time.LocalDateTime;
import org.junit.Test;

public class DateGeneratorTest {

    @Test
    public void lagerDatoerInnenInterval() throws Exception {
        LocalDateTime mustBeAfterDate = LocalDate.of(1988,2,10).atStartOfDay();
        LocalDateTime mustBeBeforeDate = LocalDate.of(1990,2,10).atStartOfDay();

        for(int i =0; i<10000; i++){
            LocalDateTime date = DateGenerator.genererRandomDatoInnenforIntervalInclusiveDatoEtterExclusiveDatoFoer(mustBeAfterDate,mustBeBeforeDate);
            assertTrue(!date.isBefore(mustBeAfterDate) && !date.isAfter(mustBeBeforeDate));
        }
    }

    @Test
    public void hvisEtterOgFoerErSammeDatoSaRetuneresSammeDato() throws Exception {
        LocalDateTime mustBeAfterDate = LocalDate.of(1988,2,10).atStartOfDay();
        LocalDateTime mustBeBeforeDate = LocalDate.of(1988,2,10).atStartOfDay();

        LocalDateTime date = DateGenerator.genererRandomDatoInnenforIntervalInclusiveDatoEtterExclusiveDatoFoer(mustBeAfterDate, mustBeBeforeDate);
        assertTrue(date.isEqual(mustBeAfterDate));
    }

}