package no.nav.tps.vedlikehold.provider.web;

import no.nav.tps.vedlikehold.provider.web.selftest.model.ApplicationProperty;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

import static java.util.Arrays.asList;

/**
 * @author Kristian Kyvik (Visma Consulting).
 */
@Controller
public class SelftestController {

    @Value("${application.name:MISSING_APPLICATION_NAME}")
    private String applicationName;

    @Value("${application.version:MISSING_APPLICATION_VERSION}")
    private String applicationVersion;

    @RequestMapping("/internal/selftest")
    public String selftest(@RequestParam(value = "status", required = false) String status, Model model) {
        model.addAttribute("applicationProperties", collectApplicationProperties());
        return "selftest";

    }

    private List<ApplicationProperty> collectApplicationProperties() {
        System.out.println(applicationName);
        System.out.println(applicationVersion);

        return asList(
                new ApplicationProperty("selftest.application.name", applicationName),
                new ApplicationProperty("selftest.application.version", applicationVersion)
        );
    }

}
