package no.nav.tps.forvalteren.domain.service.tps.servicerutiner.requests.hent;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

import no.nav.tps.forvalteren.domain.service.tps.servicerutiner.requests.TpsServiceRoutineHentRequest;


@JacksonXmlRootElement(localName = "tpsServiceRutine")
public class TpsPingServiceRoutineRequest extends TpsServiceRoutineHentRequest {
}
