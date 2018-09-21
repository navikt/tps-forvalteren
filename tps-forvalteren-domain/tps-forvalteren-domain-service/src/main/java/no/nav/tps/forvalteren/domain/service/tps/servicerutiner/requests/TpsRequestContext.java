package no.nav.tps.forvalteren.domain.service.tps.servicerutiner.requests;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import no.nav.tps.forvalteren.domain.service.user.User;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TpsRequestContext {

    private User user;
    private String environment;
}
