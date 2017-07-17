package no.nav.tps.forvalteren.consumer.ws.kodeverk.mapping;

import com.sun.org.apache.xerces.internal.jaxp.datatype.XMLGregorianCalendarImpl;
import no.nav.tjeneste.virksomhet.kodeverk.v2.informasjon.Periode;
import org.joda.time.LocalDate;

import javax.xml.datatype.XMLGregorianCalendar;
import java.util.GregorianCalendar;
import java.util.List;

import static java.util.Collections.singletonList;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * @author Kenneth Gunnerud (Visma Consulting AS).
 */
class KodeverkTestUtils {

    static List<Periode> createGyldighetsperiode(LocalDate fom, LocalDate tom) {
        XMLGregorianCalendar fomCal = mockXmlGregorianCalendar(fom);
        XMLGregorianCalendar tomCal = mockXmlGregorianCalendar(tom);

        Periode p = mock(Periode.class);
        when(p.getFom()).thenReturn(fomCal);
        when(p.getTom()).thenReturn(tomCal);
        return singletonList(p);
    }

    private static XMLGregorianCalendar mockXmlGregorianCalendar(LocalDate date) {
        GregorianCalendar gregCal = mock(GregorianCalendar.class);
        when(gregCal.getTimeInMillis()).thenReturn(date.toDateTimeAtStartOfDay().getMillis());

        XMLGregorianCalendar xmlCal = mock(XMLGregorianCalendarImpl.class);
        when(xmlCal.toGregorianCalendar()).thenReturn(gregCal);
        return xmlCal;
    }
}
