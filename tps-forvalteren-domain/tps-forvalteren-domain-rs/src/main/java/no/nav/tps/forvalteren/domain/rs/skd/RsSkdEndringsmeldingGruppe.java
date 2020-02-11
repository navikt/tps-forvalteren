package no.nav.tps.forvalteren.domain.rs.skd;

import java.time.LocalDateTime;
import java.util.List;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RsSkdEndringsmeldingGruppe {

    private Long id;

    @NotBlank
    @Size(min = 1, max = 50)
    private String navn;

    @Size(min = 1, max = 200)
    private String beskrivelse;

    private List<RsMeldingstype> meldinger;

    private LocalDateTime opprettetDato;

    private String opprettetAv;

    private LocalDateTime endretDato;

    private String endretAv;

}
