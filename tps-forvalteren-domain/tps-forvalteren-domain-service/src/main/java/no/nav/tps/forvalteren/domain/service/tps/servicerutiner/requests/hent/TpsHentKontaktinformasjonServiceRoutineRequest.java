package no.nav.tps.forvalteren.domain.service.tps.servicerutiner.requests.hent;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import no.nav.tps.forvalteren.domain.service.tps.servicerutiner.requests.TpsServiceRoutineHentRequest;

@Getter
@Setter
@NoArgsConstructor
@JacksonXmlRootElement(localName = "tpsServiceRutine")
public class TpsHentKontaktinformasjonServiceRoutineRequest extends TpsServiceRoutineHentRequest {

    private String fnr;

}
