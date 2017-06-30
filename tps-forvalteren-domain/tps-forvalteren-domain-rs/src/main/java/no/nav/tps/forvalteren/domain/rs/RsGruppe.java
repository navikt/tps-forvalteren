package no.nav.tps.forvalteren.domain.rs;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.Size;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RsGruppe {

    private Long id;

    @NotBlank
    @Size(min = 1, max = 50)
    private String navn;

    @Size(min = 1, max = 200)
    private String beskrivelse;

    private List<RsPerson> personer;

    private List<RsTag> tags;

}
