package no.nav.tps.forvalteren.consumer.rs.environments.mapper;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import no.nav.tps.forvalteren.consumer.rs.environments.FasitApiConsumer;
import no.nav.tps.forvalteren.consumer.rs.environments.dao.FasitApplication;
import no.nav.tps.forvalteren.consumer.rs.environments.dao.FasitMQInformation;
import no.nav.tps.forvalteren.consumer.rs.environments.resourcetypes.FasitChannel;
import no.nav.tps.forvalteren.consumer.rs.environments.resourcetypes.FasitMQManager;
import no.nav.tps.forvalteren.consumer.rs.environments.resourcetypes.FasitPropertyTypes;
import no.nav.tps.forvalteren.consumer.rs.environments.resourcetypes.FasitQueue;

@Service
public class MqInfoMapper {

    @Autowired
    private FasitApiConsumer fasitApiConsumer;

    public List<FasitMQInformation> execute(
            List<FasitApplication> apps,
            List<FasitQueue> queues,
            List<FasitChannel> channels) {

        List<FasitMQInformation> liste = new ArrayList<>();

        apps.forEach(app -> {
            FasitMQInformation fasitMQInformation = new FasitMQInformation();

            fasitMQInformation.setEnvironment(app.getEnvironment());
            fasitMQInformation.setQueues(getQueuesForApp(app, queues));
            fasitMQInformation.setChannels(getChannelsForApp(app, channels));
            fasitMQInformation.setMqManager(getMQManagerForApp(app));
            fasitMQInformation.setAppnavn(app.getApplication());

            liste.add(fasitMQInformation);
        });

        return liste;
    }

    private List<FasitQueue> getQueuesForApp(FasitApplication app, List<FasitQueue> koer) {
        Pattern pattern = Pattern.compile("^[A-Z]+\\.D[0-9]+_.*$");
        return koer.stream()
                .filter(q -> q.getQueueName().contains(app.getEnvironment().toUpperCase() + "_") ||
                        ('U' == app.getEnvironment().toUpperCase().charAt(0) &&
                                pattern.matcher(q.getQueueName().toUpperCase()).find() &&
                                !q.getQueueName().toUpperCase().contains("REPLY")))
                .collect(Collectors.toList());
    }

    private FasitMQManager getMQManagerForApp(FasitApplication app) {
        final FasitMQManager[] mqManager = new FasitMQManager[1];

        app.getUsedresources().stream()
                .filter(ures -> FasitPropertyTypes.QUEUE_MANAGER.getPropertyName().equals(ures.getType()))
                .findAny()
                .ifPresent(ures -> mqManager[0] = (FasitMQManager)fasitApiConsumer.getResourceFromRef(ures.getRef()).getProperties());

        return mqManager[0];
    }

    private List<FasitChannel> getChannelsForApp(FasitApplication app, List<FasitChannel> channels) {
        String regexPattern = app.getEnvironment().substring(0,1).toUpperCase() + "\\d{1,2}_";
        Pattern pattern = Pattern.compile(regexPattern);
        return channels.stream()
                .filter(q -> pattern.matcher(q.getName()).find())
                .collect(Collectors.toList());
    }
}
