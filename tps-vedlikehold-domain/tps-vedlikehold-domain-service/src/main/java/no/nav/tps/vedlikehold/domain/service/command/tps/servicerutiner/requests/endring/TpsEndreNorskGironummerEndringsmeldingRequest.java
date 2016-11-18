package no.nav.tps.vedlikehold.domain.service.command.tps.servicerutiner.requests.endring;

import com.fasterxml.jackson.xml.annotate.JacksonXmlRootElement;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import no.nav.tps.vedlikehold.domain.service.command.tps.servicerutiner.requests.TpsServiceRoutineEndringRequest;

/**
 * Created by F148888 on 16.11.2016.
 */

@Getter
@Setter
@NoArgsConstructor
@JacksonXmlRootElement(localName = "endreGironrNorsk")
public class TpsEndreNorskGironummerEndringsmeldingRequest extends TpsServiceRoutineEndringRequest {

    private String offentligIdent;
    private String giroNrNorsk;
    private String datogiroNrNorsk;

}
