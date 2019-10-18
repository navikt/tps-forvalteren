package no.nav.tps.forvalteren.domain.rs;

import java.time.LocalDateTime;
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
public class RsSivilstand {

    @NotNull
    private Long id;

    private String sivilstand;

    private LocalDateTime sivilstandRegdato;

    private RsSimplePerson person;

    private RsSimplePerson personRelasjonMed;
}