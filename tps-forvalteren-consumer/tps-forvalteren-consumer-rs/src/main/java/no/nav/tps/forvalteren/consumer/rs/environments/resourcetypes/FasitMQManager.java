package no.nav.tps.forvalteren.consumer.rs.environments.resourcetypes;

import lombok.Data;

@Data
public class FasitMQManager implements FasitProperty{

    private String name;
    private String hostname;
    private String port;
}
