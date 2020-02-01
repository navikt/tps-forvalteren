package no.nav.tps.forvalteren.domain.service.tps.servicerutiner.requests.endring;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import no.nav.tps.forvalteren.domain.service.tps.servicerutiner.requests.TpsServiceRoutineEndringRequest;

@Getter
@Setter
@NoArgsConstructor
@JacksonXmlRootElement(localName = "opphorSikkerhetsTiltak")
public class TpsOpphorSikkerhetsTiltakRequest extends TpsServiceRoutineEndringRequest {

    @Builder
    public TpsOpphorSikkerhetsTiltakRequest(String serviceRutinenavn, String offentligIdent) {
        super(serviceRutinenavn, offentligIdent);
    }
}
