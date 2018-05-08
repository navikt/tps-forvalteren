package no.nav.tps.forvalteren.testdatacontroller;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Test;
import org.springframework.http.MediaType;
import org.springframework.test.context.transaction.TestTransaction;
import org.springframework.transaction.annotation.Transactional;

import no.nav.tps.forvalteren.domain.jpa.Gruppe;
import no.nav.tps.forvalteren.domain.jpa.Person;

public class UpdatePersonsCompTest extends AbstractTestdataControllerComponentTest {
    
    @Override protected String getServiceUrl() {
        return "/updatepersoner";
    }
    
    /**
     * HVIS tjenesten blir kalt med verdier som ikke kolliderer med unikhetskrav i databasen, SÅ skal personenes data oppdateres i TPSF DB.
     * Denne testen oppdaterer personene med ekteskapsrelasjon.
     */
    @Test
    @Transactional
    public void shouldUpdatePersons() throws Exception {
        endTransactionIfActive();
        setupTestdataInTpsfDatabase();
        mvc.perform(post(getUrl()).contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(getRequestBody("testdatacontroller/updatePersons_Request.json")))
                .andExpect(status().isOk());
        
        TestTransaction.start();
        assertUpdatedTestdataInDatabase();
        TestTransaction.end();
    }
    
    public void assertUpdatedTestdataInDatabase() {
        Person updatedPerson1 = personRepository.findById(1L);
        Person updatedPerson2 = personRepository.findById(2L);
        assertEquals("04021850026", updatedPerson1.getIdent());
        assertEquals("22071758072", updatedPerson2.getIdent());
        assertNotEquals(IDENT1, updatedPerson1.getIdent());
        assertNotEquals(IDENT2, updatedPerson2.getIdent());
        assertEquals(GRUPPENAVN, updatedPerson1.getGruppe().getNavn());
        assertEquals(GRUPPENAVN, updatedPerson2.getGruppe().getNavn());
        //TODO assert relasjon
    }
    
    private void setupTestdataInTpsfDatabase() {
        gruppeRepository.save(Gruppe.builder().navn(GRUPPENAVN).build());
        constructTestpersonsInTpsfDatabase();
    }
    
    private void endTransactionIfActive() {
        if (TestTransaction.isActive()) {
            TestTransaction.end();
        }
    }
}
