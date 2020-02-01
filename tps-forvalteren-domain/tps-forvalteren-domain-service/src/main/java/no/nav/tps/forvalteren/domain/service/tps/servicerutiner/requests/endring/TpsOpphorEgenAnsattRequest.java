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
@JacksonXmlRootElement(localName = "opphorEgenAnsatt")
public class TpsOpphorEgenAnsattRequest extends TpsServiceRoutineEndringRequest {

    @Builder
    public TpsOpphorEgenAnsattRequest(String serviceRutinenavn, String offentligIdent) {
        super(serviceRutinenavn, offentligIdent);
    }
}
