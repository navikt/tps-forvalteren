package no.nav.tps.vedlikehold.provider.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import no.nav.tps.vedlikehold.common.java.message.MessageProvider;
import no.nav.tps.vedlikehold.provider.web.model.SelftestResult;
import no.nav.tps.vedlikehold.provider.web.selftest.Selftest;
import no.nav.tps.vedlikehold.provider.web.exception.SelftestFailureException;
import no.nav.tps.vedlikehold.provider.web.model.AggregateSelftestResult;
import no.nav.tps.vedlikehold.provider.web.model.ApplicationProperty;

import static java.util.Arrays.asList;
import static org.springframework.util.StringUtils.collectionToCommaDelimitedString;
import static no.nav.tps.vedlikehold.common.java.message.MessageConstants.SELFTEST_EXCEPTION_MESSAGE_KEY;
import static no.nav.tps.vedlikehold.provider.web.model.SelftestResult.Status.FEILET;

/**
 * @author Kristian Kyvik (Visma Consulting).
 */
@Controller
public class SelfTestController {

    @Value("${application.descriptiveName:MISSING_DESCRIPTIVE_APPLICATION_NAME}")
    private String descriptiveApplicationName;

    @Value("${application.name:MISSING_APPLICATION_NAME}")
    private String applicationName;

    @Value("${application.version:MISSING_APPLICATION_VERSION}")
    private String applicationVersion;

    @Value("${bootstrap.version:MISSING_BOOTSTRAP_VERSION}")
    private String bootstrapVersion;

    @Autowired
    @Qualifier(value = "diskresjonskodeSelftest")
    private Selftest diskresjonskodeSelftest;

    @Autowired
    @Qualifier(value = "egenAnsattSelftest")
    private Selftest egenAnsattSelftest;

    @Autowired
    private MessageProvider messageProvider;

    @RequestMapping("/internal/selftest")
    public String selftest(@RequestParam(value = "status", required = false) String status, Model model) {
        List<SelftestResult> selftestResults = collectSelftestResults();

        if (status != null && containsFailures(selftestResults)) {
            String message = messageProvider.get(SELFTEST_EXCEPTION_MESSAGE_KEY, mergeFailedSubSystemNames(selftestResults));
            throw new SelftestFailureException(message);
        } else {
            model.addAttribute("applicationProperties", collectApplicationProperties());
            model.addAttribute("applicationName", descriptiveApplicationName);
            model.addAttribute("bootstrapVersion", bootstrapVersion);
            model.addAttribute("aggregateStatus", aggregateSelftestResults(selftestResults));
            model.addAttribute("selftestResults", selftestResults);
            return "selftest";
        }
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

    private SelftestResult.Status aggregateSelftestResults(List<SelftestResult> results) {
        AggregateSelftestResult aggregateSelftestResult = new AggregateSelftestResult();
        aggregateSelftestResult.addResults(results);
        return aggregateSelftestResult.getStatus();
    }

    private List<SelftestResult> collectSelftestResults() {
        return Collections.unmodifiableList(asList(
                diskresjonskodeSelftest.perform(),
                egenAnsattSelftest.perform()
        ));
    }

    private boolean containsFailures(List<SelftestResult> results) {
        for (SelftestResult result : results) {
            if (result.getStatus() == FEILET) {
                return true;
            }
        }

        return false;
    }

    private String mergeFailedSubSystemNames(List<SelftestResult> results) {
        List<String> failedSubSystemNames = new ArrayList<>();

        for (SelftestResult result : results) {
            if (result.getStatus() == FEILET) {
                failedSubSystemNames.add(result.getName());
            }
        }

        return collectionToCommaDelimitedString(failedSubSystemNames);
    }

}
