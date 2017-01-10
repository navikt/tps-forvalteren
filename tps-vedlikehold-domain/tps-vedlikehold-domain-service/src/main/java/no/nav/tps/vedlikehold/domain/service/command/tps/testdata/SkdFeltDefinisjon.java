package no.nav.tps.vedlikehold.domain.service.command.tps.testdata;

import lombok.Getter;
import lombok.Setter;


/**
 * Created by Peter Fløgstad on 05.01.2017.
 */

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
