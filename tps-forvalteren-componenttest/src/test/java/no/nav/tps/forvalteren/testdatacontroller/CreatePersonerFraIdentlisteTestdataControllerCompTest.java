package no.nav.tps.forvalteren.testdatacontroller;

import static org.junit.Assert.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Arrays;
import java.util.List;
import org.junit.Test;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.transaction.annotation.Transactional;

import no.nav.tps.forvalteren.config.TestUserDetails;
import no.nav.tps.forvalteren.domain.jpa.Gruppe;
import no.nav.tps.forvalteren.domain.jpa.Person;

@Transactional
public class CreatePersonerFraIdentlisteTestdataControllerCompTest extends AbstractTestdataControllerComponentTest {
    
    private static final int IDENT1 = 03051750127;
    private static final int IDENT2 = 02304040404;
    private Long gruppeId;
    private Gruppe testgruppe;
    
    @Override
    protected String getServiceUrl() {
        return "/createpersoner/" + gruppeId;
    }
    
    public void setupTestdataInTpsfDatabase() {
        testgruppe = gruppeRepository.save(Gruppe.builder().navn("regresjonstestgruppenavn").build());
        gruppeId = testgruppe.getId();
    }
    
    /**
     * HVIS en liste med fødselsnummer/identer som er ledige og gyldige, sendes til createpersoner, SÅ skal det opprettes testpersoner i TPSF DB med disse identene.
     */
    @Test
    @WithUserDetails(TestUserDetails.USERNAME)
    public void shouldCreatePerson() throws Exception {
        setupTestdataInTpsfDatabase();
        mvc.perform(post(getUrl()).contentType(MediaType.APPLICATION_JSON_UTF8)
                .content("[\"" + IDENT1 + "\",\"" + IDENT2 + "\"]"))
                .andExpect(status().isOk());
        //sjekk db
        List<Person> persistertePersoner = personRepository.findByIdentIn(Arrays.asList("" + IDENT1, "" + IDENT2));
        assertEquals(2, persistertePersoner.size());
        persistertePersoner.forEach(person -> assertEquals(gruppeId, person.getGruppe().getId()));
    }
}

