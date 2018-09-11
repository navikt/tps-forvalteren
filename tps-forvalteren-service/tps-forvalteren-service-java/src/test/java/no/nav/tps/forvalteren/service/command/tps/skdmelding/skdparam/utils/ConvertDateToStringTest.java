package no.nav.tps.forvalteren.service.command.tps.skdmelding.skdparam.utils;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import java.time.LocalDateTime;
import org.junit.Test;

public class ConvertDateToStringTest {

    private final static Integer YEAR = 2000;
    private final static Integer MONTH = 12;
    private final static Integer DAY = 15;
    private final static Integer HOUR = 20;
    private final static Integer MIN = 30;
    private final static Integer SEC = 40;

    @Test
    public void yyyyMMdd() throws Exception {
        String date = ConvertDateToString.yyyyMMdd(LocalDateTime.of(YEAR, MONTH, DAY, 0, 0));

        assertThat(date.substring(0, 4), is(equalTo(YEAR.toString())));
        assertThat(date.substring(4, 6), is(equalTo(MONTH.toString())));
        assertThat(date.substring(6, 8), is(equalTo(DAY.toString())));
    }

    @Test
    public void hhMMss() throws Exception {
        String date = ConvertDateToString.hhMMss(LocalDateTime.of(YEAR, MONTH, DAY, HOUR, MIN, SEC));

        assertThat(date.substring(0, 2), is(equalTo(HOUR.toString())));
        assertThat(date.substring(2, 4), is(equalTo(MIN.toString())));
        assertThat(date.substring(4, 6), is(equalTo(SEC.toString())));
    }

    @Test
    public void yyyy_MM_dd() throws Exception {
        String date = ConvertDateToString.yyyysMMsdd(LocalDateTime.of(YEAR, MONTH, DAY, 0, 0));

        assertThat(date.substring(0, 4), is(equalTo(YEAR.toString())));
        assertThat(date.substring(5, 7), is(equalTo(MONTH.toString())));
        assertThat(date.substring(8, 10), is(equalTo(DAY.toString())));
    }
}