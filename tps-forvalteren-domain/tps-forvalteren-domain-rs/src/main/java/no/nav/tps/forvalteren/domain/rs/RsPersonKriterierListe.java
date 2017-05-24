package no.nav.tps.forvalteren.domain.rs;

import lombok.Getter;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

@Getter
public class RsPersonKriterierListe {

    @NotNull
    @Size(min = 1)
    List<RsPersonKriterier> personKriterierListe;

}
