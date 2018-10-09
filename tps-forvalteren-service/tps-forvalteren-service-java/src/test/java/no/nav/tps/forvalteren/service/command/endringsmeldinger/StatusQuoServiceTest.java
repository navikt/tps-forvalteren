package no.nav.tps.forvalteren.service.command.endringsmeldinger;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import no.nav.tps.forvalteren.domain.rs.skd.RsMeldingstype;
import no.nav.tps.forvalteren.domain.rs.skd.RsMeldingstype1Felter;

@RunWith(MockitoJUnitRunner.class)
public class StatusQuoServiceTest {
    
    private static final Long GRUPPE_ID = 123L;
    private static final String gammelAdresse2 = "adresse2";
    private static final String nyAdresse1 = "NYadresse1";
    private static final String fodselsdatoPerson2 = "222222";
    private static final String fodselsdatoPerson1 = "111111";
    @Mock
    private SkdEndringsmeldingService skdEndringsmeldingService;
    @InjectMocks
    private StatusQuoService statusQuoService;
    
    @Test
    public void shouldgetStatusQuoOfPeopleinDatabaseSkdmeldingsgruppe() {
        mockdatabase();
        
        List<RsMeldingstype> statusQuoOfPeopleInGruppe = statusQuoService.getStatusQuoOfPeopleInGruppe(GRUPPE_ID);
        
        assertEquals(2, statusQuoOfPeopleInGruppe.size());
        
        RsMeldingstype1Felter statusQuo1 = (RsMeldingstype1Felter) statusQuoOfPeopleInGruppe.get(0);
        assertEquals(fodselsdatoPerson1, statusQuo1.getFodselsdato());
        assertNotNull(statusQuo1.getAntallBarn());
        assertEquals(nyAdresse1, statusQuo1.getAdresse1());
        assertEquals(gammelAdresse2, statusQuo1.getAdresse2());
        
        RsMeldingstype1Felter statusQuo2 = (RsMeldingstype1Felter) statusQuoOfPeopleInGruppe.get(1);
        assertEquals(fodselsdatoPerson2, statusQuo2.getFodselsdato());
        assertNull(statusQuo2.getAdresse1());
        assertNotNull(statusQuo2.getSivilstand());
    }
    
    private void mockdatabase() {
        when(skdEndringsmeldingService.findAllSkdmeldingerByGruppeIdOrderByIdAsc(GRUPPE_ID)).thenReturn(createSkdmeldinger());
    }
    
    private List<RsMeldingstype> createSkdmeldinger() {
        RsMeldingstype1Felter person1firstMessage = RsMeldingstype1Felter.builder()
                .fodselsdato(fodselsdatoPerson1).personnummer("12345")
                .adresse1("adresse1").adresse2(gammelAdresse2)
                .antallBarn("1")
                .build();
        
        RsMeldingstype1Felter person1secondMessage = RsMeldingstype1Felter.builder()
                .fodselsdato(fodselsdatoPerson1).personnummer("12345")
                .adresse1(nyAdresse1)
                .antallBarn(null)
                .build();
        
        RsMeldingstype1Felter person2firstMessage = RsMeldingstype1Felter.builder()
                .fodselsdato(fodselsdatoPerson2).personnummer("23456")
                .sivilstand("2")
                .build();
        return Arrays.asList(person1firstMessage, person2firstMessage, person1secondMessage);
    }
    
    @Test
    public void shouldmergeLatestHistoryIntoStatusQuo() {
        StatusQuoService statusQuoService = new StatusQuoService();
        
        final RsMeldingstype1Felter firstMessage = RsMeldingstype1Felter.builder()
                .fodselsdato("111111").personnummer("12345")
                .adresse1("adresse1").adresse2(gammelAdresse2)
                .antallBarn("1")
                .build();
        String FNR = firstMessage.getFodselsdato() + firstMessage.getPersonnummer();
        HashMap<String, RsMeldingstype1Felter> statusQuoSkdmeldingTrans1 = new HashMap<>();
        statusQuoService.mergeLatestHistoryToMap(firstMessage, statusQuoSkdmeldingTrans1);
        
        assertEquals(firstMessage, statusQuoSkdmeldingTrans1.get(FNR));
        
        final RsMeldingstype1Felter secondMessage = RsMeldingstype1Felter.builder()
                .fodselsdato("111111").personnummer("12345")
                .adresse1(nyAdresse1)
                .antallBarn(null)
                .build();
        statusQuoService.mergeLatestHistoryToMap(secondMessage, statusQuoSkdmeldingTrans1);
        
        assertNotNull(statusQuoSkdmeldingTrans1.get(FNR));
        assertNotNull(statusQuoSkdmeldingTrans1.get(FNR).getAntallBarn());
        assertEquals(nyAdresse1, statusQuoSkdmeldingTrans1.get(FNR).getAdresse1());
        assertEquals(gammelAdresse2, statusQuoSkdmeldingTrans1.get(FNR).getAdresse2());
    }
}