package no.nav.tps.forvalteren.domain.rs;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.Size;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RsPersonKriteriumRequest {

    @NotEmpty
    @Size(min = 1)
    private List<RsPersonKriterier> personKriterierListe;
    
    private boolean withAdresse;

}
