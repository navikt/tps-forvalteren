package no.nav.tps.forvalteren.domain.rs;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Getter
@Setter
@NoArgsConstructor
public class RsPersonIdListe {

    @NotNull
    @Size(min = 1)
    private Long[] ids;

}
