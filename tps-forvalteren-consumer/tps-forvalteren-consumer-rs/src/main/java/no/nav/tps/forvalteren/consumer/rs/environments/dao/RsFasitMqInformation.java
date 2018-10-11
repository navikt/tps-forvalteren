package no.nav.tps.forvalteren.consumer.rs.environments.dao;

import lombok.Data;
import no.nav.tps.forvalteren.consumer.rs.environments.resourcetypes.FasitChannel;
import no.nav.tps.forvalteren.consumer.rs.environments.resourcetypes.FasitMQManager;
import no.nav.tps.forvalteren.consumer.rs.environments.resourcetypes.FasitQueue;

@Data
public class RsFasitMqInformation {

    String appnavn;
    String environment;
    FasitMQManager mqManager;
    FasitQueue fasitQueue;
    FasitChannel fasitChannel;
}
