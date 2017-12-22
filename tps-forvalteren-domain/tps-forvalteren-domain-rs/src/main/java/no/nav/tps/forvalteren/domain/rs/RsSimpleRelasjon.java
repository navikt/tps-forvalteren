package no.nav.tps.forvalteren.domain.rs;

import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RsSimpleRelasjon {

    @NotNull
    private Long id;

    @NotNull
    private RsSimplePerson person;

    @NotNull
    private RsSimplePerson personRelasjonMed;

    @NotNull
    private String relasjonTypeNavn;

}
