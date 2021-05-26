package no.nav.tps.forvalteren.domain.rs;

import java.time.LocalDateTime;
import javax.validation.constraints.Size;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "adressetype")
@JsonSubTypes({
        @JsonSubTypes.Type(value = RsRequestGateadresse.class, name = "GATE"),
        @JsonSubTypes.Type(value = RsRequestMatrikkeladresse.class, name = "MATR")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public abstract class RsRequestAdresse {

    public enum TilleggType {CO_NAVN, LEILIGHET_NR, SEKSJON_NR, BOLIG_NR }

    private Long adresseId;

    private Long personId;

    @Size(min = 4, max = 4)
    private String postnr;

    @Size(min = 4, max = 4)
    private String kommunenr;

    private LocalDateTime flyttedato;

    private TilleggAdressetype tilleggsadresse;

    private String bolignr;

    private String matrikkelId;

    public abstract boolean isValidAdresse();

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class TilleggAdressetype {

        private TilleggType tilleggType;
        private Integer nummer;
    }
}
