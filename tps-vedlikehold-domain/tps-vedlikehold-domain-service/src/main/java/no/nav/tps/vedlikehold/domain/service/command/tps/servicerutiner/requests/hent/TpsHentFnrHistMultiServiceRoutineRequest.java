package no.nav.tps.vedlikehold.domain.service.command.tps.servicerutiner.requests.hent;

import com.fasterxml.jackson.xml.annotate.JacksonXmlElementWrapper;
import com.fasterxml.jackson.xml.annotate.JacksonXmlProperty;
import com.fasterxml.jackson.xml.annotate.JacksonXmlRootElement;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import no.nav.tps.vedlikehold.domain.service.command.tps.servicerutiner.requests.TpsServiceRoutineHentRequest;

/**
 * Created by Peter Fløgstad on 17.01.2017.
 */

@Getter
@Setter
@NoArgsConstructor
@JacksonXmlRootElement(localName = "tpsServiceRutine")
public class TpsHentFnrHistMultiServiceRoutineRequest extends TpsServiceRoutineHentRequest{
    private String antallFnr;
    private String buffNr;

    @JacksonXmlElementWrapper(localName = "nFnr")
    @JacksonXmlProperty(localName = "fnr")
    private String[] fnr;

}
