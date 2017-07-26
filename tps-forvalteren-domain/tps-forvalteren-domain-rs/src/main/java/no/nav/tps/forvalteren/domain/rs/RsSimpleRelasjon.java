package no.nav.tps.forvalteren.domain.rs;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import no.nav.tps.forvalteren.domain.jpa.Person;

import javax.validation.constraints.NotNull;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RsSimpleRelasjon {

    @NotNull
    private Long id;

    @NotNull
    private RsPerson person;

    @NotNull
    private RsPerson personRelasjonMed;

    @NotNull
    private int relasjonTypeKode;

}
