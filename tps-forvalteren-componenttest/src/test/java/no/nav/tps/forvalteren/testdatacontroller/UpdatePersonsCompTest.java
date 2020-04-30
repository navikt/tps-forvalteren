package no.nav.tps.forvalteren.testdatacontroller;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;
import org.junit.Test;
import org.springframework.http.MediaType;
import org.springframework.test.context.transaction.TestTransaction;
import org.springframework.transaction.annotation.Transactional;

import no.nav.tps.forvalteren.domain.jpa.Person;

public class UpdatePersonsCompTest extends AbstractTestdataControllerComponentTest {
    private static final String expectedIdent1AfterUpdate = "04021850026";
    private static final String expectedIdent2AfterUpdate = "22071758072";
    private static final String expectedRelasjonTypeNavn = "EKTEFELLE";
    
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
        List<Person> personList = setupTestdataPersonerInTpsfDatabase();
        mvc.perform(post(getUrl()).contentType(MediaType.APPLICATION_JSON)
                .content(createRequestBody(personList)))
                  .andExpect(status().isOk());
        
        TestTransaction.start(); //Start transaksjon pga. lazy fetch i kall fra databasen
        assertUpdatedTestdataInDatabase(personList);
        TestTransaction.end();
    }
    
    private String createRequestBody(List<Person> personList) {
        return getResourceFileContent("testdatacontroller/updatePersons_Request.json")
                .replace("\"id\": 1,", "\"id\": " + personList.get(0).getGruppe().getId() + ",")
                .replace("\"personId\": 1,", "\"personId\": " + personList.get(0).getId() + ",")
                .replace("\"personId\": 2,", "\"personId\": " + personList.get(1).getId() + ",");
    }
    
    public void assertUpdatedTestdataInDatabase(List<Person> originalPersonList) {
        Person updatedPerson1 = personRepository.findById(originalPersonList.get(0).getId()).get();
        Person updatedPerson2 = personRepository.findById(originalPersonList.get(1).getId()).get();
        assertEquals(expectedIdent1AfterUpdate, updatedPerson1.getIdent());
        assertEquals(expectedIdent2AfterUpdate, updatedPerson2.getIdent());
        assertNotEquals(IDENT1, updatedPerson1.getIdent());
        assertNotEquals(IDENT2, updatedPerson2.getIdent());
        assertEquals(GRUPPENAVN, updatedPerson1.getGruppe().getNavn());
        assertEquals(GRUPPENAVN, updatedPerson2.getGruppe().getNavn());
        assertEquals(expectedIdent2AfterUpdate, updatedPerson1.getRelasjoner().get(0).getPersonRelasjonMed().getIdent());
        assertEquals(expectedIdent1AfterUpdate, updatedPerson2.getRelasjoner().get(0).getPersonRelasjonMed().getIdent());
        assertEquals(expectedRelasjonTypeNavn, updatedPerson1.getRelasjoner().get(0).getRelasjonTypeNavn());
        assertEquals(expectedRelasjonTypeNavn, updatedPerson2.getRelasjoner().get(0).getRelasjonTypeNavn());
    }
    
}
