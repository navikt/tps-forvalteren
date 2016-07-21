package no.nav.tps.vedlikehold.provider.rs.api.v1.endpoints;

import no.nav.tps.vedlikehold.service.command.vera.GetEnvironments;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpSession;
import java.util.Set;

/**
 * @author Kristian Kyvik (Visma Consulting AS).
 */
@RestController
@RequestMapping(value = "api/v1")
public class EnvironmentController extends BaseController {

    @Autowired
    public GetEnvironments getEnvironments;

    /**
     * Get an object containing a list of environments
     *
     * @param session current HTTP session
     * @return object containing list of environments
     */

    @RequestMapping(value = "/environments", method = RequestMethod.GET)
    public Set<String> getEnvironments(@ApiIgnore HttpSession session) {
        return getEnvironments.execute("tpsws");
    }
}
