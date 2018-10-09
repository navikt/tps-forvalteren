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
import no.nav.tps.forvalteren.domain.rs.RsTpsMeldingKo;
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
    @RequestMapping(value = "/queues", method = RequestMethod.GET)
    public List<RsTpsMeldingKo> getQueues() {
        return getQueuesFromEnvironment.execute("tpsws");
    }

    @LogExceptions
    @Metrics(value = "provider", tags = { @Metrics.Tag(key = RESTSERVICE, value = REST_SERVICE_NAME), @Metrics.Tag(key = OPERATION, value = "getQueues") })
    @RequestMapping(value = "/fasitresources", method = RequestMethod.GET)
    public List<FasitMQInformation> getAllQueues(@RequestParam("appNavn") String appNavn, @RequestParam(value = "env", required = false) String env) {

        List<FasitApplication> applications = fasitApiConsumer.getApplications(appNavn, true);
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
