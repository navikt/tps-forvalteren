package no.nav.tps.vedlikehold.provider.rs.api.v1;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpSession;
import java.util.List;

/**
 * @author Kristian Kyvik (Visma Consulting AS).
 */
@RestController
@RequestMapping(value = "api/v1")
public class EnvironmentController {
    /**
     * Get an object containing a list of environments
     *
     * @param session current HTTP session
     * @return object containing list of environments
     */

    @RequestMapping(value = "/user", method = RequestMethod.GET)
    public List<String> getEnvironments(@ApiIgnore HttpSession session) {
    }
}
