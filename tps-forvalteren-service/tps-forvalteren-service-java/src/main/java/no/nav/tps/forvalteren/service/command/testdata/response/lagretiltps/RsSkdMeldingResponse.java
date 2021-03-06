package no.nav.tps.forvalteren.service.command.testdata.response.lagretiltps;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RsSkdMeldingResponse {
    private Long gruppeid;

    List<SendSkdMeldingTilTpsResponse> sendSkdMeldingTilTpsResponsene;
    List<ServiceRoutineResponseStatus> serviceRoutineStatusResponsene;
}