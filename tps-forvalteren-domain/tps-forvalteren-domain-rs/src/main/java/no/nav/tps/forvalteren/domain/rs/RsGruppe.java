package no.nav.tps.forvalteren.domain.rs;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.NotBlank;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RsGruppe {

    private Long id;

    @NotBlank
    private String navn;

    @NotBlank
    private String beskrivelse;

    private List<RsPerson> personer;

    private List<RsTag> tags;

}
