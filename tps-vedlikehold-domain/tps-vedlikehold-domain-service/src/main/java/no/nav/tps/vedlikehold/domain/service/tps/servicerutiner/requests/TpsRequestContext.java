package no.nav.tps.vedlikehold.domain.service.tps.servicerutiner.requests;

import lombok.Getter;
import lombok.Setter;
import no.nav.tps.vedlikehold.domain.service.user.User;

@Getter
@Setter
public class TpsRequestContext {

    private User user;
    private String environment;

}
