package no.nav.tps.forvalteren.domain.service.tps.servicerutiner.requests.endring;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import no.nav.tps.forvalteren.domain.service.tps.servicerutiner.requests.TpsServiceRoutineEndringRequest;

@Getter
@Setter
@NoArgsConstructor
@JacksonXmlRootElement(localName = "endreGironrUtl")
public class TpsEndreUtenlandskGironummerEndringsmeldingRequest extends TpsServiceRoutineEndringRequest {

    private String giroNrUtland;
    private String datoGiroNr;
    private String kodeSwift;
    private String kodeLand;
    private String bankNavn;
    private String bankKode;
    private String valuta;
    private String bankAdresse1;
    private String bankAdresse2;
    private String bankAdresse3;
}
