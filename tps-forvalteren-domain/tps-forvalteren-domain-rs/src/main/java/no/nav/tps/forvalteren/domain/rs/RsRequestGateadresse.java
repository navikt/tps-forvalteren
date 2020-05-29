package no.nav.tps.forvalteren.domain.rs;

import static java.util.Objects.nonNull;

import javax.validation.constraints.Size;
import com.fasterxml.jackson.annotation.JsonTypeName;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@JsonTypeName("Gateadresse")
@NoArgsConstructor
@AllArgsConstructor
public class RsRequestGateadresse extends RsRequestAdresse {

    @Size(min = 1, max = 50)
    private String gateadresse;

    @Size(min = 1, max = 4)
    private String husnummer;

    @Size(min = 1, max = 4)
    private String gatekode;

    @Override
    public boolean isValidAdresse() {
        return nonNull(getGatekode());
    }
}
