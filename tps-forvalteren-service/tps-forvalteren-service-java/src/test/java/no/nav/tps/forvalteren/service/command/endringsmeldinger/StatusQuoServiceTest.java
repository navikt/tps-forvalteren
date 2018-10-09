package no.nav.tps.forvalteren.service.command.endringsmeldinger;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.HashMap;
import org.junit.Test;

import no.nav.tps.forvalteren.domain.rs.skd.RsMeldingstype1Felter;

public class StatusQuoServiceTest {
    
    @Test
    public void shouldmergeLatestHistoryIntoStatusQuo() {
        StatusQuoService statusQuoService = new StatusQuoService();
        
        String gammelAdresse2 = "adresse2";
        final RsMeldingstype1Felter firstMessage = RsMeldingstype1Felter.builder()
                .fodselsdato("111111").personnummer("12345")
                .adresse1("adresse1").adresse2(gammelAdresse2)
                .antallBarn("1")
                .build();
        String FNR = firstMessage.getFodselsdato() + firstMessage.getPersonnummer();
        HashMap<String, RsMeldingstype1Felter> statusQuoSkdmeldingTrans1 = new HashMap<>();
        statusQuoService.mergeLatestHistoryToMap(firstMessage, statusQuoSkdmeldingTrans1);
        
        assertEquals(firstMessage, statusQuoSkdmeldingTrans1.get(FNR));
        
        String nyAdresse1 = "NYadresse1";
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