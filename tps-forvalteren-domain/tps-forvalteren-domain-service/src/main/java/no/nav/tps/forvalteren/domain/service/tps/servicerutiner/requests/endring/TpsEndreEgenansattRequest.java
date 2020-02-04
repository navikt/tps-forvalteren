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
@JacksonXmlRootElement(localName = "endreEgenAnsatt")
public class TpsEndreEgenansattRequest extends TpsServiceRoutineEndringRequest {
    private String fom;
    private String tom;

    @Builder
    public TpsEndreEgenansattRequest(String serviceRutinenavn, String offentligIdent, String fom, String tom) {
        super(serviceRutinenavn, offentligIdent);
        this.fom = fom;
        this.tom = tom;
    }
}
