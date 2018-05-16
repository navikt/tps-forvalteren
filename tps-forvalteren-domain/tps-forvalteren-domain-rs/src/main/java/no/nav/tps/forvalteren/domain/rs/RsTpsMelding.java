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
public class RsTpsMelding {

    @NotNull
    private String melding;

    @NotNull
    private String ko;

}
