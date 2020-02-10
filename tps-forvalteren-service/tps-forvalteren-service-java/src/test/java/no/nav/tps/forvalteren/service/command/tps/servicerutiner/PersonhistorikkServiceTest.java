package no.nav.tps.forvalteren.service.command.tps.servicerutiner;

import static no.nav.tps.forvalteren.domain.service.tps.servicerutiner.definition.resolvers.servicerutiner.S018PersonHistorikk.PERSON_HISTORY_SERVICE_ROUTINE;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.ArgumentMatchers.anyMap;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import no.nav.tps.forvalteren.domain.service.tps.servicerutiner.response.TpsServiceRoutineResponse;
import no.nav.tps.forvalteren.service.command.exceptions.TpsfTechnicalException;
import no.nav.tps.xjc.ctg.domain.s018.S018PersonType;

@RunWith(MockitoJUnitRunner.class)
public class PersonhistorikkServiceTest {

    private final static String IDENT = "12345678901";
    private final static LocalDateTime AKSJON_DATO = LocalDateTime.of(2000, 11, 15, 0, 0);
    private final static String MILJOE = "U5";

    @Mock
    private TpsServiceRoutineService tpsServiceRoutineService;

    @Mock
    private TpsServiceRoutineResponse tpsServiceRoutineResponse;

    @InjectMocks
    private PersonhistorikkService personhistorikkService;

    @Test(expected = TpsfTechnicalException.class)
    public void hentPersonhistorikkThrowsException() throws Exception {

        when(tpsServiceRoutineResponse.getXml()).thenReturn("<?xml version=\"1.0\" encoding=\"ISO-8859-1\"?><tpsPersonData></tpsPersonData>");
        when(tpsServiceRoutineService.execute(eq(PERSON_HISTORY_SERVICE_ROUTINE), anyMap(), eq(true))).thenReturn(tpsServiceRoutineResponse);

        personhistorikkService.hentPersonhistorikk(IDENT, AKSJON_DATO, MILJOE);

        verify(tpsServiceRoutineService).execute(eq(PERSON_HISTORY_SERVICE_ROUTINE), anyMap(), eq(true));
    }

    @Test
    public void hentTomPersonhistorikk() throws Exception {

        when(tpsServiceRoutineResponse.getXml()).thenReturn("<?xml version=\"1.0\" encoding=\"ISO-8859-1\"?><tpsPersonData xmlns=\"http://www.rtv.no/NamespaceTPS\"></tpsPersonData>");
        when(tpsServiceRoutineService.execute(eq(PERSON_HISTORY_SERVICE_ROUTINE), anyMap(), eq(true))).thenReturn(tpsServiceRoutineResponse);

        S018PersonType personType = personhistorikkService.hentPersonhistorikk(IDENT, AKSJON_DATO, MILJOE);

        verify(tpsServiceRoutineService).execute(eq(PERSON_HISTORY_SERVICE_ROUTINE), anyMap(), eq(true));
        assertThat(personType, is(nullValue()));
    }

    @Test
    public void hentPersonhistorikkOkResultat() throws Exception {

        when(tpsServiceRoutineResponse.getXml()).thenReturn(getMsg("00"));
        when(tpsServiceRoutineService.execute(eq(PERSON_HISTORY_SERVICE_ROUTINE), anyMap(), eq(true))).thenReturn(tpsServiceRoutineResponse);

        S018PersonType personType = personhistorikkService.hentPersonhistorikk(IDENT, AKSJON_DATO, MILJOE);

        verify(tpsServiceRoutineService).execute(eq(PERSON_HISTORY_SERVICE_ROUTINE), anyMap(), eq(true));
        assertThat(personType, is(notNullValue()));
        assertThat(personType.getFnr(), is(equalTo(IDENT)));
        assertThat(personType.getIdentType(), is(equalTo("FNR")));
    }

    @Test(expected = TpsfTechnicalException.class)
    public void hentPersonhistorikkFeilFraTps() throws Exception {

        when(tpsServiceRoutineResponse.getXml()).thenReturn(getMsg("01"));
        when(tpsServiceRoutineService.execute(eq(PERSON_HISTORY_SERVICE_ROUTINE), anyMap(), eq(true))).thenReturn(tpsServiceRoutineResponse);

        S018PersonType personType = personhistorikkService.hentPersonhistorikk(IDENT, AKSJON_DATO, MILJOE);

        verify(tpsServiceRoutineService).execute(eq(PERSON_HISTORY_SERVICE_ROUTINE), anyMap(), eq(true));
        assertThat(personType, is(notNullValue()));
    }

    private String getMsg(String status) {
        return "<?xml version=\"1.0\" encoding=\"ISO-8859-1\"?>\n"
                + "<tpsPersonData xmlns=\"http://www.rtv.no/NamespaceTPS\">\n"
                + "    <tpsServiceRutine>\n"
                + "        <serviceRutinenavn>FS03-FDNUMMER-PADRHIST-O</serviceRutinenavn>\n"
                + "        <aksjonsDato>2018-08-31</aksjonsDato>\n"
                + "        <aksjonsKode>A</aksjonsKode>\n"
                + "        <aksjonsKode2>0</aksjonsKode2>\n"
                + "        <fnr>12345678901</fnr>\n"
                + "        <buffNr/>\n"
                + "    </tpsServiceRutine>\n"
                + "    <tpsSvar>\n"
                + "        <svarStatus>\n"
                + "            <returStatus>" + status + "</returStatus>\n"
                + "            <returMelding></returMelding>\n"
                + "            <utfyllendeMelding></utfyllendeMelding>\n"
                + "        </svarStatus>\n"
                + "        <personDataS018>\n"
                + "            <fnr>"+ IDENT + "</fnr>\n"
                + "            <identType>FNR</identType>\n"
                + "        </personDataS018>\n"
                + "    </tpsSvar>\n"
                + "</tpsPersonData>";
    }
 }