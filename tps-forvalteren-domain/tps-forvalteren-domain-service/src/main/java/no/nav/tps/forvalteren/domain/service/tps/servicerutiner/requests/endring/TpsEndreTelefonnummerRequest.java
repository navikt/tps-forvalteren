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
@JacksonXmlRootElement(localName = "nyTelefon")
public class TpsEndreTelefonnummerRequest extends TpsServiceRoutineEndringRequest {

    private String typeTelefon;
    private String telefonLandkode;
    private String telefonNr;

    @Builder
    public TpsEndreTelefonnummerRequest(String serviceRutinenavn, String offentligIdent, String typeTelefon, String telefonLandkode, String telefonNr) {
        super(serviceRutinenavn, offentligIdent);
        this.typeTelefon = typeTelefon;
        this.telefonLandkode = telefonLandkode;
        this.telefonNr = telefonNr;
    }
}