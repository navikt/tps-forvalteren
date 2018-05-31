package no.nav.tps.forvalteren.domain.service.tps.servicerutiner.requests.endring;

import com.fasterxml.jackson.xml.annotate.JacksonXmlRootElement;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import no.nav.tps.forvalteren.domain.service.tps.servicerutiner.requests.TpsServiceRoutineEndringRequest;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JacksonXmlRootElement(localName = "opprettSikkerhetsTiltak")
public class TpsEndreSikkerhetstiltakRequest extends TpsServiceRoutineEndringRequest {

    private String typeSikkerhetsTiltak;
    private String fom;
    private String tom;
    private String beskrSikkerhetsTiltak;

}
