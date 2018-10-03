package no.nav.tps.forvalteren.provider.rs.api.v1.endpoints.request;

import java.util.List;
import javax.validation.Valid;

import lombok.Getter;
import lombok.Setter;
import no.nav.tps.forvalteren.domain.rs.skd.RsMeldingstype;

@Setter
@Getter
public class RsSkdmeldingerMedLoepenrRequest {

    private int antallLoepenumre;
    
    @Valid
    private List<RsMeldingstype> skdmeldingerMedLoepenummer;
}
