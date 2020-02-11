package no.nav.tps.forvalteren.domain.rs.restTPS;

import lombok.Getter;
import lombok.Setter;
import no.nav.tps.forvalteren.domain.rs.RsPerson;

import java.util.List;
import java.util.Set;

@Getter
@Setter
public class RsInnvandringReq {

    Set<String> environments;
    RsPerson person;

}
