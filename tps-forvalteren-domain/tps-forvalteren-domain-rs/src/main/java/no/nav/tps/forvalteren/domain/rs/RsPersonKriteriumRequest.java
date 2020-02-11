package no.nav.tps.forvalteren.domain.rs;

import java.util.ArrayList;
import java.util.List;
import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RsPersonKriteriumRequest {

    @NotEmpty
    @Size(min = 1)
    private List<RsPersonKriterier> personKriterierListe;

    @Valid
    private AdresseNrInfo adresseNrInfo;

    public List<RsPersonKriterier> getPersonKriterierListe() {
        if (personKriterierListe == null) {
            personKriterierListe = new ArrayList<>();
        }
        return personKriterierListe;
    }
}