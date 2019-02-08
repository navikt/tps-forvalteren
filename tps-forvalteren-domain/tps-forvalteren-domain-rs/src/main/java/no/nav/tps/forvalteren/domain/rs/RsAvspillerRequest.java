package no.nav.tps.forvalteren.domain.rs;

import static java.util.Objects.isNull;

import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RsAvspillerRequest {

    private String miljoe;
    private String datoFra;
    private String tidFra;
    private String datoTil;
    private String tidTil;
    private Meldingsformat format;
    private List<String> typer;
    private List<String> kilder;
    private List<String> identer;

    public List<String> getTyper() {
        if (isNull(typer)) {
            typer = new ArrayList<>();
        }
        return typer;
    }

    public List<String> getKilder() {
        if (isNull(kilder)) {
            kilder = new ArrayList<>();
        }
        return kilder;
    }

    public List<String> getIdenter() {
        if (isNull(identer)) {
            identer = new ArrayList<>();
        }
        return identer;
    }
}
