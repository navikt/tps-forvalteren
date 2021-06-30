package no.nav.tps.forvalteren.testdatacontroller;

import static java.util.Arrays.asList;
import static no.nav.tps.forvalteren.consumer.mq.consumers.MessageQueueConsumer.DEFAULT_LES_TIMEOUT;
import static org.assertj.core.util.Sets.newHashSet;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;
import java.util.Set;
import javax.jms.JMSException;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.jupiter.api.Disabled;
import org.mockito.Mock;
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
import no.nav.tps.forvalteren.domain.rs.RsPersonKriterier;
import no.nav.tps.forvalteren.service.command.testdata.FiktiveIdenterGenerator;

public class CreateNewPersonsFromKriterierCompTest extends AbstractTestdataControllerComponentTest {

    private static final Set<String> IDENTER = newHashSet(asList("10050552665", "04121656499", "12017500617", "11031250855"));

    private Long gruppeId;
    private Gruppe testgruppe;

    @Mock
    private FetchEnvironmentsManager fetchEnvironmentsManager; //Alternativet er å wiremocke https://fasit.adeo.no/api/v2/applicationinstances?application=tpsws&usage=true

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

        when(fiktiveIdenterGeneratormock.genererFiktiveIdenter(any(RsPersonKriterier.class))).thenReturn(IDENTER);

        when(fetchEnvironmentsManager.getEnvironments("tpsws")).thenReturn(ENV_SET);

        mockTps();
    }

    @Test
    @Ignore
    @Disabled
    @WithUserDetails(TestUserDetails.USERNAME)
    @Transactional
    public void shouldOppretteNyeTestpersonerBasertPaaKriteriene() throws Exception {
        endTransactionIfActive();
        setupTestdataInTpsfDatabase();

//        when(adresseServiceConsumer.getAdresser(anyString(), eq(2)))
//                .thenReturn(List.of(VegadresseDTO.builder().kommunenummer("0901").build(),
//                        VegadresseDTO.builder().kommunenummer("0901").build()));

        mvc.perform(post(getUrl()).contentType(MediaType.APPLICATION_JSON)
                .content(
                        "{\"personKriterierListe\":[{\"identtype\":\"FNR\",\"kjonn\":\"K\",\"foedtEtter\":\"2016-02-16T00:00:00.000Z\",\"foedtFoer\":\"2018-04-20T00:00:00.000Z\",\"antall\":\"2\"}],\"withAdresse\":true}"))
                .andExpect(status().isOk());

        verify(messageQueueConsumer, times(14)).sendMessage(
                removeWhitespaceBetweenTags(getResourceFileContent("testdatacontroller/createNewPersonsFromKriterier/finn_identer_i_TPS_request.xml")), DEFAULT_LES_TIMEOUT);

        TestTransaction.start(); //Start transaksjon pga. lazy fetch i kall fra databasen
        assertCreatedTestdataInDatabase();
        TestTransaction.end();
    }

    private void setupTestdataInTpsfDatabase() {
        testgruppe = gruppeRepository.save(Gruppe.builder().navn("Create New Persons From Criteria Gr.navn").build());
        gruppeId = testgruppe.getId();
    }

    private void mockTps() throws JMSException {
        when(messageQueueConsumer.sendMessage(eq(removeWhitespaceBetweenTags(getResourceFileContent("testdatacontroller/createNewPersonsFromKriterier/finn_identer_i_TPS_request.xml"))), anyLong()))
                .thenReturn(getResourceFileContent("testdatacontroller/createNewPersonsFromKriterier/finn_identer_i_TPS_response.xml"));
    }

    private void assertCreatedTestdataInDatabase() {
        List<Person> lagredePersoner = gruppeRepository.findById(gruppeId).getPersoner();
        assertEquals(2, lagredePersoner.size());
        lagredePersoner.forEach(person -> {
            assertThat(person.getIdenttype(), is(equalTo("FNR")));
            assertThat(person.getKjonn(), is(equalTo("K")));
            assertTrue(IDENTER.contains(person.getIdent()));
            assertNotNull(person.getBoadresse());
            assertThat(person.getBoadresse().get(0).getKommunenr(), is(equalTo("0901")));
        });
    }
}
