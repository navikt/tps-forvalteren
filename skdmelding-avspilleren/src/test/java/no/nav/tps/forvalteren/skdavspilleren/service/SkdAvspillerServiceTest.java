package no.nav.tps.forvalteren.skdavspilleren.service;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.HashMap;
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
import no.nav.tps.forvalteren.service.command.testdata.skd.SendSkdMeldingTilGitteMiljoer;
import no.nav.tps.forvalteren.skdavspilleren.domain.jpa.Avspillergruppe;
import no.nav.tps.forvalteren.skdavspilleren.domain.jpa.SkdmeldingAvspillerdata;
import no.nav.tps.forvalteren.skdavspilleren.repository.SkdmeldingAvspillerdataRepository;
import no.nav.tps.forvalteren.skdavspilleren.service.requests.StartAvspillingRequest;

@RunWith(MockitoJUnitRunner.class)
public class SkdAvspillerServiceTest {
    
    @Mock
    private SkdmeldingAvspillerdataRepository skdmeldingAvspillerdataRepository;
    
    @Mock
    private SendSkdMeldingTilGitteMiljoer sendSkdMeldingTilGitteMiljoer;
    
    @Mock
    private SkdMeldingResolver innvandring;
    
    private TpsSkdRequestMeldingDefinition tpsSkdRequestMeldingDefinition;
    private SkdAvspillerService skdAvspillerService;
    
    @Before
    public void before() {
        tpsSkdRequestMeldingDefinition = new TpsSkdRequestMeldingDefinition();
        when(innvandring.resolve()).thenReturn(tpsSkdRequestMeldingDefinition);
        skdAvspillerService = new SkdAvspillerService(skdmeldingAvspillerdataRepository, sendSkdMeldingTilGitteMiljoer, innvandring);
    }
    
    @Test
    public void shouldSendSkdMessagesFromGroup() {
        
        long gruppeId = 1L;
        Avspillergruppe avspillergruppe = Avspillergruppe.builder()
                .id(gruppeId).beskrivelse("noe")
                .navn("testgruppe").build();
        SkdmeldingAvspillerdata skdmeldingData1 = SkdmeldingAvspillerdata.builder().sekvensnummer(1L).skdmelding("skdmelding1").avspillergruppe(avspillergruppe).build();
        SkdmeldingAvspillerdata skdmeldingData2 = SkdmeldingAvspillerdata.builder().sekvensnummer(2L).skdmelding("skdmelding2").avspillergruppe(avspillergruppe).build();
        List<SkdmeldingAvspillerdata> skdmeldinger = Arrays.asList(skdmeldingData1, skdmeldingData2);
        avspillergruppe.setSkdmeldinger(skdmeldinger);
        when(skdmeldingAvspillerdataRepository.findByAvspillergruppeAndOrderBySekvensnummerAsc(gruppeId)).thenReturn(skdmeldinger);
        
        when(sendSkdMeldingTilGitteMiljoer.execute(any(), any(), any())).thenReturn(new HashMap<>());
        
        String miljoe = "u1";
        skdAvspillerService.start(new StartAvspillingRequest(gruppeId, miljoe));
        
        InOrder inOrder = Mockito.inOrder(sendSkdMeldingTilGitteMiljoer);
        
        HashSet environment = new HashSet<>();
        environment.add(miljoe);
        inOrder.verify(sendSkdMeldingTilGitteMiljoer).execute("skdmelding1", tpsSkdRequestMeldingDefinition, environment);
        inOrder.verify(sendSkdMeldingTilGitteMiljoer).execute("skdmelding2", tpsSkdRequestMeldingDefinition, environment);
        
    }
}