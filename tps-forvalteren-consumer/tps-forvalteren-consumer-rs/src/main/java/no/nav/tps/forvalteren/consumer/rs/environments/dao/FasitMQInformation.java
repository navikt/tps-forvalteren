package no.nav.tps.forvalteren.consumer.rs.environments.dao;

import lombok.Getter;
import lombok.Setter;
import no.nav.tps.forvalteren.consumer.rs.environments.resourcetypes.FasitChannel;
import no.nav.tps.forvalteren.consumer.rs.environments.resourcetypes.FasitMQManager;
import no.nav.tps.forvalteren.consumer.rs.environments.resourcetypes.FasitQueue;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class FasitMQInformation {

    String appnavn;
    String environment;
    FasitMQManager mqManager;
    List<FasitQueue> queues = new ArrayList<>();
    List<FasitChannel> channels = new ArrayList<>();
}
