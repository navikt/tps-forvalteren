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
@JacksonXmlRootElement(localName = "endreGironrNorsk")
public class TpsEndreNorskGironummerRequest extends TpsServiceRoutineEndringRequest {

    private String giroNrNorsk;
    private String datoGiroNrNorsk; // yyyy-MM-dd

    @Builder
    public TpsEndreNorskGironummerRequest(String serviceRutinenavn, String offentligIdent, String giroNrNorsk, String datoGiroNrNorsk) {
        super(serviceRutinenavn, offentligIdent);
        this.giroNrNorsk = giroNrNorsk;
        this.datoGiroNrNorsk = datoGiroNrNorsk;
    }
}