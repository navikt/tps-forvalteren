package no.nav.tps.forvalteren.domain.rs;

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
public class RsPersonIdListe {

    @NotEmpty
    @Size(min = 1)
    private List<Long> ids;

}
