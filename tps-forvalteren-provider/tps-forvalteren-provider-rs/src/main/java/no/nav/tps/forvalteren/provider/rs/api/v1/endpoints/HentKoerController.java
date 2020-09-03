package no.nav.tps.forvalteren.provider.rs.api.v1.endpoints;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import no.nav.tps.forvalteren.common.java.logging.LogExceptions;
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
    private static final String NOT_FOUND = "Applikasjon \"%s\" ble ikke funnet i Fasit";

    @Autowired
    private GetQueuesFromEnvironment getQueuesFromEnvironment;

    @Autowired
    private FasitApiConsumer fasitApiConsumer;

    @Autowired
    private MqInfoMapper mqInfoMapper;

    @LogExceptions
    @RequestMapping(value = "/applications", method = RequestMethod.GET)
    public List<FasitApplication> getApplications(@RequestParam(name = "appname", required = false, defaultValue = "") String appNavn) {

        List<FasitApplication> applikasjoner = fasitApiConsumer.getApplications("undefined".equals(appNavn) ? "" : appNavn);

        if (applikasjoner.isEmpty()) {
            throw new NotFoundException(String.format(NOT_FOUND, appNavn));
        }

        return applikasjoner;
    }

    @LogExceptions
    @RequestMapping(value = "/queues", method = RequestMethod.GET)
    public List<FasitMQInformation> getQueues(@RequestParam("appname") String appNavn) {

        List<FasitApplication> applications = fasitApiConsumer.getApplicationInstances(appNavn, true);

        if (applications.isEmpty()) {
            throw new NotFoundException(String.format(NOT_FOUND, appNavn));
        }

        List<FasitUsedResources> usedResources = new ArrayList<>();
        for (FasitApplication application : applications) {
            if (application.getApplication().equals(appNavn)) {
                usedResources = fasitApiConsumer.getUsedResourcesFromAppByTypes(application, FasitPropertyTypes.QUEUE);
                break;
            }
        }

        List<FasitQueue> queues = new ArrayList<>();
        usedResources.forEach(res -> {
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
