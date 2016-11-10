package no.nav.tps.vedlikehold.domain.service.command.tps.servicerutiner.requests;

import lombok.Getter;
import lombok.Setter;
import no.nav.tps.vedlikehold.domain.service.command.User.User;

@Getter
@Setter
public class TpsRequestContext {

    private User user;
    private String environment;

}
