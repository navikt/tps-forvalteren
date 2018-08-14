package no.nav.tps.forvalteren.skdavspilleren.service;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

import java.util.Arrays;
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
import no.nav.tps.forvalteren.service.command.testdata.skd.impl.SendEnSkdMelding;
import no.nav.tps.forvalteren.skdavspilleren.common.exceptions.AvspillerDataNotFoundException;
import no.nav.tps.forvalteren.skdavspilleren.domain.jpa.Avspillergruppe;
import no.nav.tps.forvalteren.skdavspilleren.domain.jpa.SkdmeldingAvspillerdata;
import no.nav.tps.forvalteren.skdavspilleren.repository.AvspillergruppeRepository;
import no.nav.tps.forvalteren.skdavspilleren.repository.SkdmeldingAvspillerdataRepository;
import no.nav.tps.forvalteren.skdavspilleren.service.requests.StartAvspillingRequest;

@RunWith(MockitoJUnitRunner.class)
public class SkdAvspillerServiceTest {
    
    final long gruppeId = 1L;
    final String miljoe = "u1";
    
    @Mock
    private SkdmeldingAvspillerdataRepository skdmeldingAvspillerdataRepository;
    
    @Mock
    private AvspillergruppeRepository avspillergruppeRepository;
    
    @Mock
    private SendEnSkdMelding SendEnSkdMelding;
    
    @Mock
    private SkdMeldingResolver innvandring;
    
    private TpsSkdRequestMeldingDefinition tpsSkdRequestMeldingDefinition;
    private SkdAvspillerService skdAvspillerService;
    
    @Before
    public void before() {
        tpsSkdRequestMeldingDefinition = new TpsSkdRequestMeldingDefinition();
        when(innvandring.resolve()).thenReturn(tpsSkdRequestMeldingDefinition);
        skdAvspillerService = new SkdAvspillerService(skdmeldingAvspillerdataRepository, avspillergruppeRepository, SendEnSkdMelding, innvandring);
    }
    
    @Test
    public void shouldSendSkdMessagesFromGroup() {
        List<SkdmeldingAvspillerdata> skdmeldinger = createSkdmeldingAvspillerdataList();
        when(skdmeldingAvspillerdataRepository.findAllByAvspillergruppeIdOrderBySekvensnummerAsc(gruppeId)).thenReturn(skdmeldinger);
        
        when(SendEnSkdMelding.sendSkdMelding(any(), any(), any())).thenReturn(new String());
        
        skdAvspillerService.start(new StartAvspillingRequest(gruppeId, miljoe));
        
        InOrder inOrder = Mockito.inOrder(SendEnSkdMelding);
        inOrder.verify(SendEnSkdMelding).sendSkdMelding("skdmelding1", tpsSkdRequestMeldingDefinition, miljoe);
        inOrder.verify(SendEnSkdMelding).sendSkdMelding("skdmelding2", tpsSkdRequestMeldingDefinition, miljoe);
    }
    
    @Test(expected = AvspillerDataNotFoundException.class)
    public void shouldThrowNoContentExceptionWhenGruppeIdIsNotFound() {
        when(skdmeldingAvspillerdataRepository.findAllByAvspillergruppeIdOrderBySekvensnummerAsc(gruppeId)).thenReturn(null);
        
        skdAvspillerService.start(new StartAvspillingRequest(gruppeId, miljoe));
    }
    
    private List<SkdmeldingAvspillerdata> createSkdmeldingAvspillerdataList() {
        Avspillergruppe avspillergruppe = Avspillergruppe.builder()
                .id(gruppeId).beskrivelse("noe")
                .navn("testgruppe").build();
        SkdmeldingAvspillerdata skdmeldingData1 = SkdmeldingAvspillerdata.builder().sekvensnummer(1L).skdmelding("skdmelding1").avspillergruppe(avspillergruppe).build();
        SkdmeldingAvspillerdata skdmeldingData2 = SkdmeldingAvspillerdata.builder().sekvensnummer(2L).skdmelding("skdmelding2").avspillergruppe(avspillergruppe).build();
        List<SkdmeldingAvspillerdata> skdmeldinger = Arrays.asList(skdmeldingData1, skdmeldingData2);
        avspillergruppe.setSkdmeldinger(skdmeldinger);
        return skdmeldinger;
    }
    
    @Test
    public void shouldReportFailedSkdMessages() {
    
    }
    
    @Test
    public void shouldThrowExceptionWhenMiljoeIsOfWrongFormat() {
    
    }
}