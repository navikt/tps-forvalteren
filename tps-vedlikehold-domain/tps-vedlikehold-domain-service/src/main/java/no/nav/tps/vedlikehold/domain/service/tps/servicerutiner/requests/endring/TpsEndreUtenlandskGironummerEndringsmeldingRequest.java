package no.nav.tps.vedlikehold.domain.service.tps.servicerutiner.requests.endring;

import com.fasterxml.jackson.xml.annotate.JacksonXmlRootElement;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import no.nav.tps.vedlikehold.domain.service.tps.servicerutiner.requests.TpsServiceRoutineEndringRequest;

/**
 * Created by F148888 on 16.11.2016.
 */

@Getter
@Setter
@NoArgsConstructor
@JacksonXmlRootElement(localName = "endreGironrUtl")
public class TpsEndreUtenlandskGironummerEndringsmeldingRequest extends TpsServiceRoutineEndringRequest {

    private String offentligIdent;
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
