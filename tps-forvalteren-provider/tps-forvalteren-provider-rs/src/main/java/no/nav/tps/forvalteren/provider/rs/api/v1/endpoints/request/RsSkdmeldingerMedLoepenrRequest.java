package no.nav.tps.forvalteren.provider.rs.api.v1.endpoints.request;

import java.util.List;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import lombok.Getter;
import lombok.Setter;
import no.nav.tps.forvalteren.domain.rs.skd.RsMeldingstype;

@Setter
@Getter
public class RsSkdmeldingerMedLoepenrRequest {

    @Size(min = 1)
    private int antallLoepenumre;
    
    @NotNull
    private List<RsMeldingstype> skdmeldingerMedLoepenummer;
}
