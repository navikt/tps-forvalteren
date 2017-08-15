package no.nav.tps.forvalteren.service.command.testdata.skd;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SkdFeltDefinisjon {

    private String defaultVerdi;
    private String nokkelNavn;
    private int idRekkefolge;
    private int antallBytesAvsatt;
    private int fraByte;
    private int tilByte;
    private String verdi;
    private boolean isValueLastInSkdField = false;

    public SkdFeltDefinisjon(String nokkelNavn, String defaultVerdi, int idRekkefolge, int antallByesAvsatt,
                             int fraByte, int tilByte){
        this.nokkelNavn = nokkelNavn;
        this.defaultVerdi = defaultVerdi;
        this.idRekkefolge = idRekkefolge;
        this.antallBytesAvsatt = antallByesAvsatt;
        this.fraByte = fraByte;
        this.tilByte = tilByte;
    }

    public SkdFeltDefinisjon(String nokkelNavn, String defaultVerdi, int idRekkefolge, int antallByesAvsatt){
        this.nokkelNavn = nokkelNavn;
        this.defaultVerdi = defaultVerdi;
        this.idRekkefolge = idRekkefolge;
        this.antallBytesAvsatt = antallByesAvsatt;
    }

    public SkdFeltDefinisjon(String nokkelNavn, String defaultVerdi, int idRekkefolge, int antallByesAvsatt, boolean isValueLastInSkdField){
        this.nokkelNavn = nokkelNavn;
        this.defaultVerdi = defaultVerdi;
        this.idRekkefolge = idRekkefolge;
        this.antallBytesAvsatt = antallByesAvsatt;
        this.isValueLastInSkdField = isValueLastInSkdField;
    }

    public SkdFeltDefinisjon(){}
}
