package no.nav.tps.forvalteren.domain.rs;

import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RsSimpleRelasjoner {

    private RsSimplePersonRequest partner;

    private List<RsSimplePersonRequest> barn;

    public List<RsSimplePersonRequest> getBarn() {
        if (barn == null) {
            barn = new ArrayList<>();
        }
        return barn;
    }
}
