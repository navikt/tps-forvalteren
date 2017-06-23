package no.nav.tps.forvalteren.service.command.testdata.skd;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SkdFeltDefinisjon {

    protected String defaultVerdi;
    protected String nokkelNavn;
    protected int idRekkefolge;   // For sortering for hvor de skal være.
    protected int antallBytesAvsatt;
    protected int fraByte;
    protected int tilByte;
    String verdi;

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
}
