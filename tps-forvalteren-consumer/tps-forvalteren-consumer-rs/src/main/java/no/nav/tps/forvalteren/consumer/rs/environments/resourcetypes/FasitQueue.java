package no.nav.tps.forvalteren.consumer.rs.environments.resourcetypes;

import lombok.Data;

@Data
public class FasitQueue implements FasitProperty{

    private String queueName;
    private String queueManager;

}
