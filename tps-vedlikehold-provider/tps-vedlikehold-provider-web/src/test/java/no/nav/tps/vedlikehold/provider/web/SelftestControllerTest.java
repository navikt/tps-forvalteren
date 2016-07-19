package no.nav.tps.vedlikehold.provider.web;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.ui.Model;


import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.anyList;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;

import no.nav.tps.vedlikehold.common.java.message.MessageProvider;
import no.nav.tps.vedlikehold.provider.web.exception.SelftestFailureException;
import no.nav.tps.vedlikehold.provider.web.model.SelftestResult;
import no.nav.tps.vedlikehold.provider.web.model.SelftestResult.Status;
import no.nav.tps.vedlikehold.provider.web.selftest.Selftest;

import static no.nav.tps.vedlikehold.provider.web.model.SelftestResult.Status.FEILET;
import static org.mockito.Mockito.*;

/**
 * @author Kristian Kyvik (Visma Consulting AS).
 */
@RunWith(MockitoJUnitRunner.class)
public class SelftestControllerTest {

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @InjectMocks
    private SelftestController controller;

    @Mock
    private Model modelMock;

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
    public void setUpHappyPath() {
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
    }


    @Test
    public void throwsExceptionWithMessageFromMessageProviderWhenDiskresjonskodeFailedAndStatusIsRequested() throws Exception {
        SelftestResult diskresjonskodeTestResult = new SelftestResult("Diskresjonskode", "TPSWS - Diskresjonskode is down");
        when(diskresjonskodeSelftest.perform()).thenReturn(diskresjonskodeTestResult);
        when(messageProviderMock.get(anyString(), eq("Diskresjonskode"))).thenReturn("The following sub-systems are down: Diskresjonskode");

        expectedException.expect(SelftestFailureException.class);
        expectedException.expectMessage("The following sub-systems are down: Diskresjonskode");

        controller.selftest("status", modelMock);
    }

    @Test
    public void throwsExceptionWithMessageFromMessageProviderWhenEgenAnsattFailedAndStatusIsRequested() throws Exception {
        SelftestResult egenansattTestResult = new SelftestResult("Egen Ansatt", "TPSWS - Egen Ansatt is down");
        when(egenansattSelftest.perform()).thenReturn(egenansattTestResult);
        when(messageProviderMock.get(anyString(), eq("Egen Ansatt"))).thenReturn("The following sub-systems are down: Egen Ansatt");

        expectedException.expect(SelftestFailureException.class);
        expectedException.expectMessage("The following sub-systems are down: Egen Ansatt");

        controller.selftest("status", modelMock);
    }

    @Test
    public void throwsExceptionWithMessageFromMessageProviderWhenVeraFailedAndStatusIsRequested() throws Exception {
        SelftestResult veraTestResult = new SelftestResult("Vera", "Vera is down");
        when(veraSelftest.perform()).thenReturn(veraTestResult);
        when(messageProviderMock.get(anyString(), eq("Vera"))).thenReturn("The following sub-systems are down: Vera");

        expectedException.expect(SelftestFailureException.class);
        expectedException.expectMessage("The following sub-systems are down: Vera");

        controller.selftest("status", modelMock);
    }

    @Test
    public void throwsExceptionWithMessageFromMessageProviderWhenFasitFailedAndStatusIsRequested() throws Exception {
        SelftestResult fasitTestResult = new SelftestResult("Fasit", "Fasit is down");
        when(fasitSelftest.perform()).thenReturn(fasitTestResult);
        when(messageProviderMock.get(anyString(), eq("Fasit"))).thenReturn("The following sub-systems are down: Fasit");

        expectedException.expect(SelftestFailureException.class);
        expectedException.expectMessage("The following sub-systems are down: Fasit");

        controller.selftest("status", modelMock);
    }

    @Test
    public void throwsExceptionWithMessageFromMessageProviderWhenMqFailedAndStatusIsRequested() throws Exception {
        SelftestResult mqTestResult = new SelftestResult("Mq", "Mq is down");
        when(mqSelftest.perform()).thenReturn(mqTestResult);
        when(messageProviderMock.get(anyString(), eq("Mq"))).thenReturn("The following sub-systems are down: Mq");

        expectedException.expect(SelftestFailureException.class);
        expectedException.expectMessage("The following sub-systems are down: Mq");

        controller.selftest("status", modelMock);
    }

    @Test
    public void setsAggregateStatusToFeiletWhenDiskresjonskodeFailedAndStatusIsNotRequested() throws Exception {
        SelftestResult diskresjonskodeTestResult = new SelftestResult("Diskresjonskode", "TPSWS - Diskresjonskode is down");
        when(diskresjonskodeSelftest.perform()).thenReturn(diskresjonskodeTestResult);

        controller.selftest(null, modelMock);

        ArgumentCaptor<Status> statusCaptor = ArgumentCaptor.forClass(SelftestResult.Status.class);
        verify(modelMock, atLeastOnce()).addAttribute(anyString(), statusCaptor.capture());
        assertThat(statusCaptor.getAllValues().contains(FEILET), is(true));
    }

    @Test
    public void setsAggregateStatusToFeiletWhenEgenAnsattFailedAndStatusIsNotRequested() throws Exception {
        SelftestResult egenansattTestResult = new SelftestResult("Egen Ansatt", "TPSWS - Egen Ansatt is down");
        when(egenansattSelftest.perform()).thenReturn(egenansattTestResult);

        controller.selftest(null, modelMock);

        ArgumentCaptor<SelftestResult.Status> statusCaptor = ArgumentCaptor.forClass(SelftestResult.Status.class);
        verify(modelMock, atLeastOnce()).addAttribute(anyString(), statusCaptor.capture());
        assertThat(statusCaptor.getAllValues().contains(FEILET), is(true));
    }

    @Test
    public void setsAggregateStatusToFeiletWhenVeraFailedAndStatusIsNotRequested() throws Exception {
        SelftestResult veraTestResult = new SelftestResult("Vera", "Vera is down");
        when(veraSelftest.perform()).thenReturn(veraTestResult);

        controller.selftest(null, modelMock);

        ArgumentCaptor<SelftestResult.Status> statusCaptor = ArgumentCaptor.forClass(SelftestResult.Status.class);
        verify(modelMock, atLeastOnce()).addAttribute(anyString(), statusCaptor.capture());
        assertThat(statusCaptor.getAllValues().contains(FEILET), is(true));
    }

    @Test
    public void setsAggregateStatusToFeiletWhenFasitFailedAndStatusIsNotRequested() throws Exception {
        SelftestResult fasitTestResult = new SelftestResult("Fasit", "Fasit is down");
        when(fasitSelftest.perform()).thenReturn(fasitTestResult);

        controller.selftest(null, modelMock);

        ArgumentCaptor<SelftestResult.Status> statusCaptor = ArgumentCaptor.forClass(SelftestResult.Status.class);
        verify(modelMock, atLeastOnce()).addAttribute(anyString(), statusCaptor.capture());
        assertThat(statusCaptor.getAllValues().contains(FEILET), is(true));
    }

    @Test
    public void setsAggregateStatusToFeiletWhenMqFailedAndStatusIsNotRequested() throws Exception {
        SelftestResult mqTestResult = new SelftestResult("Mq", "Mq is down");
        when(fasitSelftest.perform()).thenReturn(mqTestResult);

        controller.selftest(null, modelMock);

        ArgumentCaptor<SelftestResult.Status> statusCaptor = ArgumentCaptor.forClass(SelftestResult.Status.class);
        verify(modelMock, atLeastOnce()).addAttribute(anyString(), statusCaptor.capture());
        assertThat(statusCaptor.getAllValues().contains(FEILET), is(true));
    }

    @Test
    public void throwsExceptionWithMessageFromMessageProviderWhenDiskresjonskodeAndEgenAnsattandVeraFailedAndStatusIsRequested() throws Exception {
        when(diskresjonskodeSelftest.perform()).thenReturn(new SelftestResult("Diskresjonskode", "TPSWS - Diskresjonskode is down"));
        when(egenansattSelftest.perform()).thenReturn(new SelftestResult("Egen Ansatt", "TPSWS - Egen Ansatt is down"));
        when(veraSelftest.perform()).thenReturn(new SelftestResult("Vera", "Vera is down"));
        when(fasitSelftest.perform()).thenReturn(new SelftestResult("Fasit", "Fasit is down"));
        when(mqSelftest.perform()).thenReturn(new SelftestResult("Mq", "Mq is down"));



        when(messageProviderMock.get(anyString(), eq("Diskresjonskode,Egen Ansatt,Vera,Fasit,Mq"))).thenReturn("The following sub-systems are down: Diskresjonskode,Egen Ansatt,Vera,Fasit,Mq");

        expectedException.expect(SelftestFailureException.class);
        expectedException.expectMessage("The following sub-systems are down: Diskresjonskode,Egen Ansatt,Vera,Fasit,Mq");

        controller.selftest("status", modelMock);
    }

    @Test
    public void returnsSelftestPageWhenNoSubSystemsFailAndStatusIsRequested() throws Exception{
        String result = controller.selftest("status", modelMock);

        assertThat(result, is(equalTo("selftest")));
    }

    @Test
    public void returnsSelftestPageWhenNoSubSystemsFailAndStatusIsNotRequested() {
        String result = controller.selftest(null, modelMock);

        assertThat(result, is(equalTo("selftest")));
    }

    @Test
    public void addsApplicationInformationToModel() {
        controller.selftest(null, modelMock);

        verify(modelMock).addAttribute(eq("bootstrapVersion"), anyString());
        verify(modelMock).addAttribute(eq("applicationName"), anyString());
        verify(modelMock).addAttribute(eq("applicationProperties"), anyList());
        verify(modelMock).addAttribute(eq("aggregateStatus"), anyString());
        verify(modelMock).addAttribute(eq("selftestResults"), anyList());
    }
}