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
        @JsonSubTypes.Type(value = RsGateadresse.class, name = "GATE"),
        @JsonSubTypes.Type(value = RsMatrikkeladresse.class, name = "MATR")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public abstract class RsAdresse {

    private Long adresseId;

    private Long personId;

    @Size(min = 4, max = 4)
    private String postnr;

    @Size(min = 4, max = 4)
    private String kommunenr;

    private LocalDateTime flyttedato;

    public abstract boolean isValidAdresse();
}
