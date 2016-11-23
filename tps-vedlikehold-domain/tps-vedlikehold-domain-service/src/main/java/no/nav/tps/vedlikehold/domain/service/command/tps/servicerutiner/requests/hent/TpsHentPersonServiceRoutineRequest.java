package no.nav.tps.vedlikehold.domain.service.command.tps.servicerutiner.requests.hent;

import com.fasterxml.jackson.xml.annotate.JacksonXmlRootElement;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import no.nav.tps.vedlikehold.domain.service.command.tps.servicerutiner.requests.TpsServiceRoutineHentRequest;

/**
 * @author Øyvind Grimnes, Visma Consulting AS
 */
@Getter
@Setter
@NoArgsConstructor
@JacksonXmlRootElement(localName = "tpsServiceRutine")
public class TpsHentPersonServiceRoutineRequest extends TpsServiceRoutineHentRequest {

    private String fnr;

}
