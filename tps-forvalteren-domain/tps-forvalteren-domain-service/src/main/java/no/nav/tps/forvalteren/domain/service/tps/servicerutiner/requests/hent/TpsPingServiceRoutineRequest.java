package no.nav.tps.forvalteren.domain.service.tps.servicerutiner.requests.hent;

import com.fasterxml.jackson.xml.annotate.JacksonXmlRootElement;

import no.nav.tps.forvalteren.domain.service.tps.servicerutiner.requests.TpsServiceRoutineHentRequest;

@JacksonXmlRootElement(localName = "tpsServiceRutine")
public class TpsPingServiceRoutineRequest extends TpsServiceRoutineHentRequest {
}
