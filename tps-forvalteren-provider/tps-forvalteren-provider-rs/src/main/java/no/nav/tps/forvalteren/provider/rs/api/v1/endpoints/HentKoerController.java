package no.nav.tps.forvalteren.provider.rs.api.v1.endpoints;

import static no.nav.tps.forvalteren.provider.rs.config.ProviderConstants.OPERATION;
import static no.nav.tps.forvalteren.provider.rs.config.ProviderConstants.RESTSERVICE;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import no.nav.freg.metrics.annotations.Metrics;
import no.nav.freg.spring.boot.starters.log.exceptions.LogExceptions;
import no.nav.tps.forvalteren.consumer.rs.environments.FasitApiConsumer;
import no.nav.tps.forvalteren.consumer.rs.environments.dao.FasitApplication;
import no.nav.tps.forvalteren.consumer.rs.environments.dao.FasitMQInformation;
import no.nav.tps.forvalteren.consumer.rs.environments.dao.FasitResource;
import no.nav.tps.forvalteren.consumer.rs.environments.dao.FasitUsedResources;
import no.nav.tps.forvalteren.consumer.rs.environments.mapper.MqInfoMapper;
import no.nav.tps.forvalteren.consumer.rs.environments.resourcetypes.FasitChannel;
import no.nav.tps.forvalteren.consumer.rs.environments.resourcetypes.FasitPropertyTypes;
import no.nav.tps.forvalteren.consumer.rs.environments.resourcetypes.FasitQueue;
import no.nav.tps.forvalteren.service.command.exceptions.NotFoundException;
import no.nav.tps.forvalteren.service.command.tps.xmlmelding.GetQueuesFromEnvironment;

@RestController
@RequestMapping(value = "api/v1")
@ConditionalOnProperty(prefix = "tps.forvalteren", name = "production.mode", havingValue = "false")
public class HentKoerController {

    private static final String REST_SERVICE_NAME = "service";

    @Autowired
    private GetQueuesFromEnvironment getQueuesFromEnvironment;

    @Autowired
    private FasitApiConsumer fasitApiConsumer;

    @Autowired
    private MqInfoMapper mqInfoMapper;

    @LogExceptions
    @Metrics(value = "provider", tags = { @Metrics.Tag(key = RESTSERVICE, value = REST_SERVICE_NAME), @Metrics.Tag(key = OPERATION, value = "getQueues") })
    @RequestMapping(value = "/applications", method = RequestMethod.GET)
    public List<FasitApplication> getApplications(@RequestParam(name = "appname", required = false, defaultValue = "") String appNavn) {

        return fasitApiConsumer.getApplications("undefined".equals(appNavn) ? "" : appNavn);
    }

    @LogExceptions
    @Metrics(value = "provider", tags = { @Metrics.Tag(key = RESTSERVICE, value = REST_SERVICE_NAME), @Metrics.Tag(key = OPERATION, value = "getQueues") })
    @RequestMapping(value = "/queues", method = RequestMethod.GET)
    public List<FasitMQInformation> getQueues(@RequestParam("appname") String appNavn) {

        List<FasitApplication> applications = fasitApiConsumer.getApplicationInstances(appNavn, true);

        if (applications.isEmpty()) {
            throw new NotFoundException(String.format("Applikasjon \"%s\" ble ikke funnet i Fasit", appNavn));
        }

        List<FasitUsedResources> usedResource = fasitApiConsumer.getUsedResourcesFromAppByTypes(applications.get(0),
                FasitPropertyTypes.QUEUE
        );

        List<FasitQueue> queues = new ArrayList<>();
        usedResource.forEach(res -> {
            List<FasitResource> resourceElements = fasitApiConsumer.getResourcesByAliasAndType(res.getAlias(), FasitPropertyTypes.getEnumByName(res.getType()));
            queues.addAll(resourceElements.stream()
                    .map(r -> (FasitQueue) r.getProperties())
                    .collect(Collectors.toList())
            );
        });

        List<FasitResource> channelRes = fasitApiConsumer.getResourcesByAliasAndType(appNavn, FasitPropertyTypes.CHANNEL);
        List<FasitChannel> channels = (channelRes.stream()
                .map(res -> (FasitChannel) res.getProperties())
                .collect(Collectors.toList())
        );

        return mqInfoMapper.execute(applications, queues, channels);
    }
}
