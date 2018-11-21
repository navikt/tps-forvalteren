package no.nav.tps.forvalteren.domain.rs;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RsSimpleRelasjoner {

    private RsSimplePersonRequest partner;

    private List<RsSimplePersonRequest> barn;
}
