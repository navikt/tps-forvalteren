package no.nav.tps.forvalteren.domain.service.tps.servicerutiner.requests;

import lombok.Getter;
import lombok.Setter;
import no.nav.tps.forvalteren.domain.service.user.User;

@Getter
@Setter
public class TpsRequestContext {

    private User user;
    private String environment;
}
