package no.nav.tps.forvalteren.domain.service.tps.servicerutiner.requests;

import com.fasterxml.jackson.xml.annotate.JacksonXmlRootElement;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@JacksonXmlRootElement(localName = "tpsServiceRutine")
public class TpsServiceRoutineHentByFnrBufNrRequest extends TpsServiceRoutineHentByFnrRequest {

    private String buffNr;
}
