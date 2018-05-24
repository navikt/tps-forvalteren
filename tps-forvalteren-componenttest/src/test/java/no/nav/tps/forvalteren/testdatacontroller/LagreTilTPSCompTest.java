package no.nav.tps.forvalteren.testdatacontroller;

import static no.nav.tps.forvalteren.config.ComptestConfig.actualConnectedToEnvironments;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import javax.jms.JMSException;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.MvcResult;

import no.nav.tps.forvalteren.config.TestUserDetails;
import no.nav.tps.forvalteren.consumer.mq.consumers.MessageQueueConsumer;
import no.nav.tps.forvalteren.domain.jpa.Adresse;
import no.nav.tps.forvalteren.domain.jpa.Gateadresse;
import no.nav.tps.forvalteren.domain.jpa.Gruppe;
import no.nav.tps.forvalteren.domain.jpa.Person;

public class LagreTilTPSCompTest extends AbstractTestdataControllerComponentTest {
    private List<String> environments = Arrays.asList("t1", "t2", "u5");
    List<String> expectedSkdRequestsUrl = Arrays.asList("testdatacontroller/lagretiltps/skdmelding_request_SFE_ENDRINGSMELDING_fnr04121656499.txt");
    String xmlFindNonexistingIdenterInTpsUrl = "testdatacontroller/lagretiltps/finn_en_ledig_ident_request.xml";
    List<String> expectedTpsXmlRequestsUrl = Arrays.asList(xmlFindNonexistingIdenterInTpsUrl);
    List<String> expectedRequests = constructExpectedRequests();
    private Long gruppeId;
    private Gruppe testgruppe;
    
    @Autowired
    private MessageQueueConsumer messageQueueConsumer;
    
    @Override
    protected String getServiceUrl() {
        return "/tps/" + gruppeId;
    }
    
    @Before
    public void setup() throws JMSException {
        setupTestdataInTpsfDatabase();
        mockTps();
    }
    
    @Test
    @WithUserDetails(TestUserDetails.USERNAME)
    public void shouldSendSuccesfulSkdMessagesToTPS() throws Exception {
        MvcResult mvcResult = mvc.perform(post(getUrl()).contentType(MediaType.APPLICATION_JSON_UTF8)
                .content("[\"t1\", \"t2\", \"u5\"]"))
//                .content("[\""+String.join( "\",\"" , environments)+"\"]"))
                .andExpect(status().isOk()).andReturn();
    
    
        ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
        verify(messageQueueConsumer, times(6)).sendMessage(captor.capture());
        List<String> actualRequests = captor.getAllValues().stream().map(request -> removeNewLineAndTab(request)).collect(Collectors.toList());;
        
        assertEquals(expectedRequests.toString(),actualRequests.toString());
        assertCalledEnvironments();
        
    }
    
    private void mockTps() throws JMSException {
        //mock findPersonsNotInEnvironments:
        when(messageQueueConsumer.sendMessage( removeNewLineAndTab(getResourceFileContent(xmlFindNonexistingIdenterInTpsUrl))))
                .thenReturn(getResourceFileContent("testdatacontroller/lagretiltps/Finn_ledige_identer_FS03-FDLISTER-DISKNAVN-M_response_from_TPS.xml"));
        
        //    TODO mock    sendDoedsmeldinger
        //    TODO mock    sendRelasjonsmeldinger
        //    TODO mock    sendInnvandringsMeldinger
        //    TODO mock    sendUpdateInnvandringsMeldinger
    }
    
    private void setupTestdataInTpsfDatabase() {
        testgruppe = gruppeRepository.save(Gruppe.builder().navn(GRUPPENAVN).build());
        gruppeId = testgruppe.getId();
        opprettPersonerSomTriggerInnvandringsMelding();
        opprettPersonerSomTriggerUpdateInnvandringsMelding();
        opprettPersonerSomTriggerRelasjonsmelding();
        opprettPersonerSomTriggerDoedsmeldinger();
    }
    
    private void opprettPersonerSomTriggerInnvandringsMelding() {
        Person person1 = Person.builder().gruppe(testgruppe).ident("10050552565").build();
        Person person2 = Person.builder().gruppe(testgruppe).ident("11031250155").build();
        
        Adresse adressenTilPerson3 = Gateadresse.builder().husnummer("9354").gatekode("01415").adresse("KJØLVEGEN").build();
        adressenTilPerson3.setKommunenr("1112");
        adressenTilPerson3.setPostnr("1001");
        adressenTilPerson3.setFlyttedato(LocalDateTime.of(2018, 04, 26, 12, 11, 10));
        Person person3 = Person.builder().gruppe(testgruppe).ident("04121656499").identtype("FNR")
                .fornavn("GLITRENDE").etternavn("NORDMANN").statsborgerskap("349")
                .kjonn('M')
                .regdato(LocalDateTime.of(2018, 04, 26, 12, 11, 10))
                .boadresse(adressenTilPerson3).build();
        adressenTilPerson3.setPerson(person3);
        Person person4 = Person.builder().gruppe(testgruppe).ident("12017500617").build();
        Person person5 = Person.builder().gruppe(testgruppe).ident("02020403694").build();
        //        personRepository.save(person1);
        //        personRepository.save(person2);
        personRepository.save(person3);
        //        personRepository.save(person4);
        //        personRepository.save(person5);
        
    }
    
    private void opprettPersonerSomTriggerUpdateInnvandringsMelding() {
    
    }
    
    private void opprettPersonerSomTriggerRelasjonsmelding() {
    
    }
    
    private void opprettPersonerSomTriggerDoedsmeldinger() {
    
    }
    
    private String removeNewLineAndTab(String text) {
        return text.replace("\n", "").replace("\r", "").replace("\t","");
    }
    
    private void assertCalledEnvironments() {
        final List<String> actualEnvironmentsToRecieveSkdMelding = actualConnectedToEnvironments.stream().filter(pair -> "SFE_ENDRINGSMELDING".equals(pair.getValue())).map(pair -> pair.getKey()).collect(Collectors.toList());
        final List<String> actualEnvironmentsToRecieveXml = actualConnectedToEnvironments.stream().filter(pair -> "TPS_FORESPORSEL_XML_O".equals(pair.getValue())).map(pair -> pair.getKey()).collect(Collectors.toList());
        assertTrue("Sjekk at skdmeldinger blir sendt til alle miljøene",environments.stream().allMatch(env -> actualEnvironmentsToRecieveSkdMelding.contains(env)));
        assertTrue("Sjekk at xml-er blir sendt til alle miljøene",environments.stream().allMatch(env -> actualEnvironmentsToRecieveXml.contains(env)));
    }
    
    private List<String> constructExpectedRequests() {
        List<String> expectedRequests = new ArrayList<>();
        environments.forEach( env ->
            expectedRequests.addAll(expectedTpsXmlRequestsUrl.stream().map(expectedUrl -> getResourceFileContent(expectedUrl)).collect(Collectors.toList()))
        );
        environments.forEach( env ->
            expectedRequests.addAll(expectedSkdRequestsUrl.stream().map(expectedUrl -> getResourceFileContent(expectedUrl).replace("ENDOFFILE", "")).collect(Collectors.toList())) //ENDOFFILE må være med i filen, fordi IntelliJ trimmer filen av en eller annen grunn.
        );
        return expectedRequests.stream().map(request -> removeNewLineAndTab(request)).collect(Collectors.toList());
    }
    
}
