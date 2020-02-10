package no.nav.tps.forvalteren.domain.rs;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RsSimpleGruppe {

    private Long id;

    @NotBlank
    @Size(min = 1, max = 50)
    private String navn;

    @Size(min = 1, max = 200)
    private String beskrivelse;

}
