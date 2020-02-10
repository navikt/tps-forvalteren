package no.nav.tps.forvalteren.testdatacontroller;

import static org.junit.Assert.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;
import java.util.stream.Collectors;
import org.junit.Test;
import org.springframework.http.MediaType;

import no.nav.tps.forvalteren.domain.jpa.Person;

public class DeletePersonsCompTest extends AbstractTestdataControllerComponentTest {
    
    @Override
    protected String getServiceUrl() {
        return "/deletepersoner";
    }
    
    /**
     * HVIS id-ene eksisterer i TPSF DB, SÅ skal de slettes via REST-tjenesten deletepersoner.
     */
    @Test
    public void shouldDeletePersons() throws Exception {
        List ids = setupTestdataPersonerInTpsfDatabase()
                .stream().map(Person::getId).collect(Collectors.toList());
        mvc.perform(post(getUrl()).contentType(MediaType.APPLICATION_JSON).content("{\"ids\":" + ids.toString() + "}")).andExpect(status().isOk());
        
        assertTrue(personRepository.findAllByOrderByIdAsc().isEmpty());
    }
    
    /**
     * HVIS id-ene ikke eksisterer i TPSF DB, SÅ skal ingenting skje når man prøver å slette dem via REST-tjenesten deletepersoner.
     *
     * @throws Exception
     */
    @Test
    public void shouldReturnOkIfDeletionOfNonexistingPerson() throws Exception {
        personRepository.deleteAll();
        mvc.perform(post(getUrl()).contentType(MediaType.APPLICATION_JSON).content("{\"ids\":[100053405,100053406]}")).andExpect(status().isOk());
        
    }
}
