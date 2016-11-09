package no.nav.tps.vedlikehold.service.command.tps.utils;

import com.fasterxml.jackson.xml.XmlMapper;
import no.nav.tps.vedlikehold.domain.service.command.tps.servicerutiner.requests.TpsServiceRoutineRequest;
import org.hamcrest.CoreMatchers;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.io.IOException;

import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

/**
 * Created by f148888 on 27.10.2016.
 */

@RunWith(MockitoJUnitRunner.class)
public class TpsRequestXmlCreatorTest {

    private static final String POSTFIX_ENDRINGSMELDING = "</sfeAjourforing> </sfePersonData>";
    private static final String POSTFIX_SERVICE_RUTINE = "</tpsPersonData>";

    @Mock
    private XmlMapper xmlMapperMock;

    @Mock
    private TpsServiceRoutineRequest tpsServiceRoutineRequest;

//    @Mock
//    private TpsRequestEndringsmelding tpsRequestEndringsmelding;


    @Test(expected = IOException.class)
    public void throwsIOExceptionWhenMapperFails() throws Exception{
//        when(xmlMapperMock.writeValueAsString(any(TpsRequest.class))).thenThrow(IOException.class);
//        tpsRequestXmlCreator.createXmlTpsRequestServiceRutine(tpsServiceRoutineRequest);
    }

    @Test
    public void setsCorrectPreAndPostFixToServiceRutineXmlString() throws Exception{
        String tpsReqSr = null;//tpsRequestXmlCreator.createXmlTpsRequestServiceRutine(tpsServiceRoutineRequest);
        assertThat(tpsReqSr, not(CoreMatchers.containsString(POSTFIX_ENDRINGSMELDING)));
        assertThat(tpsReqSr, CoreMatchers.containsString(POSTFIX_SERVICE_RUTINE));
    }

    @Test
    public void setsCorrectPreAndPostFixToEndringsmeldingXmlString() throws Exception{
        String tpsReqEndre = null;//tpsRequestXmlCreator.createXmlTpsRequestEndringsmelding(tpsRequestEndringsmelding, any(TpsSystemInfo.class));
        assertThat(tpsReqEndre, not(CoreMatchers.containsString(POSTFIX_SERVICE_RUTINE)));
        assertThat(tpsReqEndre, CoreMatchers.containsString(POSTFIX_ENDRINGSMELDING));
    }
}