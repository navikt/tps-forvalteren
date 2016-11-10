package no.nav.tps.vedlikehold.domain.service.command.tps;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import no.nav.tps.vedlikehold.domain.service.command.tps.servicerutiner.response.ServiceRoutineResponse;

@Getter
@Setter
@NoArgsConstructor
public class Response {

    private String xml;
    private ServiceRoutineResponse routineResponse;

}
