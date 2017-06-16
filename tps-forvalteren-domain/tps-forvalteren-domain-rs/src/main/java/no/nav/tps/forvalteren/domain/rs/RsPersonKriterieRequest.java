package no.nav.tps.forvalteren.domain.rs;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

@Getter
@Setter
public class RsPersonKriterieRequest {

    @NotNull
    @Size(min = 1)
    List<RsPersonKriterier> personKriterierListe;

}
