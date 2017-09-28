package no.nav.tps.forvalteren.service.command.tps.skdmelding.skdparam.utils;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import java.time.LocalDateTime;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

@RunWith(MockitoJUnitRunner.class)
public class GetStringVersionOfLocalDateTimeTest {

    private static final int YEAR = 2017;
    private static final int MONTH = 9;
    private static final int DAY = 28;
    private static final int HOUR = 9;
    private static final int MINUTE = 22;
    private static final int SECOND = 44;
    private static final LocalDateTime TIME = LocalDateTime.of(YEAR, MONTH, DAY, HOUR, MINUTE, SECOND);

    @Test
    public void checkThatYyyyMmDdReturnsCorrectDate() {
        String result = GetStringVersionOfLocalDateTime.yyyyMMdd(TIME);
        assertThat(result, is("20170928"));
    }

    @Test
    public void checkThatHhMmSsReturnsCorrectDate() {
        String result = GetStringVersionOfLocalDateTime.hhMMss(TIME);
        assertThat(result, is("092244"));
    }

}