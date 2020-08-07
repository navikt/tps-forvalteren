package no.nav.tps.forvalteren.service.command.tps.skdmelding.skdparam.utils;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import java.time.LocalDateTime;
import java.time.LocalTime;
import org.junit.Test;

public class ConvertStringToDateTest {
    private final static Integer YEAR = 2000;
    private final static Integer MONTH = 12;
    private final static Integer DAY = 15;
    private final static Integer HOUR = 20;
    private final static Integer MIN = 30;
    private final static Integer SEC = 40;

    @Test(expected = IllegalAccessException.class)
    public void callConstructorFails() throws  Exception{
        Class clazs = getClass().getClassLoader().loadClass("no.nav.tps.forvalteren.service.command.tps.skdmelding.skdparam.utils.ConvertStringToDate");
        clazs.getDeclaredConstructor().newInstance();
    }

    @Test
    public void yyyMMddConvertOk() throws Exception {
        LocalDateTime result = ConvertStringToDate.yyyyMMdd(String.format("%s%s%s", YEAR.toString(), MONTH.toString(), DAY.toString()));
        assertThat(result.getYear(), is(equalTo(YEAR)));
        assertThat(result.getMonthValue(), is(equalTo(MONTH)));
        assertThat(result.getDayOfMonth(), is(equalTo(DAY)));
    }

    @Test
    public void hhMMssConvertOk() throws Exception {
        LocalTime result = ConvertStringToDate.hhMMss(String.format("%s%s%s", HOUR.toString(), MIN.toString(), SEC.toString()));
        assertThat(result.getHour(), is(equalTo(HOUR)));
        assertThat(result.getMinute(), is(equalTo(MIN)));
        assertThat(result.getSecond(), is(equalTo(SEC)));
    }

    @Test
    public void yyyysMMsddConvertOk() throws Exception {
        LocalDateTime result = ConvertStringToDate.yyyysMMsdd(String.format("%s-%s-%s", YEAR.toString(), MONTH.toString(), DAY.toString()));
        assertThat(result.getYear(), is(equalTo(YEAR)));
        assertThat(result.getMonthValue(), is(equalTo(MONTH)));
        assertThat(result.getDayOfMonth(), is(equalTo(DAY)));
    }
}