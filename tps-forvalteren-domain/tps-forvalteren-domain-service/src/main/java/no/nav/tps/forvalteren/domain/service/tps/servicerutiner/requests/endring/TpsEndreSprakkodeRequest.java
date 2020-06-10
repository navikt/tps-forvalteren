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
@JacksonXmlRootElement(localName = "endreSprak")
public class TpsEndreSprakkodeRequest extends TpsServiceRoutineEndringRequest {

    private String sprakKode;
    private String datoSprak;

    @Builder
    public TpsEndreSprakkodeRequest(String serviceRutinenavn, String offentligIdent, String sprakKode, String datoSprak) {
        super(serviceRutinenavn, offentligIdent);
        this.sprakKode = sprakKode;
        this.datoSprak = datoSprak;
    }
}