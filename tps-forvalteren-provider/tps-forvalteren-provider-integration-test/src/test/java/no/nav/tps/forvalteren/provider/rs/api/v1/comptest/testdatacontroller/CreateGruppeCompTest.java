package no.nav.tps.forvalteren.provider.rs.api.v1.comptest.testdatacontroller;

import static org.junit.Assert.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Ignore;
import org.junit.Test;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.annotation.Commit;

import no.nav.tps.forvalteren.domain.jpa.Gruppe;
import no.nav.tps.forvalteren.provider.rs.api.v1.config.TestUserDetails;

public class CreateGruppeCompTest extends AbstractTestdataControllerComponentTest {
    private final String GRUPPENAVN = "unikt testgrnavn";
    private final String BESKRIVELSE = "beskrivelse her, takk";
    
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
    @Commit
    @Test(expected = DataIntegrityViolationException.class)
    @WithUserDetails(TestUserDetails.USERNAME)
    @Ignore("fixme: selv om expected er oppgitt, så blir den ikke fanget. ")
    public void shouldThrowExceptionAsGroupAlreadyExists() throws Exception {
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
