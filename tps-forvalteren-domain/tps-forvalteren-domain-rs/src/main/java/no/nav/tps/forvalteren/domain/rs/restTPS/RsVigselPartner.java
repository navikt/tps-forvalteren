package no.nav.tps.forvalteren.domain.rs.restTPS;

import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
public class RsVigselPartner {

    String personIdent;
    String giftetMedIdent;

    Set<String> environments;
}
