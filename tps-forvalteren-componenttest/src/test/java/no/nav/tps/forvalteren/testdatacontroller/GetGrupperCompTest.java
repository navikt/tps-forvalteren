package no.nav.tps.forvalteren.testdatacontroller;

import static org.junit.Assert.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.List;
import org.junit.Test;
import org.springframework.test.web.servlet.MvcResult;

import no.nav.tps.forvalteren.domain.jpa.Gruppe;
import no.nav.tps.forvalteren.domain.rs.RsSimpleGruppe;

public class GetGrupperCompTest extends AbstractTestdataControllerComponentTest {
    
    @Override
    protected String getServiceUrl() {
        return "/grupper";
    }
    
    @Test
    public void shouldGetListOfGroups() throws Exception {
        List<Gruppe> testgrupper = setupTestdataInTpsfDatabase();
        
        MvcResult result = mvc.perform(get(getUrl()))
                .andExpect(status().isOk()).andReturn();
        final List<RsSimpleGruppe> actualGruppeliste = convertMvcResultToList(result, RsSimpleGruppe.class);
        
        for (int i = 0; i < testgrupper.size(); i++) {
            assertGruppe(testgrupper.get(i), actualGruppeliste.get(i));
        }
    }
    
    private void assertGruppe(Gruppe testgruppe, RsSimpleGruppe actualResponseGruppe) {
        assertEquals(testgruppe.getNavn(), actualResponseGruppe.getNavn());
        assertEquals(testgruppe.getId(), actualResponseGruppe.getId());
        assertEquals(testgruppe.getBeskrivelse(), actualResponseGruppe.getBeskrivelse());
    }
    
    private List<Gruppe> setupTestdataInTpsfDatabase() {
        List<Gruppe> grupper = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            grupper.add(gruppeRepository.save(Gruppe.builder().navn(GRUPPENAVN + i).build()));
        }
        return grupper;
    }
}
