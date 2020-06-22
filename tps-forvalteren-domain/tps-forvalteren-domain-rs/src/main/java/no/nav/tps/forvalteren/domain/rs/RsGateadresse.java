package no.nav.tps.forvalteren.domain.rs;

import static org.apache.commons.lang3.StringUtils.isNotBlank;

import javax.validation.constraints.Size;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
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
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class RsGateadresse extends RsAdresse{

    @Size(min = 1, max = 50)
    private String gateadresse;

    @Size(min = 1, max = 4)
    private String husnummer;

    @Size(min = 1, max = 4)
    private String gatekode;

    @Override
    @JsonIgnore
    public boolean isValidAdresse() {
        return isNotBlank(getGatekode());
    }
}
