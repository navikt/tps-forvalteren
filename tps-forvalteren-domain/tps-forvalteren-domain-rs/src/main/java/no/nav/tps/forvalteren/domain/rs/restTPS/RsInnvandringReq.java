package no.nav.tps.forvalteren.domain.rs.restTPS;

import java.util.Set;

import lombok.Getter;
import lombok.Setter;
import no.nav.tps.forvalteren.domain.rs.RsPerson;

@Getter
@Setter
public class RsInnvandringReq {

    Set<String> environments;
    RsPerson person;

}
