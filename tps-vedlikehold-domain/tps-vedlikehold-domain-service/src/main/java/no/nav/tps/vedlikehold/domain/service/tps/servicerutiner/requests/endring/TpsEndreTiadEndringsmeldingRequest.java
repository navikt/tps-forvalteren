package no.nav.tps.vedlikehold.domain.service.tps.servicerutiner.requests.endring;

import com.fasterxml.jackson.xml.annotate.JacksonXmlRootElement;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import no.nav.tps.vedlikehold.domain.service.tps.servicerutiner.requests.TpsServiceRoutineEndringRequest;

@Getter
@Setter
@NoArgsConstructor
@JacksonXmlRootElement(localName = "nyAdresseNavNorge")
public class TpsEndreTiadEndringsmeldingRequest extends TpsServiceRoutineEndringRequest {

    private String offentligIdent;
    private String datoTom;
    private String typeAdresseNavNorge;
    private String typeTilleggslinje;
    private String tilleggslinje;
    private String kommunenrTiad;
    private String gatekode;
    private String gatenavn;
    private String husnr;
    private String postNr;
    private String husbokstav;
    private String bolignr;
    private String eiendomsnav;
    private String postboksnr;
    private String postboksAnlegg;

}
