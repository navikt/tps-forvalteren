package no.nav.tps.vedlikehold.domain.service.command.tps.servicerutiner.requests;

import com.fasterxml.jackson.xml.annotate.JacksonXmlRootElement;

/**
 * Created by f148888 on 30.09.2016.
 */

// Er vel egentlig "tpsRequestServiceRoutine"
@JacksonXmlRootElement(localName = "tpsServiceRutine")
public class TpsSokPersonRequestServiceRoutine extends TpsRequestServiceRoutine {

    private String navn;
    private String etternavn;
    private String fornavn;
    private String adresseNavn;
    private String postnr;
    private String husnrFra;
    private String husnrTil;
    private String knr;
    private String buffNr;

    public String getNavn() {
        return navn;
    }

    public void setNavn(String navn) {
        this.navn = navn;
    }

    public String getEtternavn() {
        return etternavn;
    }

    public void setEtternavn(String etternavn) {
        this.etternavn = etternavn;
    }

    public String getFornavn() {
        return fornavn;
    }

    public void setFornavn(String fornavn) {
        this.fornavn = fornavn;
    }


    public String getAdresseNavn() {
        return adresseNavn;
    }

    public void setAdresseNavn(String adresseNavn) {
        this.adresseNavn = adresseNavn;
    }

    public String getPostnr() {
        return postnr;
    }

    public void setPostnr(String postnr) {
        this.postnr = postnr;
    }

    public String getHusnrFra() {
        return husnrFra;
    }

    public void setHusnrFra(String husnrFra) {
        this.husnrFra = husnrFra;
    }

    public String getHusnrTil() {
        return husnrTil;
    }

    public void setHusnrTil(String husnrTil) {
        this.husnrTil = husnrTil;
    }

    public String getKnr(){
        return knr;
    }

    public void setKnr(String knr) {
        this.knr = knr;
    }

    public String getBuffNr() {
        return buffNr;
    }

    public void setBuffNr(String buffNr) {
        this.buffNr = buffNr;
    }
}
