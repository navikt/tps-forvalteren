package no.nav.tps.vedlikehold.service.command.tps.utils;

import com.fasterxml.jackson.xml.XmlMapper;
import no.nav.tps.vedlikehold.domain.service.command.tps.TpsRequest;
import no.nav.tps.vedlikehold.domain.service.command.tps.TpsSystemInfo;
import no.nav.tps.vedlikehold.domain.service.command.tps.ajourforing.requests.TpsRequestEndringsmelding;
import no.nav.tps.vedlikehold.domain.service.command.tps.servicerutiner.requests.TpsRequestServiceRoutine;
import org.hamcrest.CoreMatchers;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.io.IOException;

import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.*;
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
    private TpsRequestServiceRoutine tpsRequestServiceRoutine;

    @Mock
    private TpsRequestEndringsmelding tpsRequestEndringsmelding;

    @InjectMocks
    private TpsRequestXmlCreator tpsRequestXmlCreator;


    @Test(expected = IOException.class)
    public void throwsIOExceptionWhenMapperFails() throws Exception{
        when(xmlMapperMock.writeValueAsString(any(TpsRequest.class))).thenThrow(IOException.class);
        tpsRequestXmlCreator.createXmlTpsRequestServiceRutine(tpsRequestServiceRoutine);
    }

    @Test
    public void setsCorrectPreAndPostFixToServiceRutineXmlString() throws Exception{
        String tpsReqSr = tpsRequestXmlCreator.createXmlTpsRequestServiceRutine(tpsRequestServiceRoutine);
        assertThat(tpsReqSr, not(CoreMatchers.containsString(POSTFIX_ENDRINGSMELDING)));
        assertThat(tpsReqSr, CoreMatchers.containsString(POSTFIX_SERVICE_RUTINE));
    }

    @Test
    public void setsCorrectPreAndPostFixToEndringsmeldingXmlString() throws Exception{
        String tpsReqEndre = tpsRequestXmlCreator.createXmlTpsRequestEndringsmelding(tpsRequestEndringsmelding, any(TpsSystemInfo.class));
        assertThat(tpsReqEndre, not(CoreMatchers.containsString(POSTFIX_SERVICE_RUTINE)));
        assertThat(tpsReqEndre, CoreMatchers.containsString(POSTFIX_ENDRINGSMELDING));
    }
}