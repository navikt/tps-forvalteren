package no.nav.tps.forvalteren.domain.rs;

import static java.util.Objects.isNull;

import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RsTyperOgKilderResponse {

    private List<TypeOgAntall> typer;
    private List<TypeOgAntall> kilder;

    public List<TypeOgAntall> getTyper() {
        if (isNull(typer)) {
            typer = new ArrayList();
        }
        return typer;
    }

    public List<TypeOgAntall> getKilder() {
        if (isNull(kilder)) {
            kilder = new ArrayList<>();
        }
        return kilder;
    }
}
