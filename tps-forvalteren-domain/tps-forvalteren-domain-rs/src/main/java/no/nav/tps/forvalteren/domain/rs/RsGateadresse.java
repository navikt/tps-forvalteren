package no.nav.tps.forvalteren.domain.rs;

import com.fasterxml.jackson.annotation.JsonTypeName;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Size;

@Getter
@Setter
@JsonTypeName("Gateadresse")
@NoArgsConstructor
@AllArgsConstructor
public class RsGateadresse extends RsAdresse{

    @Size(min = 1, max = 50)
    private String gateadresse;

    @Size(min = 1, max = 4)
    private String husnummer;

    @Size(min = 1, max = 4)
    private String gatekode;

}
