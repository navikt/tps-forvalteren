package no.nav.tps.vedlikehold.domain.service.command.tps.servicerutiner.requests;

import com.fasterxml.jackson.xml.annotate.JacksonXmlRootElement;
import no.nav.tps.vedlikehold.domain.service.command.tps.TpsRequest;

@JacksonXmlRootElement(localName = "endreNavn")
public class TpsEndreNavnRequestEndringsmelding extends TpsRequest {

    private String offentligIdent;
    private String fornavn;
    private String mellomnavn;
    private String etternavn;
    private String tidligerenavn;
    private String kortnavn;
    private String datoNyttNavn;

    public String getOffentligIdent() {
        return offentligIdent;
    }

    public void setOffentligIdent(String offentligIdent) {
        this.offentligIdent = offentligIdent;
    }

    public String getFornavn() {
        return fornavn;
    }

    public void setFornavn(String fornavn) {
        this.fornavn = fornavn;
    }

    public String getMellomnavn() {
        return mellomnavn;
    }

    public void setMellomnavn(String mellomnavn) {
        this.mellomnavn = mellomnavn;
    }

    public String getEtternavn() {
        return etternavn;
    }

    public void setEtternavn(String etternavn) {
        this.etternavn = etternavn;
    }

    public String getTidligerenavn() {
        return tidligerenavn;
    }

    public void setTidligerenavn(String tidligerenavn) {
        this.tidligerenavn = tidligerenavn;
    }

    public String getKortnavn() {
        return kortnavn;
    }

    public void setKortnavn(String kortnavn) {
        this.kortnavn = kortnavn;
    }

    public String getDatoNyttNavn() {
        return datoNyttNavn;
    }

    public void setDatoNyttNavn(String datoNyttNavn) {
        this.datoNyttNavn = datoNyttNavn;
    }
}
