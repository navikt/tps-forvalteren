package no.nav.tps.forvalteren.provider.rs.api.v1.endpoints.dolly;

import java.util.List;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PersonIdentListe {
    @NotEmpty
    @Size(min = 1)
    private List<String> identer;
}
