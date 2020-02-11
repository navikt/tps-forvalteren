package no.nav.tps.forvalteren.service.command.testdata.response;

import static java.util.Objects.isNull;

import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CheckIdentResponse {

    private List<IdentStatusExtended> statuser;

    public List<IdentStatusExtended> getStatuser() {
        if (isNull(statuser)) {
            statuser = new ArrayList();
        }
        return statuser;
    }
}
