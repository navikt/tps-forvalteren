package no.nav.tps.vedlikehold.domain.service.tps.servicerutiner.requests.hent;

import com.fasterxml.jackson.xml.annotate.JacksonXmlRootElement;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import no.nav.tps.vedlikehold.domain.service.tps.servicerutiner.requests.TpsServiceRoutineHentRequest;

@Getter
@Setter
@NoArgsConstructor
@JacksonXmlRootElement(localName = "tpsServiceRutine")
public class TpsSokPersonServiceRoutineRequest extends TpsServiceRoutineHentRequest {

    private String navn;
    private String etternavn;
    private String fornavn;
    private String adresseNavn;
    private String postnr;
    private String husnrFra;
    private String husnrTil;
    private String knr;
    private String buffNr;


}
