package no.nav.tps.forvalteren.domain.test.provider;

import no.nav.tps.forvalteren.domain.jpa.Personmal;

public class PersonmalProvider {

    private PersonmalProvider(){

    }

    public static Personmal.PersonmalBuilder personmalA() {
        return Personmal.builder()
                .fodtEtter()
                .fodtFor()
                .kjonn()
                .statsborgerskap()
                .spesreg()
                .spesregDato()
                .doedsdato()
                .sivilstand()
                .innvandretFraLand()
                .minAntallBarn()
                .maxAntallBarn()
                .gateadresse()
                .gateHusnr()
                .gatePostnr()
                .gateKommunenr()
                .gateFlyttedatoFra()
                .gateFlyttedatoTil()
                .postLinje1()
                .postLinje2()
                .postLinje3()
                .postLand()
                .postGardnr()
                .postBruksnr()
                .postFestenr()
                .postUndernr()
                .postPostnr()
                .postKommunenr()
                .postFlyttedatoFra()
                .postFlyttedatoTil();
    }
}
