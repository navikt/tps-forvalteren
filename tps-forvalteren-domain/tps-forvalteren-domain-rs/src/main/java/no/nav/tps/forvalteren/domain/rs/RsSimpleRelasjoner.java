package no.nav.tps.forvalteren.domain.rs;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
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
