package no.nav.tps.forvalteren.provider.rs.api.v1.endpoints;

import java.time.LocalDate;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Meldinger {
    private String handling;
    private List<String> identer;
    private LocalDate doedsdato;
    private String miljoe;

    @Override public String toString() {
        return "Meldinger{" +
                "handling='" + handling + '\'' +
                ", identer=" + identer +
                ", doedsdato=" + doedsdato +
                ", miljoe='" + miljoe + '\'' +
                '}';
    }
}
