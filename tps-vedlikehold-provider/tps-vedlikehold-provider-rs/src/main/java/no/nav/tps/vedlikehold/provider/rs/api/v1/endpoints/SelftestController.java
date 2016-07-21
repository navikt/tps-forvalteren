package no.nav.tps.vedlikehold.provider.rs.api.v1.endpoints;

import com.fasterxml.jackson.databind.ObjectMapper;
import no.nav.tps.vedlikehold.common.java.message.MessageProvider;
import no.nav.tps.vedlikehold.provider.rs.api.v1.exceptions.SelftestFailureException;
import no.nav.tps.vedlikehold.provider.rs.api.v1.selftest.Selftest;
import no.nav.tps.vedlikehold.provider.rs.api.v1.selftest.models.AggregateSelftestResult;
import no.nav.tps.vedlikehold.provider.rs.api.v1.selftest.models.ApplicationProperty;
import no.nav.tps.vedlikehold.provider.rs.api.v1.selftest.models.JsonSelftest;
import no.nav.tps.vedlikehold.provider.rs.api.v1.selftest.models.SelftestResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import static java.util.Arrays.asList;
import static no.nav.tps.vedlikehold.common.java.message.MessageConstants.SELFTEST_EXCEPTION_MESSAGE_KEY;
import static no.nav.tps.vedlikehold.provider.rs.api.v1.selftest.models.SelftestResult.Status.FEILET;
import static org.springframework.util.StringUtils.collectionToCommaDelimitedString;

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

    @Autowired
    @Qualifier(value = "diskresjonskodeSelftest")
    private Selftest diskresjonskodeSelftest;

    @Autowired
    @Qualifier(value = "egenAnsattSelftest")
    private Selftest egenAnsattSelftest;

    @Autowired
    @Qualifier(value = "veraSelftest")
    private Selftest veraSelftest;

    @Autowired
    @Qualifier(value = "fasitSelftest")
    private Selftest fasitSelftest;

    @Autowired
    @Qualifier(value = "mqSelftest")
    private Selftest mqSelftest;

    @Autowired
    @Qualifier(value = "tpsSelftest")
    private Selftest tpsSelftest;

    @Autowired
    private MessageProvider messageProvider;


    /**
     * Get the results from selftest as a JSON object
     */
    @RequestMapping(value = "/internal/selftest", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> jsonSelftest(@RequestParam(value = "status", required = false) String status) throws Exception {

        List<SelftestResult> selftestResults = collectSelftestResults();
        JsonSelftest jsonSelftest            = new JsonSelftest();
        HttpStatus responseStatus            = HttpStatus.OK;

        if (status != null && containsFailures(selftestResults)) {
            String message = messageProvider.get(SELFTEST_EXCEPTION_MESSAGE_KEY, mergeFailedSubSystemNames(selftestResults));
            throw new SelftestFailureException(message);
        }

        ObjectMapper mapper = new ObjectMapper();

        jsonSelftest.setApplicationName(descriptiveApplicationName);
        jsonSelftest.setApplicationVersion(applicationVersion);
        jsonSelftest.setTimestamp(new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new Date()));
        jsonSelftest.setAggregateStatus(aggregateSelftestResults(selftestResults));
        jsonSelftest.setChecks(selftestResults);

        String jsonResponse = mapper.writeValueAsString(jsonSelftest);

        if (FEILET.equals(jsonSelftest.getAggregateStatus())) {
            responseStatus = HttpStatus.INTERNAL_SERVER_ERROR;
        }

        return new ResponseEntity<>(jsonResponse, responseStatus);
    }

    /**
     *  Get the selftest HTML page
     */
    @RequestMapping(value = "/internal/selftest", produces = MediaType.TEXT_HTML_VALUE)
    public String selftest(@RequestParam(value = "status", required = false) String status, Model model) {

        List<SelftestResult> selftestResults = collectSelftestResults();
        System.out.println(selftestResults);

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
                egenAnsattSelftest.perform(),
                veraSelftest.perform(),
                fasitSelftest.perform(),
                mqSelftest.perform(),
                tpsSelftest.perform()
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
