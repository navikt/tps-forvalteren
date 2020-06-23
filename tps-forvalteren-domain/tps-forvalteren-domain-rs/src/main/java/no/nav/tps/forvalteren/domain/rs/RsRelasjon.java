package no.nav.tps.forvalteren.domain.rs;

import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonInclude;

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
@JsonInclude(JsonInclude.Include.NON_EMPTY)
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