package no.nav.tps.forvalteren.domain.rs;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class RsPersonnavn {

    private String ident;
    private String fornavn;
    private String mellomnavn;
    private String etternavn;
}
