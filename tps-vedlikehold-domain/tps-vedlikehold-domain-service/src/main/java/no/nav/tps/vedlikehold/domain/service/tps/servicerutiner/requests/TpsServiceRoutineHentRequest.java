package no.nav.tps.vedlikehold.domain.service.tps.servicerutiner.requests;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;

@Getter
@Setter
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class TpsServiceRoutineHentRequest extends TpsServiceRoutineRequest {

    private String aksjonsDato;

    private String aksjonsKode;
    private String aksjonsKode2;

    public void setAksjonsKode(String aksjonsKode) {
        this.aksjonsKode = aksjonsKode.substring(0, 1);
        this.aksjonsKode2 = aksjonsKode.substring(1);
    }

    public void setAksjonsKode2(String aksjonsKode2) {
        // Do nothing. oh goodie...
    }

}
