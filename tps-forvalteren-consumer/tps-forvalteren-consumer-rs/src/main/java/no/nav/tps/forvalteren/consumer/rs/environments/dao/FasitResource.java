package no.nav.tps.forvalteren.consumer.rs.environments.dao;

import lombok.Data;
import no.nav.tps.forvalteren.consumer.rs.environments.resourcetypes.FasitProperty;

@Data
public class FasitResource {

    private String id;
    private String type;
    private String alias;
    private String created;
    private String updated;
    private boolean dodgy;
    private FasitResourceScope scope;
    private FasitProperty properties;

}
