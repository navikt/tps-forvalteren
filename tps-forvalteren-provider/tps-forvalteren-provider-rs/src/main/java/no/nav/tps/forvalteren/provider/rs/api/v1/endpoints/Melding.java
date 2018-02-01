package no.nav.tps.forvalteren.provider.rs.api.v1.endpoints;

import java.time.LocalDate;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Melding {
    private String handling;
    private String ident;
    private LocalDate doedsdato;
    private String miljoe;
    private String bruker;
    private String tidspunkt;
    private String status;
    private String tilstand;

    @Override public String toString() {
        return "Melding{" +
                "handling='" + handling + '\'' +
                ", ident='" + ident + '\'' +
                ", doedsdato=" + doedsdato +
                ", miljoe='" + miljoe + '\'' +
                ", bruker='" + bruker + '\'' +
                ", tidspunkt='" + tidspunkt + '\'' +
                ", status='" + status + '\'' +
                ", tilstand='" + tilstand + '\'' +
                '}';
    }
}
