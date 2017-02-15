package no.nav.tps.vedlikehold.domain.service.tps.servicerutiner.requests;

import lombok.Getter;
import lombok.Setter;
import no.nav.tps.vedlikehold.domain.service.User.User;

@Getter
@Setter
public class TpsRequestContext {

    private User user;
    private String environment;

}
