package no.nav.tps.forvalteren.service.command.testdata.utils;

import org.junit.Test;

import java.time.LocalDate;

import static org.junit.Assert.*;

public class DateGeneratorTest {

    @Test
    public void lagerDatoerInnenInterval() throws Exception {
        LocalDate mustBeAfterDate = LocalDate.of(1988,2,10);
        LocalDate mustBeBeforeDate = LocalDate.of(1990,2,10);

        for(int i =0; i<10000; i++){
            LocalDate date = DateGenerator.genererRandomDatoInnenforIntervalInclusiveDatoEtterExclusiveDatoFoer(mustBeAfterDate,mustBeBeforeDate);
            assertTrue(!date.isBefore(mustBeAfterDate) && !date.isAfter(mustBeBeforeDate));
        }
    }

    @Test
    public void hvisEtterOgFoerErSammeDatoSaRetuneresSammeDato() throws Exception {
        LocalDate mustBeAfterDate = LocalDate.of(1988,2,10);
        LocalDate mustBeBeforeDate = LocalDate.of(1988,2,10);

        LocalDate date = DateGenerator.genererRandomDatoInnenforIntervalInclusiveDatoEtterExclusiveDatoFoer(mustBeAfterDate, mustBeBeforeDate);
        assertTrue(date.isEqual(mustBeAfterDate));
    }

}