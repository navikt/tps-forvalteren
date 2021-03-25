package no.nav.tps.forvalteren.domain.rs.skd;

import java.util.List;
import javax.validation.constraints.NotEmpty;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RsSkdEndringsmeldingIdListToTps {

    @NotEmpty
    private String environment;

    private List<Long> ids;
}
