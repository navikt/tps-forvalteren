package no.nav.tps.forvalteren.provider.rs.api.v1;

import no.nav.tps.forvalteren.provider.rs.AbstractRsProviderIntegrationTest;
import org.junit.Test;
import org.springframework.http.MediaType;

import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class EnvironmentControllerIntegrationTest extends AbstractRsProviderIntegrationTest {

    @Test
    public void getsEnvironments() throws Exception {

        mvc.perform(get("/api/v1/environments"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$", hasSize(15)))
                .andExpect(jsonPath("$", containsInAnyOrder(
                        "t4","u5","t5","u6","t6","t8","t9","t10","t12","t11","t13","t0","t1","t2","t3"
                )));
    }

}