package no.nav.tps.forvalteren.service.command.tps.skdmelding.skdparam.utils;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.time.LocalDateTime;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class GetStringVersionOfLocalDateTimeTest {

    private static final int YEAR = 2017;
    private static final int MONTH = 9;
    private static final int DAY = 8;
    private static final int HOUR = 9;
    private static final int MINUTE = 2;
    private static final int SECOND = 4;
    private static final LocalDateTime TIME = LocalDateTime.of(YEAR, MONTH, DAY, HOUR, MINUTE, SECOND);

    @Test
    public void checkThatYyyyMmDdReturnsCorrectDate() {
        String result = ConvertDateToString.yyyyMMdd(TIME);
        assertThat(result, is("20170908"));
    }

    @Test
    public void checkThatHhMmSsReturnsCorrectDate() {
        String result = ConvertDateToString.hhMMss(TIME);
        assertThat(result, is("090204"));
    }

}