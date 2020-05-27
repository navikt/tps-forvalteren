package no.nav.tps.forvalteren.service.command.testdata.utils;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

import java.time.LocalDateTime;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class HentDatoFraIdentServiceTest {

    @InjectMocks
    private HentDatoFraIdentService hentDatoFraIdentService;

    @Test
    public void extractDateFromFnr1900Century() {

        LocalDateTime target = hentDatoFraIdentService.extract("13035830234");

        assertThat(target.getYear(), is(equalTo(1958)));
        assertThat(target.getMonthValue(), is(equalTo(3)));
        assertThat(target.getDayOfMonth(), is(equalTo(13)));
    }

    @Test
    public void extractDateFromFnr2000Century() {

        LocalDateTime target = hentDatoFraIdentService.extract("21050556234");

        assertThat(target.getYear(), is(equalTo(2005)));
        assertThat(target.getMonthValue(), is(equalTo(5)));
        assertThat(target.getDayOfMonth(), is(equalTo(21)));
    }

    @Test
    public void extractDateFromDnr1900Century() {

        LocalDateTime target = hentDatoFraIdentService.extract("61039596234");

        assertThat(target.getYear(), is(equalTo(1995)));
        assertThat(target.getMonthValue(), is(equalTo(3)));
        assertThat(target.getDayOfMonth(), is(equalTo(21)));
    }

    @Test
    public void extractDateFromDnr2000Century() {

        LocalDateTime target = hentDatoFraIdentService.extract("51050586534");

        assertThat(target.getYear(), is(equalTo(2005)));
        assertThat(target.getMonthValue(), is(equalTo(5)));
        assertThat(target.getDayOfMonth(), is(equalTo(11)));
    }

    @Test
    public void extractDateFromBost1900Century() {

        LocalDateTime target = hentDatoFraIdentService.extract("21250546234");

        assertThat(target.getYear(), is(equalTo(1905)));
        assertThat(target.getMonthValue(), is(equalTo(5)));
        assertThat(target.getDayOfMonth(), is(equalTo(21)));
    }

    @Test
    public void extractDateFromBost2000Century() {

        LocalDateTime target = hentDatoFraIdentService.extract("21250556234");

        assertThat(target.getYear(), is(equalTo(2005)));
        assertThat(target.getMonthValue(), is(equalTo(5)));
        assertThat(target.getDayOfMonth(), is(equalTo(21)));
    }

    @Test
    public void extractDateFromFnr1800Century() {
        LocalDateTime target = hentDatoFraIdentService.extract("21256556234");

        assertThat(target.getYear(), is(equalTo(1865)));
        assertThat(target.getMonthValue(), is(equalTo(5)));
        assertThat(target.getDayOfMonth(), is(equalTo(21)));
    }

    @Test
    public void extractDateFromFdat1900() {
        LocalDateTime target = hentDatoFraIdentService.extract("21256500001");

        assertThat(target.getYear(), is(equalTo(1965)));
        assertThat(target.getMonthValue(), is(equalTo(5)));
        assertThat(target.getDayOfMonth(), is(equalTo(21)));
    }

    @Test
    public void extractDateFromFdat2000() {
        LocalDateTime target = hentDatoFraIdentService.extract("21252000001");

        assertThat(target.getYear(), is(equalTo(2020)));
        assertThat(target.getMonthValue(), is(equalTo(5)));
        assertThat(target.getDayOfMonth(), is(equalTo(21)));
    }
}