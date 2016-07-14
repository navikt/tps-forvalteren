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
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import no.nav.tps.vedlikehold.common.java.message.MessageProvider;
import no.nav.tps.vedlikehold.provider.web.exception.SelftestFailureException;
import no.nav.tps.vedlikehold.provider.web.model.SelftestResult;
import no.nav.tps.vedlikehold.provider.web.model.SelftestResult.Status;
import no.nav.tps.vedlikehold.provider.web.selftest.Selftest;

import static no.nav.tps.vedlikehold.provider.web.model.SelftestResult.Status.FEILET;

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

    @Mock
    private MessageProvider messageProviderMock;

    @Before
    public void setUpHappyPath() {
        SelftestResult diskresjonskodeResults = new SelftestResult("TPSWS Diskresjonskode");
        when(diskresjonskodeSelftest.perform()).thenReturn(diskresjonskodeResults);

        SelftestResult egenansattResults = new SelftestResult("TPSWS Egen Ansatt");
        when(egenansattSelftest.perform()).thenReturn(egenansattResults);
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
        SelftestResult egenansattTestResult = new SelftestResult("Egen Ansatt", "Egen Ansatt is down");
        when(egenansattSelftest.perform()).thenReturn(egenansattTestResult);

        controller.selftest(null, modelMock);

        ArgumentCaptor<SelftestResult.Status> statusCaptor = ArgumentCaptor.forClass(SelftestResult.Status.class);
        verify(modelMock, atLeastOnce()).addAttribute(anyString(), statusCaptor.capture());
        assertThat(statusCaptor.getAllValues().contains(FEILET), is(true));
    }

    @Test
    public void throwsExceptionWithMessageFromMessageProviderWhenDiskresjonskodeAndEgenAnsattFailedAndStatusIsRequested() throws Exception {
        when(diskresjonskodeSelftest.perform()).thenReturn(new SelftestResult("Diskresjonskode", "TPSWS - Diskresjonskode is down"));
        when(egenansattSelftest.perform()).thenReturn(new SelftestResult("Egen Ansatt", "Egen Ansatt is down"));
        when(messageProviderMock.get(anyString(), eq("Diskresjonskode,Egen Ansatt"))).thenReturn("The following sub-systems are down: Diskresjonskode,Egen Ansatt");

        expectedException.expect(SelftestFailureException.class);
        expectedException.expectMessage("The following sub-systems are down: Diskresjonskode,Egen Ansatt");

        controller.selftest("status", modelMock);
    }

    @Test
    public void returnsSelftestPageWhenNoSubSystemsFailAndStatusIsRequested() {
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