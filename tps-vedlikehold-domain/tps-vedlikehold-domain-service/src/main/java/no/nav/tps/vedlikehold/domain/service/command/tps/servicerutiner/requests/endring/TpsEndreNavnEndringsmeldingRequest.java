package no.nav.tps.vedlikehold.domain.service.command.tps.servicerutiner.requests.endring;

import com.fasterxml.jackson.xml.annotate.JacksonXmlRootElement;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import no.nav.tps.vedlikehold.domain.service.command.tps.servicerutiner.requests.TpsServiceRoutineEndringRequest;

@Getter
@Setter
@NoArgsConstructor
@JacksonXmlRootElement(localName = "endreNavn")
public class TpsEndreNavnEndringsmeldingRequest extends TpsServiceRoutineEndringRequest {

    private String offentligIdent;
    private String fornavn;
    private String mellomnavn;
    private String etternavn;
    private String tidligerenavn;
    private String kortnavn;
    private String datoNyttNavn;

}
