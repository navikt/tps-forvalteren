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
@JacksonXmlRootElement(localName = "endreGironrUtl")
public class TpsEndreUtenlandskGironummerEndringsmeldingRequest extends TpsServiceRoutineEndringRequest {

    private String giroNrUtland;
    private String datoGiroNr;
    private String kodeSwift;
    private String kodeLand;
    private String bankNavn;
    private String bankKode;
    private String valuta;
    private String bankAdresse1;
    private String bankAdresse2;
    private String bankAdresse3;
    
    @Builder
    public TpsEndreUtenlandskGironummerEndringsmeldingRequest(String serviceRutinenavn, String offentligIdent, String giroNrUtland, String datoGiroNr, String kodeSwift, String kodeLand, String bankNavn, //NOSONAR inheritance Builder krever super i allArgsConstructor. Lombok har ikke støtte for det.
            String bankKode, String valuta, String bankAdresse1, String bankAdresse2, String bankAdresse3) { //NOSONAR
        super(serviceRutinenavn, offentligIdent);
        this.giroNrUtland = giroNrUtland;
        this.datoGiroNr = datoGiroNr;
        this.kodeSwift = kodeSwift;
        this.kodeLand = kodeLand;
        this.bankNavn = bankNavn;
        this.bankKode = bankKode;
        this.valuta = valuta;
        this.bankAdresse1 = bankAdresse1;
        this.bankAdresse2 = bankAdresse2;
        this.bankAdresse3 = bankAdresse3;
    }
}
