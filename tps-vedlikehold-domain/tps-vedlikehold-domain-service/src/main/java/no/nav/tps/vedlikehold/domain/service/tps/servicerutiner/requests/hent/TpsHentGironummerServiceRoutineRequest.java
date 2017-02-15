package no.nav.tps.vedlikehold.domain.service.tps.servicerutiner.requests.hent;

import com.fasterxml.jackson.xml.annotate.JacksonXmlRootElement;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import no.nav.tps.vedlikehold.domain.service.tps.servicerutiner.requests.TpsServiceRoutineHentRequest;

/**
 * Created by F148888 on 14.11.2016.
 */

@Getter
@Setter
@NoArgsConstructor
@JacksonXmlRootElement(localName = "tpsServiceRutine")
public class TpsHentGironummerServiceRoutineRequest extends TpsServiceRoutineHentRequest {

    private String fnr;

}

