package no.nav.tps.forvalteren.repository.jpa.converter;

import java.sql.Date;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

import org.joda.time.DateTime;
import org.springframework.stereotype.Component;

@Component
@Converter(autoApply = true)
public class DateTimeToDateConverter implements AttributeConverter<DateTime, Date> {

    @Override
    public Date convertToDatabaseColumn(DateTime dateTime) {
        if (dateTime != null) {
            return new Date(dateTime.getMillis());
        }
        return null;
    }

    @Override
    public DateTime convertToEntityAttribute(Date date) {
        if (date != null) {
            return new DateTime(date.getTime());
        }
        return null;
    }
}
