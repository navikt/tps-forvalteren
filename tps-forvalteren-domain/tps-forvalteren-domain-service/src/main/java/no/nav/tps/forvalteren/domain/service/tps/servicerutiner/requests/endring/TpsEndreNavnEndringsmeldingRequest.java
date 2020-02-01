package no.nav.tps.forvalteren.domain.service.tps.servicerutiner.requests.endring;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import no.nav.tps.forvalteren.domain.service.tps.servicerutiner.requests.TpsServiceRoutineEndringRequest;

@Getter
@Setter
@NoArgsConstructor
@JacksonXmlRootElement(localName = "endreNavn")
public class TpsEndreNavnEndringsmeldingRequest extends TpsServiceRoutineEndringRequest {

    private String fornavn;
    private String mellomnavn;
    private String etternavn;
    private String tidligerenavn;
    private String kortnavn;
    private String datoNyttNavn;
    
    @Builder
    public TpsEndreNavnEndringsmeldingRequest(String serviceRutinenavn, String offentligIdent, String fornavn, String mellomnavn, String etternavn, String tidligerenavn, String kortnavn, String datoNyttNavn) { //NOSONAR inheritance Builder krever super i allArgsConstructor. Lombok har ikke støtte for det.
        super(serviceRutinenavn, offentligIdent);
        this.fornavn = fornavn;
        this.mellomnavn = mellomnavn;
        this.etternavn = etternavn;
        this.tidligerenavn = tidligerenavn;
        this.kortnavn = kortnavn;
        this.datoNyttNavn = datoNyttNavn;
    }//NOSONAR
}
