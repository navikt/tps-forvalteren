package no.nav.tps.forvalteren.repository.jpa;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.joda.time.DateTime.now;
import static org.junit.Assert.assertThat;

import java.sql.Date;

import no.nav.tps.forvalteren.repository.jpa.converter.DateTimeToDateConverter;
import org.joda.time.DateTime;
import org.joda.time.DateTimeUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class DateTimeToDateConverterTest {

    private DateTimeToDateConverter converter;

    @Before
    public void setUpTest() {
        DateTimeUtils.setCurrentMillisFixed(System.currentTimeMillis());
        converter = new DateTimeToDateConverter();
    }

    @After
    public void releaseTime() {
        DateTimeUtils.setCurrentMillisSystem();
    }

    @Test
    public void returnsCorrectDateWhenConvertingDateTime() {
        Date result = converter.convertToDatabaseColumn(now());

        assertThat(result.getTime(), is(equalTo(now().getMillis())));
    }

    @Test
    public void returnsNullWhenConvertingNullToDate() {
        Date result = converter.convertToDatabaseColumn(null);

        assertThat(result, is(nullValue()));
    }

    @Test
    public void returnsCorrectDateTimeWhenConvertingDate() {
        DateTime result = converter.convertToEntityAttribute(new Date(now().getMillis()));

        assertThat(result, is(equalTo(now())));
    }

    @Test
    public void returnsNullWhenConvertingNullToDateTime() {
        DateTime result = converter.convertToEntityAttribute(null);

        assertThat(result, is(nullValue()));
    }
}
