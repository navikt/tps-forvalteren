package no.nav.tps.forvalteren.consumer.rs.environments.resourcetypes;

import lombok.Data;

@Data
public class FasitChannel extends FasitProperty{

    private String queueManager;
    private String name;
}
