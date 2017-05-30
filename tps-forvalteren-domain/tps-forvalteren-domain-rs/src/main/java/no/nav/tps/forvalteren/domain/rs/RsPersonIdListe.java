package no.nav.tps.forvalteren.domain.rs;

import lombok.Getter;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Getter
public class RsPersonIdListe {

    @NotNull
    @Size(min = 1)
    private Long[] ids;

}
