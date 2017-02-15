package no.nav.tps.vedlikehold.domain.service.tps;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ResponseStatus {

    private String kode;
    private String melding;
    private String utfyllendeMelding;

}
