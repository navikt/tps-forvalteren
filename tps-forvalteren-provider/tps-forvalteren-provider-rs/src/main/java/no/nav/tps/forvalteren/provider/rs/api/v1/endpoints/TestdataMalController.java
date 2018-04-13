package no.nav.tps.forvalteren.provider.rs.api.v1.endpoints;

import java.util.List;

import no.nav.freg.metrics.annotations.Metrics;
import no.nav.freg.spring.boot.starters.log.exceptions.LogExceptions;
import static no.nav.tps.forvalteren.provider.rs.config.ProviderConstants.OPERATION;
import static no.nav.tps.forvalteren.provider.rs.config.ProviderConstants.RESTSERVICE;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value= "api/v1/testdatamal")
@ConditionalOnProperty(prefix= "tps.forvalteren", name= "production-mode", havingValue = "false")
public class TestdataMalController {

    private static final String REST_SERVICE_NAME = "testdata_mal";

    @LogExceptions
    @Metrics(value = "provider", tags = { @Metrics.Tag(key = RESTSERVICE, value = REST_SERVICE_NAME), @Metrics.Tag(key = OPERATION, value = "createNewTestdataMal") })
    @RequestMapping(value = "createmal", method = RequestMethod.POST)
    public void createTestdataMal(){

    }

    @LogExceptions
    @Metrics(value = "provider", tags = { @Metrics.Tag(key = RESTSERVICE, value = REST_SERVICE_NAME), @Metrics.Tag(key = OPERATION, value = "updateTestdataMal") })
    @RequestMapping(value = "updatemal", method = RequestMethod.POST)
    public void updateTestdataMal(){

    }

    @LogExceptions
    @Metrics(value = "provider", tags = { @Metrics.Tag(key = RESTSERVICE, value = REST_SERVICE_NAME), @Metrics.Tag(key = OPERATION, value = "deleteTestdataMal") })
    @RequestMapping(value = "deletemal", method = RequestMethod.POST)
    public void deleteTestdataMal(){

    }

    @LogExceptions
    @Metrics(value = "provider", tags = { @Metrics.Tag(key = RESTSERVICE, value = REST_SERVICE_NAME), @Metrics.Tag(key = OPERATION, value = "getTestdataMal") })
    @RequestMapping(value = "getmal", method = RequestMethod.POST)
    public void getTestdataMal(){

    }


    @LogExceptions
    @Metrics(value = "provider", tags = { @Metrics.Tag(key = RESTSERVICE, value = REST_SERVICE_NAME), @Metrics.Tag(key = OPERATION, value = "getAllTestdataMal") })
    @RequestMapping(value = "getallmal", method = RequestMethod.POST)
    public List getAllTestdataMal(){

        return null;
    }

}
