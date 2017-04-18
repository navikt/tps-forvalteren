package no.nav.tps.vedlikehold.provider.rs.api.v1;

import no.nav.tps.vedlikehold.provider.rs.AbstractRsProviderIntegrationTest;
import no.nav.tps.vedlikehold.provider.rs.api.v1.config.TestUserDetails;
import org.junit.Test;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class UserControllerIntegrationTest extends AbstractRsProviderIntegrationTest {

    private static String BASE_URL = "/api/v1";

    @Test
    @WithUserDetails(TestUserDetails.USERNAME)
    public void getUserReturnsJsonUser() throws Exception {
        mvc.perform(get(BASE_URL+"/user"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.name", is(equalTo(TestUserDetails.DISPLAY_NAME))))
                .andExpect(jsonPath("$.username", is(equalTo(TestUserDetails.USERNAME))));
    }

    @Test
    @WithUserDetails(TestUserDetails.USERNAME)
    public void logoutReturnsOk() throws Exception {
        mvc.perform(post(BASE_URL+"/user/logout"))
                .andExpect(status().isOk());
    }
}
