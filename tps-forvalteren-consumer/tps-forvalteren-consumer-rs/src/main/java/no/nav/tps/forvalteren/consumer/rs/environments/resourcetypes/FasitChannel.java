package no.nav.tps.forvalteren.consumer.rs.environments.resourcetypes;

import lombok.Data;

@Data
public class FasitChannel implements FasitProperty{

    private String queueManager;
    private String name;
}
