package no.nav.tps.forvalteren.testdatacontroller;

import static org.junit.Assert.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import org.junit.Test;
import org.springframework.http.MediaType;

import no.nav.tps.forvalteren.domain.jpa.Person;

public class DeletePersonsCompTest extends  AbstractTestdataControllerComponentTest {
    private final String IDENT = "11111111111";
    private final String IDENT2 = "22222222222";
    
    @Override protected String getServiceUrl() {
        return "/deletepersoner";
    }
    
    /**
     * HVIS id-ene eksisterer i TPSF DB, SÅ skal de slettes via REST-tjenesten deletepersoner.
     */
    @Test
    public void shouldDeletePersons() throws Exception {
        List ids= setupTestdataInDatabase();
        mvc.perform(post(getUrl()).contentType(MediaType.APPLICATION_JSON_UTF8).content("{\"ids\":"+ids.toString()+"}")).andExpect(status().isOk());
    
        assertTrue(personRepository.findAllByOrderByIdAsc().isEmpty());
    }
    
    private List setupTestdataInDatabase() {
        Person person= personRepository.save(Person.builder().identtype("per").kjonn('m').regdato(LocalDateTime.now()).fornavn("lol").etternavn("sdf").ident(IDENT).statsborgerskap("nor").build());
        Person person2=personRepository.save(Person.builder().identtype("per").kjonn('k').regdato(LocalDateTime.now()).fornavn("fnavn").etternavn("etternavn2").ident(IDENT2).statsborgerskap("nor").build());
        return Arrays.asList(person.getId(), person2.getId());
    }
    
    /**
     * HVIS id-ene ikke eksisterer i TPSF DB, SÅ skal ingenting skje når man prøver å slette dem via REST-tjenesten deletepersoner.
     * @throws Exception
     */
    @Test
    public void shouldReturnOkIfDeletionOfNonexistingPerson() throws Exception {
        personRepository.deleteAll();
        mvc.perform(post(getUrl()).contentType(MediaType.APPLICATION_JSON_UTF8).content("{\"ids\":[100053405,100053406]}")).andExpect(status().isOk());
        
    }
}
