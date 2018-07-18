package no.nav.tps.forvalteren.testdatacontroller;

import static no.nav.tps.forvalteren.config.ComptestConfig.actualConnectedToEnvironments;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.reset;
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

import no.nav.tps.forvalteren.config.TestUserDetails;
import no.nav.tps.forvalteren.consumer.mq.consumers.MessageQueueConsumer;
import no.nav.tps.forvalteren.domain.jpa.Adresse;
import no.nav.tps.forvalteren.domain.jpa.Gateadresse;
import no.nav.tps.forvalteren.domain.jpa.Gruppe;
import no.nav.tps.forvalteren.domain.jpa.Person;
import no.nav.tps.forvalteren.domain.jpa.Relasjon;

public class LagreTilTPSCompTest extends AbstractTestdataControllerComponentTest {
    List<String> expectedSkdInnvandringCreateRequestsUrl = Arrays.asList("testdatacontroller/lagretiltps/skdmelding_request_InnvandringCreate_fnr04121656499.txt",
            "testdatacontroller/lagretiltps/skdmelding_request_InnvandringCreate_fnr_10050552565.txt",
            "testdatacontroller/lagretiltps/skdmelding_request_InnvandringCreate_fnr_12017500617.txt",
            "testdatacontroller/lagretiltps/skdmelding_request_innvandringCreate_fnr_11031250155.txt");
    List<String> expectedSkdUpdateInnvandringRequestsUrl = Arrays.asList("testdatacontroller/lagretiltps/skdmelding_request_updateInnvandring_fnr_02020403694.txt");
    List<String> expectedSkdRelasjonsmeldingerRequestsUrl = Arrays.asList("testdatacontroller/lagretiltps/skdmelding_request_Vigselsmelding_ektemann.txt",
            "testdatacontroller/lagretiltps/skdmelding_request_Vigselsmelding_kone.txt");
    List<String> expectedSkdDoedsmeldingerRequestsUrl = Arrays.asList("testdatacontroller/lagretiltps/skdmelding_request_doedsmelding_fnr_11031250155.txt");
    String xmlFindNonexistingIdenterInTpsUrl = "testdatacontroller/lagretiltps/Finn_identer_i_TPS_FS03-FDLISTER-DISKNAVN-M_request.xml";
    List<String> expectedTpsXmlRequestsUrl = Arrays.asList(xmlFindNonexistingIdenterInTpsUrl);
    private List<String> environments = Arrays.asList("t1", "t2", "u5");
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
        reset(messageQueueConsumer);
        setupTestdataInTpsfDatabase();
        mockTps();
    }
    
    @Test
    @WithUserDetails(TestUserDetails.USERNAME)
    public void shouldSendSuccesfulSkdMessagesToTPS() throws Exception {
//        TODO Noen verdier blir ikke satt som forvetnet. Tror det kanskje er dato, så er forskjll. 00000 istedenfor verdier.
//        mvc.perform(post(getUrl()).contentType(MediaType.APPLICATION_JSON_UTF8)
//                .content("[\"" + String.join("\",\"", environments) + "\"]"))
//                .andExpect(status().isOk());
//
//        ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
//        verify(messageQueueConsumer, times(expectedRequests.size())).sendMessage(captor.capture());
//        List<String> actualRequests = captor.getAllValues().stream().map(request -> removeNewLineAndTab(request)).collect(Collectors.toList());
//
//        assertEquals(expectedRequests.toString(), actualRequests.toString());
//        assertCalledEnvironments();
        
    }
    
    private void mockTps() throws JMSException {
        //mock findPersonsNotInEnvironments:
        when(messageQueueConsumer.sendMessage(removeNewLineAndTab(getResourceFileContent(xmlFindNonexistingIdenterInTpsUrl))))
                .thenReturn(getResourceFileContent("testdatacontroller/lagretiltps/Finn_identer_i_TPS_FS03-FDLISTER-DISKNAVN-M_response.xml"));
        
    }
    
    private void setupTestdataInTpsfDatabase() {
        testgruppe = gruppeRepository.save(Gruppe.builder().navn(GRUPPENAVN).build());
        gruppeId = testgruppe.getId();
        opprettPersonerSomTriggerInnvandringsMelding();
        opprettPersonerSomTriggerUpdateInnvandringsMelding();
        opprettPersonerSomTriggerRelasjonsmeldingVigsel();
        opprettPersonerSomTriggerDoedsmeldinger();
    }
    
    private void opprettPersonerSomTriggerInnvandringsMelding() {
        
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
        
        personRepository.save(person3);
        
    }
    
    private void opprettPersonerSomTriggerUpdateInnvandringsMelding() {
        Person person5 = Person.builder().gruppe(testgruppe).ident("02020403694").identtype("FNR")
                .etternavn("Kake").fornavn("Snill")
                .kjonn('M')
                .regdato(LocalDateTime.of(2018, 04, 26, 12, 11, 10)) //FIXME testen feiler ved at time i regdato ikke settes i skdmeldingens streng-versjon. Dette gjelder for samtlige skdmeldinger
                .build();
        personRepository.save(person5);
    }
    
    private void opprettPersonerSomTriggerRelasjonsmeldingVigsel() {
        Adresse adressenTilEktemann = Gateadresse.builder().husnummer("2").gatekode("16188").adresse("SANNERGATA").build();
        adressenTilEktemann.setKommunenr("0301");
        adressenTilEktemann.setPostnr("0557");
        adressenTilEktemann.setFlyttedato(LocalDateTime.of(2018, 04, 05, 11, 30, 28));
        Person ektemann = Person.builder().gruppe(testgruppe).ident("10050552565").identtype("FNR")
                .fornavn("KRIMINELL").etternavn("BUSK").statsborgerskap("000")
                .kjonn('K')
                .regdato(LocalDateTime.of(2018, 04, 05, 11, 30, 28))
                .boadresse(adressenTilEktemann).build();
        adressenTilEktemann.setPerson(ektemann);
        final Person lagretEktemann = personRepository.save(ektemann);
        
        Adresse adressenTilKone = Gateadresse.builder().husnummer("8400").gatekode("21485").adresse("SMELTEDIGELEN").build();
        adressenTilKone.setKommunenr("0301");
        adressenTilKone.setPostnr("0195");
        adressenTilKone.setFlyttedato(LocalDateTime.of(2018, 05, 15, 14, 10, 44));
        Person kone = Person.builder().gruppe(testgruppe).ident("12017500617").identtype("FNR")
                .fornavn("BLÅ").etternavn("KAFFI").statsborgerskap("000")
                .kjonn('M')
                .regdato(LocalDateTime.of(2018, 04, 05, 11, 30, 28))
                .boadresse(adressenTilKone).build();
        adressenTilKone.setPerson(kone);
        final Person lagretKone = personRepository.save(kone);
        
        relasjonRepository.save(Relasjon.builder().person(lagretEktemann).personRelasjonMed(lagretKone).relasjonTypeNavn("EKTEFELLE").build());
        relasjonRepository.save(Relasjon.builder().person(lagretKone).personRelasjonMed(lagretEktemann).relasjonTypeNavn("EKTEFELLE").build());
    }
    
    private void opprettPersonerSomTriggerDoedsmeldinger() {
        Person doedPerson = Person.builder().gruppe(testgruppe).ident("11031250155").identtype("DNR")
                .doedsdato(LocalDateTime.of(2018, 05, 15, 14, 10, 44))
                .kjonn('M')
                .fornavn("Døende").etternavn("Person")
                .regdato(LocalDateTime.of(2018, 04, 26, 12, 11, 10))
                .build();
        personRepository.save(doedPerson);
    }
    
    private void assertCalledEnvironments() {
        final List<String> actualEnvironmentsToRecieveSkdMelding = actualConnectedToEnvironments.stream()
                .filter(pair -> "SFE_ENDRINGSMELDING".equals(pair.getSecond()))
                .map(pair -> pair.getFirst())
                .collect(Collectors.toList());
        final List<String> actualEnvironmentsToRecieveXml = actualConnectedToEnvironments.stream()
                .filter(pair -> "TPS_FORESPORSEL_XML_O".equals(pair.getSecond()))
                .map(pair -> pair.getFirst())
                .collect(Collectors.toList());
        assertTrue("Sjekk at skdmeldinger blir sendt til alle miljøene", environments.stream().allMatch(env -> actualEnvironmentsToRecieveSkdMelding.contains(env)));
        assertTrue("Sjekk at xml-er blir sendt til alle miljøene", environments.stream().allMatch(env -> actualEnvironmentsToRecieveXml.contains(env)));
        actualConnectedToEnvironments.clear();
    }
    
    private List<String> constructExpectedRequests() {
        List<String> expectedRequests = new ArrayList<>();
        
        expectedTpsXmlRequestsUrl.forEach(url -> environments.forEach(env -> expectedRequests.add(getResourceFileContent(url))));
        expectedSkdInnvandringCreateRequestsUrl.forEach(url -> environments.forEach(env -> expectedRequests.add(getResourceFileContent(url).replace("ENDOFFILE", ""))));
        expectedSkdUpdateInnvandringRequestsUrl.forEach(url -> environments.forEach(env -> expectedRequests.add(getResourceFileContent(url).replace("ENDOFFILE", ""))));
        expectedSkdRelasjonsmeldingerRequestsUrl.forEach(url -> environments.forEach(env -> expectedRequests.add(getResourceFileContent(url).replace("ENDOFFILE", ""))));
        expectedSkdDoedsmeldingerRequestsUrl.forEach(url -> environments.forEach(env -> expectedRequests.add(getResourceFileContent(url).replace("ENDOFFILE", ""))));
        
        return expectedRequests.stream().map(request -> removeNewLineAndTab(request)).collect(Collectors.toList());
    }
    
}
