package no.nav.tps.forvalteren.provider.rs.api.v1;

import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.util.ReflectionTestUtils;

import no.nav.tps.forvalteren.provider.rs.AbstractRsProviderIntegrationTest;
import no.nav.tps.forvalteren.provider.rs.api.v1.config.TestUserDetails;
import no.nav.tps.forvalteren.provider.rs.api.v1.endpoints.EnvironmentController;

public class EnvironmentControllerIntegrationTest extends AbstractRsProviderIntegrationTest {

    @Autowired
    private EnvironmentController environmentController;

    @Test
    @WithUserDetails(TestUserDetails.USERNAME)
    public void getsEnvironments() throws Exception {

        ReflectionTestUtils.setField(environmentController, "currentEnvironmentIsProd", false);
        mvc.perform(get("/api/v1/environments"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.environments", hasSize(greaterThanOrEqualTo(8))))
                .andExpect(jsonPath("$.productionMode", is(false)));
    }
}