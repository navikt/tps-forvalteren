package no.nav.tps.forvalteren.testdatacontroller;

import static java.util.Arrays.asList;
import static no.nav.tps.forvalteren.ComptestConfig.actualConnectedToEnvironments;
import static no.nav.tps.forvalteren.consumer.mq.consumers.MessageQueueConsumer.DEFAULT_TIMEOUT;
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

/**
 * Komptesten utfører følgende fra REST-grensesnitt til mock-versjon av messageQueueConsumer:
 * REST-tjenesten for "Lagre til TPS" blir kalt med gruppe og miljøer satt i request. Basert på testpersonene lagret på denne gruppen i databasen, blir innvandringsmelding osv. sendt til TPS.
 * messageQueueConsumerMock er mocket ut.
 * Komponenttesten tester at riktige meldinger blir sendt, og i riktig rekkefølge, når tjenesten blir kalt og personene ligger lagret på gruppen i databasen.
 * <p>
 * Merk: I flyway-skriptet er regdato DATE. Derfor blir klokkeslettet satt til 00:00:00
 */
public class LagreTilTPSCompTest extends AbstractTestdataControllerComponentTest {
    
    private static final List<String> EXPECTED_SKD_INNVANDRING_CREATE_REQUESTS_URL = asList("testdatacontroller/lagretiltps/skdmelding_request_InnvandringCreate_fnr04121656499.txt",
            "testdatacontroller/lagretiltps/skdmelding_request_InnvandringCreate_fnr_10050552565.txt",
            "testdatacontroller/lagretiltps/skdmelding_request_InnvandringCreate_fnr_12017500617.txt",
            "testdatacontroller/lagretiltps/skdmelding_request_innvandringCreate_fnr_11031250155.txt");
    private static final List<String> EXPECTED_SKD_UPDATE_INNVANDRING_REQUESTS_URL = asList("testdatacontroller/lagretiltps/skdmelding_request_updateInnvandring_fnr_02020403694.txt");
    private static final List<String> EXPECTED_SKD_RELASJONSMELDING_ER_REQUESTS_URL = asList("testdatacontroller/lagretiltps/skdmelding_request_Vigselsmelding_ektemann.txt",
            "testdatacontroller/lagretiltps/skdmelding_request_Vigselsmelding_kone.txt");
    private static final List<String> EXPECTED_SKD_DOEDSMELDING_IS_REQUESTS_URL = asList("testdatacontroller/lagretiltps/skdmelding_request_doedsmelding_fnr_11031250155.txt");
    private static final String XML_FIND_NON_EXISTING_IDENTS_IN_TPS_URL = "testdatacontroller/lagretiltps/Finn_identer_i_TPS_FS03-FDLISTER-DISKNAVN-M_request.xml";
    private static final List<String> ENVIRONMENTS = asList("t1", "t2", "u5", "q0");
    private List<String> expectedSkdRequests = constructExpectedRequests();
    
    private Long gruppeId;
    private Gruppe testgruppe;

    @Autowired
    private MessageQueueConsumer messageQueueConsumerMock;
    
    @Override
    protected String getServiceUrl() {
        return "/tps/" + gruppeId;
    }
    
    @Before
    public void setup() throws JMSException {
        reset(messageQueueConsumerMock);
        setupTestdataInTpsfDatabase();
        mockTps();
    }

    @Test
    @WithUserDetails(TestUserDetails.USERNAME)
    public void shouldSendSuccesfulSkdMessagesToTPS() throws Exception {
        mvc.perform(post(getUrl()).contentType(MediaType.APPLICATION_JSON_UTF8)
                .content("[\"" + String.join("\",\"", ENVIRONMENTS) + "\"]"))
                .andExpect(status().isOk());
    
        verify(messageQueueConsumerMock, times(ENVIRONMENTS.size())).sendMessage(removeNewLineAndTab(getResourceFileContent(XML_FIND_NON_EXISTING_IDENTS_IN_TPS_URL)), DEFAULT_TIMEOUT);
        
        assertCalledEnvironments();
        ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
        verify(messageQueueConsumerMock, times(expectedSkdRequests.size())).sendMessage(captor.capture());
        List<String> actualRequests = captor.getAllValues().stream().map(request -> removeNewLineAndTab(request)).collect(Collectors.toList());

        assertEquals(expectedSkdRequests.toString(), actualRequests.toString());
    }
    
    private void mockTps() throws JMSException {
        when(messageQueueConsumerMock.sendMessage(removeNewLineAndTab(getResourceFileContent(XML_FIND_NON_EXISTING_IDENTS_IN_TPS_URL)), DEFAULT_TIMEOUT))
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
                .kjonn("M")
                .regdato(LocalDateTime.of(2018, 04, 26, 00, 00, 00))
                .opprettetDato(LocalDateTime.now())
                .opprettetAv("A123456")
                .boadresse(adressenTilPerson3).build();
        adressenTilPerson3.setPerson(person3);
        
        personRepository.save(person3);
        
    }
    
    private void opprettPersonerSomTriggerUpdateInnvandringsMelding() {
        Person person5 = Person.builder().gruppe(testgruppe).ident("02020403694").identtype("FNR")
                .etternavn("Kake").fornavn("Snill")
                .kjonn("M")
                .regdato(LocalDateTime.of(2018, 04, 26, 00, 00, 00))
                .opprettetDato(LocalDateTime.now())
                .opprettetAv("A123456")
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
                .kjonn("K")
                .regdato(LocalDateTime.of(2018, 04, 05, 00, 00, 00))
                .opprettetDato(LocalDateTime.now())
                .opprettetAv("A123456")
                .boadresse(adressenTilEktemann).build();
        adressenTilEktemann.setPerson(ektemann);
        final Person lagretEktemann = personRepository.save(ektemann);
        
        Adresse adressenTilKone = Gateadresse.builder().husnummer("8400").gatekode("21485").adresse("SMELTEDIGELEN").build();
        adressenTilKone.setKommunenr("0301");
        adressenTilKone.setPostnr("0195");
        adressenTilKone.setFlyttedato(LocalDateTime.of(2018, 05, 15, 14, 10, 44));
        Person kone = Person.builder().gruppe(testgruppe).ident("12017500617").identtype("FNR")
                .fornavn("BLÅ").etternavn("KAFFI").statsborgerskap("000")
                .kjonn("M")
                .regdato(LocalDateTime.of(2018, 04, 05, 00, 00, 00))
                .opprettetDato(LocalDateTime.now())
                .opprettetAv("A123456")
                .boadresse(adressenTilKone).build();
        adressenTilKone.setPerson(kone);
        final Person lagretKone = personRepository.save(kone);
        
        relasjonRepository.save(Relasjon.builder().person(lagretEktemann).personRelasjonMed(lagretKone).relasjonTypeNavn("EKTEFELLE").build());
        relasjonRepository.save(Relasjon.builder().person(lagretKone).personRelasjonMed(lagretEktemann).relasjonTypeNavn("EKTEFELLE").build());
    }
    
    private void opprettPersonerSomTriggerDoedsmeldinger() {
        Person doedPerson = Person.builder().gruppe(testgruppe).ident("11031250155").identtype("DNR")
                .doedsdato(LocalDateTime.of(2018, 05, 15, 14, 10, 44))
                .kjonn("M")
                .fornavn("Døende").etternavn("Person")
                .regdato(LocalDateTime.of(2018, 04, 26, 00, 00, 00))
                .opprettetDato(LocalDateTime.now())
                .opprettetAv("A123456")
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
        assertTrue("Sjekk at skdmeldinger blir sendt til alle miljøene", ENVIRONMENTS.stream().allMatch(env -> actualEnvironmentsToRecieveSkdMelding.contains(env)));
        assertTrue("Sjekk at xml-er blir sendt til alle miljøene", ENVIRONMENTS.stream().allMatch(env -> actualEnvironmentsToRecieveXml.contains(env)));
        actualConnectedToEnvironments.clear();
    }
    
    private List<String> constructExpectedRequests() {
        List<String> expectedRequests = new ArrayList<>();
        
        EXPECTED_SKD_INNVANDRING_CREATE_REQUESTS_URL.forEach(url -> ENVIRONMENTS.forEach(env -> expectedRequests.add(getResourceFileContent(url).replace("ENDOFFILE", ""))));
        EXPECTED_SKD_UPDATE_INNVANDRING_REQUESTS_URL.forEach(url -> ENVIRONMENTS.forEach(env -> expectedRequests.add(getResourceFileContent(url).replace("ENDOFFILE", ""))));
        EXPECTED_SKD_RELASJONSMELDING_ER_REQUESTS_URL.forEach(url -> ENVIRONMENTS.forEach(env -> expectedRequests.add(getResourceFileContent(url).replace("ENDOFFILE", ""))));
        EXPECTED_SKD_DOEDSMELDING_IS_REQUESTS_URL.forEach(url -> ENVIRONMENTS.forEach(env -> expectedRequests.add(getResourceFileContent(url).replace("ENDOFFILE", ""))));
        
        return expectedRequests.stream().map(request -> removeNewLineAndTab(request)).collect(Collectors.toList());
    }
    
}
