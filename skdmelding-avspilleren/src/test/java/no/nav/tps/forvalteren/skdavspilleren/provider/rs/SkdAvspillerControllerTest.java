package no.nav.tps.forvalteren.skdavspilleren.provider.rs;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import no.nav.tps.forvalteren.common.java.mapping.MapperConfig;
import no.nav.tps.forvalteren.skdavspilleren.domain.jpa.Avspillergruppe;
import no.nav.tps.forvalteren.skdavspilleren.provider.response.AvspillergruppeResponse;
import no.nav.tps.forvalteren.skdavspilleren.service.SkdAvspillerService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = MapperConfig.class)
public class SkdAvspillerControllerTest {
    
    private Avspillergruppe expected;
    @Mock
    private SkdAvspillerService skdAvspillerService;
    @InjectMocks
    private SkdAvspillerController skdAvspillerController;
    
    @Test
    public void shouldReturnereAvspillergruppene() {
        setupMock();
        
        List<AvspillergruppeResponse> allAvspillergrupper = skdAvspillerController.getAllAvspillergrupper();
        
        AvspillergruppeResponse actual = allAvspillergrupper.get(0);
        assertEquals(actual.getBeskrivelse(), expected.getBeskrivelse());
        assertEquals(actual.getNavn(), expected.getNavn());
        assertEquals(actual.getId(), expected.getId());
        assertEquals(actual.getEndretAv(), expected.getEndretAv());
        assertEquals(actual.getEndretDato(), expected.getEndretDato());
        assertEquals(actual.getOpprettetAv(), expected.getOpprettetAv());
        assertEquals(actual.getOpprettetDato(), expected.getOpprettetDato());
    }
    
    private void setupMock() {
        ArrayList<Avspillergruppe> serviceResponse = new ArrayList<>();
        expected = Avspillergruppe.builder().beskrivelse("bes").navn("navnet").id(221L).build();
        expected.setOpprettetDato(LocalDateTime.now());
        expected.setEndretDato(LocalDateTime.now());
        expected.setEndretAv("ole");
        expected.setOpprettetAv("oppr av");
        serviceResponse.add(expected);
        serviceResponse.add(expected);
        
        when(skdAvspillerService.getAllAvspillergrupper()).thenReturn(serviceResponse);
    }
}