package no.nav.tps.forvalteren.domain.rs;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class RsSimpleRelasjoner {

    private RsSimpleDollyRequest partner;

    private List<RsSimpleDollyRequest> barn;
}
