package no.nav.tps.forvalteren.domain.rs;

import java.time.LocalDateTime;
import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RsIdenthistorikk {

    @NotNull
    private Long id;

    @NotNull
    private RsSimplePerson person;

    @NotNull
    private RsPersonUtenIdenthistorikk aliasPerson;

    @NotNull
    private Integer historicIdentOrder;

    @NotNull
    private LocalDateTime regdato;
}