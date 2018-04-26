package no.nav.tps.forvalteren.service.command.testdata.utils;

import java.time.LocalDate;
import java.time.LocalDateTime;

import no.nav.tps.forvalteren.service.command.tps.skdmelding.skdparam.utils.ConvertStringToDate;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.junit.Assert.assertThat;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class ConvertStringToDateTest {

    @InjectMocks
    private ConvertStringToDate convertStringToDate;

    @Test
    public void yyyyMMddToLocalDateTime() {
        LocalDateTime date = LocalDate.of(1900, 01, 01).atStartOfDay();
        String dateString = "1900101";

        assertThat(convertStringToDate.yyyyMMdd(dateString), instanceOf(date.getClass()));
    }
}
