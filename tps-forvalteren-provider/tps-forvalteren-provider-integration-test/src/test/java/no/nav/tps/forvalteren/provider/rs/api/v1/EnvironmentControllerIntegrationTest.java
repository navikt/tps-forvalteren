package no.nav.tps.forvalteren.provider.rs.api.v1;

import no.nav.tps.forvalteren.provider.rs.AbstractRsProviderIntegrationTest;
import no.nav.tps.forvalteren.provider.rs.api.v1.config.TestUserDetails;

import org.junit.Test;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;

import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class EnvironmentControllerIntegrationTest extends AbstractRsProviderIntegrationTest {

    @Test
    public void test() {

    }

    @Test
    @WithUserDetails(TestUserDetails.USERNAME)
    public void getsEnvironments() throws Exception {

        mvc.perform(get("/api/v1/environments"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.environments", hasSize(16)))
                .andExpect(jsonPath("$.environments", containsInAnyOrder(
                        "u5","u6", "t0","t1", "t2","t3","t4","t5", "t6", "t7","t8","t9","t10","t11","t12", "t13"
                )))
                .andExpect(jsonPath("$.productionMode", is(false)));
    }

}