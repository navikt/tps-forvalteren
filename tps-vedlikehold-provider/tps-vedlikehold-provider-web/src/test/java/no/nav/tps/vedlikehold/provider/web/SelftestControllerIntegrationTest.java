package no.nav.tps.vedlikehold.provider.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import no.nav.tps.vedlikehold.provider.web.selftest.JsonSelftest;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.ui.Model;

import no.nav.tps.vedlikehold.common.java.message.MessageProvider;
import no.nav.tps.vedlikehold.provider.web.model.SelftestResult;
import no.nav.tps.vedlikehold.provider.web.selftest.Selftest;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

import javax.servlet.http.HttpSession;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


/**
 * @author Kristian Kyvik (Visma Consulting AS).
 */
@RunWith(SpringJUnit4ClassRunner.class)
//@ContextConfiguration(classes = {LocalApplicationConfig.class})
@WebAppConfiguration
public class SelftestControllerIntegrationTest {

    private static final String SESSION_ID = "sessionID";
    private MockMvc mockMvc;

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @InjectMocks
    private SelftestController controller;

    @Mock
    private HttpSession httpSessionMock;

    @Mock
    private Model modelMock;

    @Mock
    private ObjectMapper mapper;

    @Mock(name = "diskresjonskodeSelftest")
    private Selftest diskresjonskodeSelftest;

    @Mock(name = "egenansattSelftest")
    private Selftest egenansattSelftest;

    @Mock(name = "veraSelftest")
    private Selftest veraSelftest;

    @Mock(name = "fasitSelftest")
    private Selftest fasitSelftest;

    @Mock(name = "mqSelftest")
    private Selftest mqSelftest;

    @Mock
    private MessageProvider messageProviderMock;

    @Before
    public void setUpHappyPath() throws Exception{
        SelftestResult diskresjonskodeResults = new SelftestResult("TPSWS Diskresjonskode");
        when(diskresjonskodeSelftest.perform()).thenReturn(diskresjonskodeResults);

        SelftestResult egenansattResults = new SelftestResult("TPSWS Egen Ansatt");
        when(egenansattSelftest.perform()).thenReturn(egenansattResults);

        SelftestResult veraResults = new SelftestResult("Vera");
        when(veraSelftest.perform()).thenReturn(veraResults);

        SelftestResult fasitResults = new SelftestResult("Fasit");
        when(fasitSelftest.perform()).thenReturn(fasitResults);

        SelftestResult mqResults = new SelftestResult("Mq");
        when(mqSelftest.perform()).thenReturn(mqResults);

        when( httpSessionMock.getId() ).thenReturn(SESSION_ID);

        InternalResourceViewResolver viewResolver = new InternalResourceViewResolver();
        viewResolver.setPrefix("/WEB-INF/jsp/view/");
        viewResolver.setSuffix(".jsp");

        MockitoAnnotations.initMocks(this);

        this.mockMvc = MockMvcBuilders.standaloneSetup(controller).setViewResolvers(viewResolver).build();

    }


//    @Test
//    public void textHtmlIsReturnedWhenAcceptHeadersIsSetToTextHtml() throws Exception {
//
//        mockMvc.perform(get("/internal/selftest").accept(MediaType.TEXT_HTML))
//                .andExpect(status().isOk())
//                .andExpect(content().contentType("text/html"));
//    }
//
//    @Test
//    public void applicationJsonIsReturnedWhenAcceptHeadersIsSetToApplicationJson() throws Exception {
//        mockMvc.perform(get("/internal/selftest").accept(MediaType.APPLICATION_JSON))
//                .andExpect(status().isOk())
//                .andExpect(content().contentType("application/html"));
//    }
}