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
@JacksonXmlRootElement(localName = "endreGironrNorsk")
public class TpsEndreNorskGironummerEndringsmeldingRequest extends TpsServiceRoutineEndringRequest {

    private String giroNrNorsk;
    private String datogiroNrNorsk;
    
    @Builder
    public TpsEndreNorskGironummerEndringsmeldingRequest(String serviceRutinenavn, String offentligIdent, String giroNrNorsk, String datogiroNrNorsk) {
        super(serviceRutinenavn, offentligIdent);
        this.giroNrNorsk = giroNrNorsk;
        this.datogiroNrNorsk = datogiroNrNorsk;
    }
}
