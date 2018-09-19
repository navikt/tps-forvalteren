package no.nav.tps.forvalteren.provider.rs.api.v1.endpoints.mapping;

import static no.nav.tps.forvalteren.domain.service.tps.servicerutiner.requests.hent.attributter.finngyldigeadresser.JaEllerNei.J;
import static no.nav.tps.forvalteren.domain.service.tps.servicerutiner.requests.hent.attributter.finngyldigeadresser.JaEllerNei.N;
import static no.nav.tps.forvalteren.domain.service.tps.servicerutiner.requests.hent.attributter.finngyldigeadresser.Sorteringskategorier.K;
import static no.nav.tps.forvalteren.domain.service.tps.servicerutiner.requests.hent.attributter.finngyldigeadresser.Typesok.E;
import static no.nav.tps.forvalteren.domain.service.tps.servicerutiner.requests.hent.attributter.finngyldigeadresser.Typesok.F;
import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

import no.nav.tps.forvalteren.domain.service.tps.servicerutiner.requests.hent.TpsFinnGyldigeAdresserRequest;
import no.nav.tps.forvalteren.provider.rs.api.v1.endpoints.request.param.FinnGyldigeAdresserRequestParam;

public class MapFinnGyldigeAdresserToTpsServiceRutineTest {
    private static String ADRESSENAVN = "adrnavn";
    private TpsFinnGyldigeAdresserRequest tpsServiceRoutineRequest;
    
    private MapFinnGyldigeAdresserToTpsServiceRutine finnGyldigeAdresserMapper = new MapFinnGyldigeAdresserToTpsServiceRutine();
    
    @Before
    public void setupTestdata() {
        tpsServiceRoutineRequest = TpsFinnGyldigeAdresserRequest.builder().typesok(F).visPostnr(J).alleSkrivevarianter(N).alltidRetur(J).maxRetur(5).build();
    }
    
    /**
     * De feltene i FinnGyldigeAdresserRequestParam som er null skal ikke mappes til destination-objektet tpsServiceRutineRequest.
     */
    @Test
    public void shouldOnlyMapNotNullFieldsToTpsServiceRutineRequest() {
        FinnGyldigeAdresserRequestParam finnGyldigeAdresserRequestParam = FinnGyldigeAdresserRequestParam.builder()
                .typesok(null)
                .visPostnr(null)
                .alleSkrivevarianter(null)
                .maxRetur(null)
                .adresseNavnsok("adrnavn")
                .build();
        
        tpsServiceRoutineRequest = finnGyldigeAdresserMapper.map(finnGyldigeAdresserRequestParam, tpsServiceRoutineRequest);
        
        assertEquals(F, tpsServiceRoutineRequest.getTypesok());
        assertEquals(J, tpsServiceRoutineRequest.getVisPostnr());
        assertEquals(N, tpsServiceRoutineRequest.getAlleSkrivevarianter());
        assertEquals(J, tpsServiceRoutineRequest.getAlltidRetur());
        assertEquals(new Integer(5), tpsServiceRoutineRequest.getMaxRetur());
        assertEquals(ADRESSENAVN, tpsServiceRoutineRequest.getAdresseNavnsok());
    }
    
    /**
     * Sjekker at alle felt mappes
     */
    @Test
    public void shouldMapNotNullFieldsToTpsServiceRutineRequest() {
        FinnGyldigeAdresserRequestParam finnGyldigeAdresserRequestParam = FinnGyldigeAdresserRequestParam.builder()
                .typesok(E)
                .visPostnr(N)
                .alleSkrivevarianter(J)
                .maxRetur(2)
                .adresseNavnsok("adrnavn")
                .husNrsok("123")
                .kommuneNrsok("321")
                .sortering(K)
                .build();
        
        tpsServiceRoutineRequest = finnGyldigeAdresserMapper.map(finnGyldigeAdresserRequestParam, tpsServiceRoutineRequest);
        
        assertSuccessfulMapping(finnGyldigeAdresserRequestParam, tpsServiceRoutineRequest);
    }
    
    private void assertSuccessfulMapping(FinnGyldigeAdresserRequestParam finnGyldigeAdresserRequestParam, TpsFinnGyldigeAdresserRequest tpsServiceRoutineRequest) {
        assertEquals(finnGyldigeAdresserRequestParam.getTypesok(), tpsServiceRoutineRequest.getTypesok());
        assertEquals(finnGyldigeAdresserRequestParam.getVisPostnr(), tpsServiceRoutineRequest.getVisPostnr());
        assertEquals(finnGyldigeAdresserRequestParam.getAlleSkrivevarianter(), tpsServiceRoutineRequest.getAlleSkrivevarianter());
        assertEquals(finnGyldigeAdresserRequestParam.getMaxRetur(), tpsServiceRoutineRequest.getMaxRetur());
        assertEquals(finnGyldigeAdresserRequestParam.getAdresseNavnsok(), tpsServiceRoutineRequest.getAdresseNavnsok());
        assertEquals(finnGyldigeAdresserRequestParam.getHusNrsok(), tpsServiceRoutineRequest.getHusNrsok());
        assertEquals(finnGyldigeAdresserRequestParam.getKommuneNrsok(), tpsServiceRoutineRequest.getKommuneNrsok());
        assertEquals(finnGyldigeAdresserRequestParam.getPostNrsok(), tpsServiceRoutineRequest.getPostNrsok());
        assertEquals(finnGyldigeAdresserRequestParam.getSortering(), tpsServiceRoutineRequest.getSortering());
    }
    
}