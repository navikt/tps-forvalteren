package no.nav.tps.forvalteren.domain.rs;

import java.time.LocalDateTime;
import java.util.List;
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
public class RsGruppe {

    private Long id;

    @NotBlank
    @Size(min = 1, max = 30)
    private String navn;

    @Size(min = 1, max = 200)
    private String beskrivelse;

    private List<RsPerson> personer;

    private List<RsTag> tags;

    private LocalDateTime opprettetDato;

    private String opprettetAv;

    private LocalDateTime endretDato;

    private String endretAv;

}
