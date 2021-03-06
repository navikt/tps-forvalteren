package no.nav.tps.forvalteren.provider.rs.api.v1;

import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Test;
import org.springframework.security.test.context.support.WithUserDetails;

import no.nav.tps.forvalteren.provider.rs.AbstractRsProviderIntegrationTest;
import no.nav.tps.forvalteren.provider.rs.api.v1.config.TestUserDetails;

public class TpsServicesControllerIntegrationTest extends AbstractRsProviderIntegrationTest {

    @Test
    public void checkContextOk() {

    }

    private static final String URL = "/api/v1/tpsservices";

    @Test
    @WithUserDetails(TestUserDetails.USERNAME)
    public void getAllServiceRoutines() throws Exception {

        mvc.perform(get(URL))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(26)));
    }
}
