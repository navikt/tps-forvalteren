package no.nav.tps.forvalteren.domain.service.tps.servicerutiner.requests.hent;

import com.fasterxml.jackson.xml.annotate.JacksonXmlElementWrapper;
import com.fasterxml.jackson.xml.annotate.JacksonXmlProperty;
import com.fasterxml.jackson.xml.annotate.JacksonXmlRootElement;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import no.nav.tps.forvalteren.domain.service.tps.servicerutiner.requests.TpsServiceRoutineHentRequest;

@Getter
@Setter
@NoArgsConstructor
@JacksonXmlRootElement(localName = "tpsServiceRutine")
public class TpsHentFnrHistMultiServiceRoutineRequest extends TpsServiceRoutineHentRequest {
    private String antallFnr;

    @JacksonXmlElementWrapper(localName = "nFnr")
    @JacksonXmlProperty(localName = "fnr")
    private String[] fnr;

}
