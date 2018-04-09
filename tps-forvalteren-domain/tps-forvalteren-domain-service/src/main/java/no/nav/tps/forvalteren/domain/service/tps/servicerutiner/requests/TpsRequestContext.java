package no.nav.tps.forvalteren.domain.service.tps.servicerutiner.requests;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import no.nav.tps.forvalteren.domain.service.user.User;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TpsRequestContext {

    private User user;
    private String environment;
}
