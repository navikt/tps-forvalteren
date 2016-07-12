package no.nav.tps.vedlikehold.provider.web;

import no.nav.tps.vedlikehold.provider.web.model.ApplicationProperty;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.List;

import static java.util.Arrays.asList;

/**
 * @author Kristian Kyvik (Visma Consulting).
 */
@Controller
public class SelftestController {

    @Value("${application.descriptiveName:MISSING_DESCRIPTIVE_APPLICATION_NAME}")
    private String descriptiveApplicationName;

    @Value("${application.name:MISSING_APPLICATION_NAME}")
    private String applicationName;

    @Value("${application.version:MISSING_APPLICATION_VERSION}")
    private String applicationVersion;

    @Value("${bootstrap.version:MISSING_BOOTSTRAP_VERSION}")
    private String bootstrapVersion;

    @RequestMapping("/internal/selftest")
    public String selftest(@RequestParam(value = "status", required = false) String status, Model model) {
        model.addAttribute("applicationProperties", collectApplicationProperties());
        model.addAttribute("applicationName", descriptiveApplicationName);
        model.addAttribute("bootstrapVersion", bootstrapVersion);
        return "selftest";

    }

    private List<ApplicationProperty> collectApplicationProperties() {

        return asList(
                new ApplicationProperty("selftest.application.name", applicationName),
                new ApplicationProperty("selftest.application.version", applicationVersion),
                new ApplicationProperty("selftest.application.server", getServerAddress())
        );
    }

    private String getServerAddress() {
        try {
            return InetAddress.getLocalHost().getCanonicalHostName();
        } catch (UnknownHostException e) {
            return "N/A";
        }
    }

}
