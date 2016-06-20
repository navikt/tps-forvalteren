package no.nav.tps.vedlikehold.provider.web.api.v1;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Kenneth Gunnerud (Visma Consulting AS).
 */
@RestController
public class EnvironmentController {

    @RequestMapping(value = "/api/v1/test")
    public String test() {
        return "test";
    }
}
