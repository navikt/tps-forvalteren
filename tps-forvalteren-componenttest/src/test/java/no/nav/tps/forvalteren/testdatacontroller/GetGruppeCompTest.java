package no.nav.tps.forvalteren.testdatacontroller;

import static org.junit.Assert.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.junit.Test;
import org.springframework.test.context.transaction.TestTransaction;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;

import no.nav.tps.forvalteren.domain.jpa.Gruppe;
import no.nav.tps.forvalteren.domain.jpa.Person;
import no.nav.tps.forvalteren.domain.jpa.Tag;
import no.nav.tps.forvalteren.domain.rs.RsGruppe;
import no.nav.tps.forvalteren.domain.rs.RsPerson;

public class GetGruppeCompTest extends AbstractTestdataControllerComponentTest {
    
    private Long gruppeId;
    
    @Override
    protected String getServiceUrl() {
        return "/gruppe/" + gruppeId;
    }
    
    @Test
    @Transactional
    public void shouldReturnereGruppeInfo() throws Exception {
        endTransactionIfActive();
        setupTestdataInTpsfDatabase();
        
        MvcResult result = mvc.perform(get(getUrl()))
                .andExpect(status().isOk()).andReturn();
        final RsGruppe actualTestgruppe = convertMvcResultToObject(result, RsGruppe.class);
        
        TestTransaction.start(); //Start transaksjon pga. lazy fetch i kall fra databasen
        assertResponse(actualTestgruppe);
        TestTransaction.end();
    }
    
    private void assertResponse(RsGruppe actualTestgruppe) {
        Gruppe expectedTestgruppe = gruppeRepository.findById(gruppeId);
        assertEquals(expectedTestgruppe.getNavn(), actualTestgruppe.getNavn());
        assertEquals(expectedTestgruppe.getBeskrivelse(), actualTestgruppe.getBeskrivelse());
        assertEquals(expectedTestgruppe.getId(), actualTestgruppe.getId());
        assertEquals(expectedTestgruppe.getEndretAv(), actualTestgruppe.getEndretAv());
        assertEquals(expectedTestgruppe.getEndretDato(), actualTestgruppe.getEndretDato());
        assertEquals(expectedTestgruppe.getOpprettetAv(), actualTestgruppe.getOpprettetAv());
        assertEquals(expectedTestgruppe.getOpprettetDato(), actualTestgruppe.getOpprettetDato());
        assertEquals(expectedTestgruppe.getTags().get(0).getNavn(), actualTestgruppe.getTags().get(0).getNavn());
        assertEquals(expectedTestgruppe.getPersoner().size(), actualTestgruppe.getPersoner().size());
        for (int i = 0; i < expectedTestgruppe.getPersoner().size(); i++) {
            assertPersoner(expectedTestgruppe.getPersoner().get(i), actualTestgruppe.getPersoner().get(i));
        }
    }
    
    private void assertPersoner(Person person, RsPerson rsPerson) {
        assertEquals(person.getIdent(), rsPerson.getIdent());
        assertEquals(person.getFornavn(), rsPerson.getFornavn());
        assertEquals(person.getEtternavn(), rsPerson.getEtternavn());
        if (!person.getStatsborgerskap().isEmpty()) {
            assertEquals(person.getStatsborgerskap().get(0).getStatsborgerskap(), rsPerson.getStatsborgerskap().get(0).getStatsborgerskap());
            assertEquals(person.getStatsborgerskap().get(0).getStatsborgerskapRegdato(), rsPerson.getStatsborgerskap().get(0).getStatsborgerskapRegdato());
        }
    }
    
    private void setupTestdataInTpsfDatabase() {
        clearAllRepositories();
        List tags = new ArrayList<Tag>();
        final Tag tag = new Tag();
        tag.setNavn("tagnavn");
        tags.add(tag);
        
        final Gruppe gruppe = Gruppe.builder().navn(GRUPPENAVN).tags(tags).build();
        tag.setGrupper(Arrays.asList(gruppe));
        Gruppe lagretGruppe = gruppeRepository.save(gruppe);
        
        constructTestpersonsInTpsfDatabase(lagretGruppe);
        
        gruppeId = lagretGruppe.getId();
    }
}
