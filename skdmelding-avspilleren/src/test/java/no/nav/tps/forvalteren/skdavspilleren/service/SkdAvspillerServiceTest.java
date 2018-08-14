package no.nav.tps.forvalteren.skdavspilleren.service;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import no.nav.tps.forvalteren.domain.service.tps.servicerutiner.definition.TpsSkdRequestMeldingDefinition;
import no.nav.tps.forvalteren.domain.service.tps.servicerutiner.definition.resolvers.skdmeldinger.SkdMeldingResolver;
import no.nav.tps.forvalteren.service.command.FilterEnvironmentsOnDeployedEnvironment;
import no.nav.tps.forvalteren.service.command.testdata.skd.impl.SendEnSkdMelding;
import no.nav.tps.forvalteren.skdavspilleren.common.exceptions.AvspillerDataNotFoundException;
import no.nav.tps.forvalteren.skdavspilleren.domain.jpa.Avspillergruppe;
import no.nav.tps.forvalteren.skdavspilleren.domain.jpa.SkdmeldingAvspillerdata;
import no.nav.tps.forvalteren.skdavspilleren.repository.AvspillergruppeRepository;
import no.nav.tps.forvalteren.skdavspilleren.repository.SkdmeldingAvspillerdataRepository;
import no.nav.tps.forvalteren.skdavspilleren.service.requests.StartAvspillingRequest;
import no.nav.tps.forvalteren.skdavspilleren.service.response.StartAvspillingResponse;

@RunWith(MockitoJUnitRunner.class)
public class SkdAvspillerServiceTest {
    
    final long gruppeId = 1L;
    final String miljoe = "u5";
    
    @Mock
    private SkdmeldingAvspillerdataRepository skdmeldingAvspillerdataRepository;
    @Mock
    private AvspillergruppeRepository avspillergruppeRepository;
    @Mock
    private SendEnSkdMelding SendEnSkdMelding;
    @Mock
    private FilterEnvironmentsOnDeployedEnvironment filterEnvironmentsOnDeployedEnvironment;
    @Mock
    private SkdMeldingResolver innvandring;
    
    private TpsSkdRequestMeldingDefinition tpsSkdRequestMeldingDefinition;
    private SkdAvspillerService skdAvspillerService;
    private SkdmeldingAvspillerdata skdmeldingData1;
    private SkdmeldingAvspillerdata skdmeldingData2;
    private List<SkdmeldingAvspillerdata> skdmeldinger;
    
    @Before
    public void before() {
        tpsSkdRequestMeldingDefinition = new TpsSkdRequestMeldingDefinition();
        when(innvandring.resolve()).thenReturn(tpsSkdRequestMeldingDefinition);
        HashSet<String> filteredEnv = new HashSet<>();
        filteredEnv.add(miljoe);
        when(filterEnvironmentsOnDeployedEnvironment.execute(any())).thenReturn(filteredEnv);
        
        skdAvspillerService = new SkdAvspillerService(skdmeldingAvspillerdataRepository, avspillergruppeRepository,
                SendEnSkdMelding, filterEnvironmentsOnDeployedEnvironment, innvandring);
        
        createSkdmeldingAvspillerdataList();
    }
    
    private void createSkdmeldingAvspillerdataList() {
        Avspillergruppe avspillergruppe = Avspillergruppe.builder()
                .id(gruppeId).beskrivelse("noe")
                .navn("testgruppe").build();
        skdmeldingData1 = SkdmeldingAvspillerdata.builder().sekvensnummer(1L).skdmelding("skdmelding1").avspillergruppe(avspillergruppe).build();
        skdmeldingData2 = SkdmeldingAvspillerdata.builder().sekvensnummer(2L).skdmelding("skdmelding2").avspillergruppe(avspillergruppe).build();
        skdmeldinger = Arrays.asList(skdmeldingData1, skdmeldingData2);
        avspillergruppe.setSkdmeldinger(skdmeldinger);
    }
    
    @Test
    public void shouldSendSkdMessagesFromGroup() {
        when(skdmeldingAvspillerdataRepository.findAllByAvspillergruppeIdOrderBySekvensnummerAsc(gruppeId)).thenReturn(skdmeldinger);
        
        when(SendEnSkdMelding.sendSkdMelding(any(), any(), any())).thenReturn("00");
        
        skdAvspillerService.start(new StartAvspillingRequest(gruppeId, miljoe));
        
        InOrder inOrder = Mockito.inOrder(SendEnSkdMelding);
        inOrder.verify(SendEnSkdMelding).sendSkdMelding("skdmelding1", tpsSkdRequestMeldingDefinition, miljoe);
        inOrder.verify(SendEnSkdMelding).sendSkdMelding("skdmelding2", tpsSkdRequestMeldingDefinition, miljoe);
    }
    
    @Test
    public void shouldReportFailedSkdMessages() {
        String feilmelding = "Feilmelding: skdmeldingen feilet";
        when(SendEnSkdMelding.sendSkdMelding(any(), any(), any())).thenReturn("00");
        when(SendEnSkdMelding.sendSkdMelding(skdmeldingData2.getSkdmelding(), tpsSkdRequestMeldingDefinition, miljoe)).thenReturn(feilmelding);
        when(skdmeldingAvspillerdataRepository.findAllByAvspillergruppeIdOrderBySekvensnummerAsc(gruppeId)).thenReturn(skdmeldinger);
        
        StartAvspillingResponse response = skdAvspillerService.start(new StartAvspillingRequest(gruppeId, miljoe));
        assertEquals(2, response.getAntallSendte());
        assertEquals(1, response.getAntallFeilet());
        assertEquals(feilmelding, response.getStatusFraFeilendeMeldinger().get(0).getStatus());
        assertEquals(2L, response.getStatusFraFeilendeMeldinger().get(0).getSekvensnummer().longValue());
    }
    
    @Test(expected = AvspillerDataNotFoundException.class)
    public void shouldThrowNoContentExceptionWhenGruppeIdIsNotFound() {
        when(skdmeldingAvspillerdataRepository.findAllByAvspillergruppeIdOrderBySekvensnummerAsc(gruppeId)).thenReturn(null);
        
        skdAvspillerService.start(new StartAvspillingRequest(gruppeId, miljoe));
    }
    
    @Test(expected = RuntimeException.class)
    public void shouldThrowExceptionWhenMiljoeIsNotAvailable() {
        when(filterEnvironmentsOnDeployedEnvironment.execute(any())).thenReturn(new HashSet<>());
        skdAvspillerService.start(new StartAvspillingRequest(gruppeId, "L0"));
    }
}