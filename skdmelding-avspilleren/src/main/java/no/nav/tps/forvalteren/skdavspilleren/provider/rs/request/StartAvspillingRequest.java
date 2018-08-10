package no.nav.tps.forvalteren.skdavspilleren.provider.rs.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class StartAvspillingRequest {
    
    private Long gruppeId;
    private String miljoe;
}
