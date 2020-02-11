package no.nav.tps.forvalteren.domain.service.tps.servicerutiner.requests.endring;

import com.fasterxml.jackson.xml.annotate.JacksonXmlRootElement;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import no.nav.tps.forvalteren.domain.service.tps.servicerutiner.requests.TpsServiceRoutineEndringRequest;

@Getter
@Setter
@NoArgsConstructor
@JacksonXmlRootElement(localName = "opprettSikkerhetsTiltak")
public class TpsEndreSikkerhetstiltakRequest extends TpsServiceRoutineEndringRequest {

    private String typeSikkerhetsTiltak;
    private String fom;
    private String tom;
    private String beskrSikkerhetsTiltak;

    @Builder
    public TpsEndreSikkerhetstiltakRequest(String serviceRutinenavn, String offentligIdent, String typeSikkerhetsTiltak, String fom, String tom, String beskrSikkerhetsTiltak) {
        super(serviceRutinenavn, offentligIdent);
        this.typeSikkerhetsTiltak = typeSikkerhetsTiltak;
        this.fom = fom;
        this.tom = tom;
        this.beskrSikkerhetsTiltak = beskrSikkerhetsTiltak;
    }
}
