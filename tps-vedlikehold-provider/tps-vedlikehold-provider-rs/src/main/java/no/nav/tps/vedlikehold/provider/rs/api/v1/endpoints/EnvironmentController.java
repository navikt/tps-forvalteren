package no.nav.tps.vedlikehold.provider.rs.api.v1.endpoints;

import no.nav.tps.vedlikehold.service.command.vera.GetEnvironments;
import org.springframework.beans.factory.annotation.Autowired;

import static java.util.stream.Collectors.toSet;
import static org.springframework.util.StringUtils.isEmpty;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpSession;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * @author Kristian Kyvik (Visma Consulting AS).
 */
@RestController
@RequestMapping(value = "api/v1")
public class EnvironmentController {

    private static final Set<String> supportedEnvironments = new HashSet<>(Arrays.asList("u", "t"));

    @Autowired
    public GetEnvironments getEnvironments;

    /**
     * Get an object containing a list of environments
     *
     * @return object containing list of environments
     */

    @RequestMapping(value = "/environments", method = RequestMethod.GET)
    public Set<String> getEnvironments() {
        return getEnvironments.execute("tpsws").stream()
                .filter(environment -> {
                    String prefix = environment.substring(0, 1).toLowerCase();

                    return !isEmpty(environment) && supportedEnvironments.contains(prefix);
                })
                .collect(toSet());
    }
}
