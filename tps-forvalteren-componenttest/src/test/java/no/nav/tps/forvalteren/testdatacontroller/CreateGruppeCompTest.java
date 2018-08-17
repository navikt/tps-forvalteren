package no.nav.tps.forvalteren.testdatacontroller;

import static org.junit.Assert.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.hamcrest.Matchers;
import org.junit.Test;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;

import no.nav.tps.forvalteren.config.TestUserDetails;
import no.nav.tps.forvalteren.domain.jpa.Gruppe;

public class CreateGruppeCompTest extends AbstractTestdataControllerComponentTest {
    
    private static final String GRUPPENAVN = "unikt testgrnavn";
    private static final String BESKRIVELSE = "beskrivelse her, takk";
    
    @Override
    protected String getServiceUrl() {
        return "/gruppe";
    }
    
    /**
     * HVIS POST til REST-endepunktet gruppe utføres med navn som ikke eksisterer i databasen fra før av, SÅ skal denne gruppen opprettes i databasen og endepunktet returnere status 200.
     *
     * @throws Exception
     */
    @Test
    @WithUserDetails(TestUserDetails.USERNAME)
    public void shouldCreateGruppe() throws Exception {
        mvc.perform(post(getUrl()).contentType(MediaType.APPLICATION_JSON_UTF8)
                .content("{\"navn\":\"" + GRUPPENAVN + "\", \"beskrivelse\":\"" + BESKRIVELSE + "\"}"))
                .andExpect(status().isOk());
        assertTrue(gruppeRepository.findAllByOrderByIdAsc().stream().anyMatch(gruppe -> GRUPPENAVN.equals(gruppe.getNavn()) && BESKRIVELSE.equals(gruppe.getBeskrivelse())));
    }
    
    /**
     * HVIS post   til REST-endepunktet gruppe utføres med navn som eksisterer i databasen fra før av, SÅ skal operasjonen avbrytes og kaste feilmelding.
     *
     * @throws Exception
     */
    @Test
    @WithUserDetails(TestUserDetails.USERNAME)
    public void shouldThrowExceptionAsGroupAlreadyExists() throws Exception {
        expectedException.expectCause(Matchers.allOf(Matchers.instanceOf(DataIntegrityViolationException.class)));
        makeSureGroupExistsInDatabase(GRUPPENAVN);
        mvc.perform(post(getUrl()).contentType(MediaType.APPLICATION_JSON_UTF8)
                .content("{\"navn\":\"" + GRUPPENAVN + "\", \"beskrivelse\":\"" + BESKRIVELSE + "\"}"));
    }
    
    private void makeSureGroupExistsInDatabase(String navn) {
        if (!gruppeRepository.findAllByOrderByIdAsc().stream().anyMatch(gruppe -> navn.equals(gruppe.getNavn()))) {
            gruppeRepository.save(Gruppe.builder().navn(navn).build());
        }
    }
}
