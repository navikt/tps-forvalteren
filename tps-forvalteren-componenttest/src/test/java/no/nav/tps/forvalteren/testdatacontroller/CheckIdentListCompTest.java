package no.nav.tps.forvalteren.testdatacontroller;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;
import javax.jms.JMSException;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.MvcResult;

import no.nav.tps.forvalteren.config.TestUserDetails;
import no.nav.tps.forvalteren.consumer.mq.consumers.MessageQueueConsumer;
import no.nav.tps.forvalteren.consumer.rs.environments.FetchEnvironmentsManager;
import no.nav.tps.forvalteren.domain.jpa.Gruppe;
import no.nav.tps.forvalteren.domain.jpa.Person;
import no.nav.tps.forvalteren.service.command.testdata.response.IdentMedStatus;

public class CheckIdentListCompTest extends AbstractTestdataControllerComponentTest {
    final HashSet<String> fasitRegistrerteEnvMedTps = new HashSet<>();
    private Gruppe testgruppe;
    String gyldigIdentEksistererITpsf = "04121656499";
    
    @Autowired
    private FetchEnvironmentsManager fetchEnvironmentsManagerSpy; //Alternativet er å wiremocke https://fasit.adeo.no/api/v2/applicationinstances?application=tpsws&usage=true
    @Autowired
    private MessageQueueConsumer messageQueueConsumer;
    
    @Override
    protected String getServiceUrl() {
        return "/checkpersoner";
    }
    
    @Before
    public void setup() throws JMSException {
        reset(messageQueueConsumer);
        
        fasitRegistrerteEnvMedTps.addAll(Arrays.asList("u5", "t4", "t5", "t9", "t8"));
        when(fetchEnvironmentsManagerSpy.getEnvironments("tpsws")).thenReturn(fasitRegistrerteEnvMedTps);
        
        mockTps();
    }
    
    /**
     * NÅR en liste med identer sendes inn for sjekk, SÅ skal det sjekkes om de er gyldige numre og om de er ledige identer i TPSF databasen og TPS databasen.
     */
    @Test
    @WithUserDetails(TestUserDetails.USERNAME)
    public void shouldReturnStatusOnAllIdents() throws Exception {
        
        setupTestdataInTpsfDatabase();
        List<IdentMedStatus> expectedResponse = Arrays.asList(
                new IdentMedStatus("12017500617", "IL"),
                new IdentMedStatus("32156489777", "IG"),
                new IdentMedStatus("03051750127", "LOG"),
                new IdentMedStatus("12345612345", "IG"),
                new IdentMedStatus(gyldigIdentEksistererITpsf, "IL")
        );
        
        MvcResult mvcResult = mvc.perform(post(getUrl()).contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(createRequestContent(expectedResponse)))
                .andExpect(status().isOk()).andReturn();
        
        verify(messageQueueConsumer, times(fasitRegistrerteEnvMedTps.size())).sendMessage(
                removeNewLineAndTab(getResourceFileContent("testdatacontroller/checkidentlist/finn_identer_i_TPS_request.xml")));
        
        assertResponse(expectedResponse, mvcResult);
    }
    
    private void assertResponse(List<IdentMedStatus> expectedResponse, MvcResult mvcResult) throws IOException {
        List<IdentMedStatus> identStatusList = convertMvcResultToList(mvcResult, IdentMedStatus.class);
        
        assertEquals(expectedResponse.size(), identStatusList.size());
        for (int i = 0; i < identStatusList.size(); i++) {
            assertEquals(expectedResponse.get(i).getIdent(), identStatusList.get(i).getIdent());
            assertEquals(expectedResponse.get(i).getIdent() + " status", expectedResponse.get(i).getStatus(), identStatusList.get(i).getStatus());
        }
    }
    
    private String createRequestContent(List<IdentMedStatus> expectedResponse) {
        List<String> identer = expectedResponse.stream().map(IdentMedStatus::getIdent).collect(Collectors.toList());
        return "[\"" + String.join("\",\"", identer) + "\"]";
    }
    
    private void mockTps() throws JMSException {
        when(messageQueueConsumer.sendMessage(any()))
                .thenReturn(getResourceFileContent("testdatacontroller/checkidentlist/finn_identer_i_TPS_response.xml"));
    }
    
    public String setupTestdataInTpsfDatabase() {
        testgruppe = gruppeRepository.save(Gruppe.builder().navn("regresjonstestgruppenavn").build());
        Person person = personRepository.save(Person.builder()
                .ident(gyldigIdentEksistererITpsf)
                .identtype("FNR")
                .gruppe(testgruppe)
                .identtype("per")
                .kjonn('m')
                .regdato(LocalDateTime.now())
                .fornavn("lol")
                .etternavn("sdf")
                .statsborgerskap("nor")
                .build());
        return person.getIdent();
    }
}