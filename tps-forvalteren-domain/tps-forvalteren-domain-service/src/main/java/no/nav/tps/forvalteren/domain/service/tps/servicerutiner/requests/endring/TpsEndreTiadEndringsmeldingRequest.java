package no.nav.tps.forvalteren.domain.service.tps.servicerutiner.requests.endring;

import com.fasterxml.jackson.xml.annotate.JacksonXmlRootElement;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import no.nav.tps.forvalteren.domain.service.tps.servicerutiner.requests.TpsServiceRoutineEndringRequest;

@Getter
@Setter
@NoArgsConstructor
@JacksonXmlRootElement(localName = "nyAdresseNavNorge")
public class TpsEndreTiadEndringsmeldingRequest extends TpsServiceRoutineEndringRequest {

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

    @Builder
    public TpsEndreTiadEndringsmeldingRequest(String serviceRutinenavn, String offentligIdent, String datoTom, String typeAdresseNavNorge, String typeTilleggslinje, String tilleggslinje, String kommunenrTiad,
            String gatekode, String gatenavn, String husnr, String postNr, String husbokstav, String bolignr, String eiendomsnav, String postboksnr, String postboksAnlegg) {
        super(serviceRutinenavn, offentligIdent);
        this.datoTom = datoTom;
        this.typeAdresseNavNorge = typeAdresseNavNorge;
        this.typeTilleggslinje = typeTilleggslinje;
        this.tilleggslinje = tilleggslinje;
        this.kommunenrTiad = kommunenrTiad;
        this.gatekode = gatekode;
        this.gatenavn = gatenavn;
        this.husnr = husnr;
        this.postNr = postNr;
        this.husbokstav = husbokstav;
        this.bolignr = bolignr;
        this.eiendomsnav = eiendomsnav;
        this.postboksnr = postboksnr;
        this.postboksAnlegg = postboksAnlegg;
    }//NOSONAR
}
