package no.nav.tps.forvalteren.domain.service.tps.servicerutiner.requests.endring;

import com.fasterxml.jackson.xml.annotate.JacksonXmlRootElement;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import no.nav.tps.forvalteren.domain.service.tps.servicerutiner.requests.TpsServiceRoutineEndringRequest;

@Getter
@Setter
@NoArgsConstructor
@JacksonXmlRootElement(localName = "endreRelasjon")
public class TpsEndreRelasjonRequest extends TpsServiceRoutineEndringRequest {

    private String typeRelasjon;
    private String relasjonsFnr1;
    private String relasjonsFnr2;
    private String fomRelasjon;
    private String tomRelasjon;
}
