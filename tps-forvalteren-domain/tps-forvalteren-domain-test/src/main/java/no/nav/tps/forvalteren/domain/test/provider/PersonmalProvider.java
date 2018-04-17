package no.nav.tps.forvalteren.domain.test.provider;

import no.nav.tps.forvalteren.domain.jpa.Personmal;

public class PersonmalProvider {

    private PersonmalProvider(){

    }

    public static Personmal.PersonmalBuilder personmalA() {
        return Personmal.builder()
                .fodtEtter("20100101")
                .fodtFor("20180101")
                .kjonn("K")
                .statsborgerskap("001")
                .spesreg("1")
                .spesregDato("")
                .doedsdato("")
                .sivilstand("1")
                .innvandretFraLand("")
                .minAntallBarn(0)
                .maxAntallBarn(0)
                .gateadresse("STORGATA")
                .gateHusnr("2")
                .gatePostnr("0010")
                .gateKommunenr("")
                .gateFlyttedatoFra("")
                .gateFlyttedatoTil("")
                .postLinje1("")
                .postLinje2("")
                .postLinje3("")
                .postLand("")
                .postGardnr("")
                .postBruksnr("")
                .postFestenr("")
                .postUndernr("")
                .postPostnr("")
                .postKommunenr("")
                .postFlyttedatoFra("")
                .postFlyttedatoTil("");
    }
}
