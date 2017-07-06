package no.nav.tps.forvalteren.domain.rs;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RsSimplePerson {

    @NotNull
    private Long id;

}
