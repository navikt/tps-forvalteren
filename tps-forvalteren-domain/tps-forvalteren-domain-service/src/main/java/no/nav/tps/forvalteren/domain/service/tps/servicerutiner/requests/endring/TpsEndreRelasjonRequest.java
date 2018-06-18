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
@JacksonXmlRootElement(localName = "endreRelasjon")
public class TpsEndreRelasjonRequest extends TpsServiceRoutineEndringRequest {

    private String typeRelasjon;
    private String relasjonsFnr1;
    private String relasjonsFnr2;
    private String fomRelasjon;
    private String tomRelasjon;
    
    @Builder
    public TpsEndreRelasjonRequest(String serviceRutinenavn, String offentligIdent, String typeRelasjon, String relasjonsFnr1, String relasjonsFnr2, String fomRelasjon, String tomRelasjon) {
        super(serviceRutinenavn, offentligIdent);
        this.typeRelasjon = typeRelasjon;
        this.relasjonsFnr1 = relasjonsFnr1;
        this.relasjonsFnr2 = relasjonsFnr2;
        this.fomRelasjon = fomRelasjon;
        this.tomRelasjon = tomRelasjon;
    }
}
