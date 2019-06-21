package no.nav.tps.forvalteren.domain.rs;

import static java.util.Objects.isNull;

import java.time.LocalDateTime;
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

    private String miljoeFra;
    private LocalDateTime datoFra;
    private LocalDateTime datoTil;
    private Meldingsformat format;
    private List<String> typer;
    private List<String> kilder;
    private List<String> identer;
    private String miljoeTil;
    private String queue;
    private Long pageNumber;
    private Long bufferSize;
    private Long timeout;

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
