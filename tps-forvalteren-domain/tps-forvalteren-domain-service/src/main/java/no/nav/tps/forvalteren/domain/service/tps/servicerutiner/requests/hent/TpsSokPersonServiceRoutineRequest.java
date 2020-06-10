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
public class TpsSokPersonServiceRoutineRequest extends TpsServiceRoutineHentRequest {

    private String navn;
    private String navnFTE;
    private String navnehist;
    private String etternavn;
    private String etternavnFTE;
    private String fornavn;
    private String kjonn;
    private String adresseNavn;
    private String adresseFTE;
    private String adresseType;
    private String adressehist;
    private String postnr;
    private String husnrFra;
    private String bokstavFra;
    private String husnrTil;
    private String bokstavTil;
    private String knr;
    private String landKode;
    private String fodselsDatoFra;
    private String fodselsDatoTil;
    private String alderFra;
    private String alderTil;
    private String identType;
    private String personStatus;
    private String statsborgerskap;
    private String tknr;
    private String stigAvt;
    private String sortering;
    private String buffNr;
}
