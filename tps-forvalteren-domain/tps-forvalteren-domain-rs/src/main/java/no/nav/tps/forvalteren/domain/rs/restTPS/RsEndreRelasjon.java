package no.nav.tps.forvalteren.domain.rs.restTPS;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RsEndreRelasjon {

        private String offentligIdent;
        private String typeRelasjon;
        private String relasjonsFnr1;
        private String relasjonsFnr2;
        private String fomRelasjon;
        private String tomRelasjon;
        private String environment;
}
