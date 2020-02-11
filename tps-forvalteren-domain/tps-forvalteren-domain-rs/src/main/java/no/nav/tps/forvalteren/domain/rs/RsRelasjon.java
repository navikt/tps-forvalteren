package no.nav.tps.forvalteren.domain.rs;

import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RsRelasjon {

    @NotNull
    private Long id;

    @NotNull
    private RsSimplePerson person;

    @NotNull
    private RsPersonUtenRelasjon personRelasjonMed;

    @NotNull
    private String relasjonTypeNavn;
}