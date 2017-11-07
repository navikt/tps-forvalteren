package no.nav.tps.forvalteren.domain.rs.skd;

import java.time.LocalDateTime;
import javax.validation.constraints.Size;
import org.hibernate.validator.constraints.NotBlank;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RsSkdEndringsmeldingGruppe {

    private Long id;

    @NotBlank
    @Size(min = 1, max = 50)
    private String navn;

    @Size(min = 1, max = 200)
    private String beskrivelse;

    private LocalDateTime opprettetDato;

    private String opprettetAv;

    private LocalDateTime endretDato;

    private String endretAv;

}
