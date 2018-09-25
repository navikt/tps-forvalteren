package no.nav.tps.forvalteren.testdatacontroller;

import static no.nav.tps.forvalteren.consumer.mq.consumers.MessageQueueConsumer.DEFAULT_TIMEOUT;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.jms.JMSException;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.transaction.TestTransaction;
import org.springframework.transaction.annotation.Transactional;

import no.nav.tps.forvalteren.config.TestUserDetails;
import no.nav.tps.forvalteren.consumer.mq.consumers.MessageQueueConsumer;
import no.nav.tps.forvalteren.consumer.rs.environments.FetchEnvironmentsManager;
import no.nav.tps.forvalteren.domain.jpa.Gruppe;
import no.nav.tps.forvalteren.domain.jpa.Person;
import no.nav.tps.forvalteren.service.command.testdata.FiktiveIdenterGenerator;

public class CreateNewPersonsFromKriterierCompTest extends AbstractTestdataControllerComponentTest {
    
    private static final Set<String> identer = new HashSet<>();
    private static final Set<String> fasitRegistrerteEnvMedTps = new HashSet<>();
    private Long gruppeId;
    private Gruppe testgruppe;
    
    @Autowired
    private FetchEnvironmentsManager fetchEnvironmentsManagerSpy; //Alternativet er å wiremocke https://fasit.adeo.no/api/v2/applicationinstances?application=tpsws&usage=true
    @Autowired
    private FiktiveIdenterGenerator fiktiveIdenterGeneratormock;
    @Autowired
    private MessageQueueConsumer messageQueueConsumer;
    
    @Override
    protected String getServiceUrl() {
        return "/personer/" + gruppeId;
    }
    
    @Before
    public void setup() throws JMSException {
        reset(messageQueueConsumer);
        identer.addAll(Arrays.asList("10050552665",
                "04121656499",
                "12017500617",
                "11031250855"));
        when(fiktiveIdenterGeneratormock.genererFiktiveIdenter(any())).thenReturn(identer);
        
        fasitRegistrerteEnvMedTps.addAll(Arrays.asList("q0"));
        when(fetchEnvironmentsManagerSpy.getEnvironments("tpsws")).thenReturn(fasitRegistrerteEnvMedTps);
        
        mockTps();
    }
    
    @Test
    @WithUserDetails(TestUserDetails.USERNAME)
    @Transactional
    public void shouldOppretteNyeTestpersonerBasertPaaKriteriene() throws Exception {
        endTransactionIfActive();
        setupTestdataInTpsfDatabase();
        
        mvc.perform(post(getUrl()).contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(
                        "{\"personKriterierListe\":[{\"identtype\":\"FNR\",\"kjonn\":\"K\",\"foedtEtter\":\"2016-02-16T00:00:00.000Z\",\"foedtFoer\":\"2018-04-20T00:00:00.000Z\",\"antall\":\"2\"}],\"withAdresse\":true}"))
                .andExpect(status().isOk());
        
        verify(messageQueueConsumer, times(fasitRegistrerteEnvMedTps.size())).sendMessage(
                removeNewLineAndTab(getResourceFileContent("testdatacontroller/createNewPersonsFromKriterier/finn_identer_i_TPS_request.xml")), DEFAULT_TIMEOUT);
        verify(messageQueueConsumer).sendMessage(eq(
                removeNewLineAndTab(getResourceFileContent("testdatacontroller/createNewPersonsFromKriterier/hentGyldigeAdresser_servicerutinen_S051_request.xml"))), anyLong());
        
        TestTransaction.start(); //Start transaksjon pga. lazy fetch i kall fra databasen
        assertCreatedTestdataInDatabase();
        TestTransaction.end();
    }
    
    private void setupTestdataInTpsfDatabase() {
        testgruppe = gruppeRepository.save(Gruppe.builder().navn("Create New Persons From Criteria Gr.navn").build());
        gruppeId = testgruppe.getId();
    }
    
    private void mockTps() throws JMSException {
        when(messageQueueConsumer.sendMessage(eq(removeNewLineAndTab(getResourceFileContent("testdatacontroller/createNewPersonsFromKriterier/finn_identer_i_TPS_request.xml"))),anyLong()))
                .thenReturn(getResourceFileContent("testdatacontroller/createNewPersonsFromKriterier/finn_identer_i_TPS_response.xml"));
        when(messageQueueConsumer.sendMessage(eq(removeNewLineAndTab(getResourceFileContent("testdatacontroller/createNewPersonsFromKriterier/hentGyldigeAdresser_servicerutinen_S051_request.xml"))),anyLong()))
                .thenReturn(getResourceFileContent("testdatacontroller/createNewPersonsFromKriterier/hentGyldigeAdresser_servicerutinen_S051_response.xml"));
    }
    
    private void assertCreatedTestdataInDatabase() {
        List<Person> lagredePersoner = gruppeRepository.findById(gruppeId).getPersoner();
        assertEquals(2, lagredePersoner.size());
        lagredePersoner.forEach(person -> {
            assertThat(person.getIdenttype(), is(equalTo("FNR")));
            assertThat(person.getKjonn(), is(equalTo("K")));
            assertTrue(identer.contains(person.getIdent()));
            assertNotNull(person.getBoadresse());
            assertThat(person.getBoadresse().getKommunenr(), is(equalTo("0901")));
        });
    }
    
}
