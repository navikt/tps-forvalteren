package no.nav.tps.forvalteren.domain.test.provider;

import no.nav.tps.forvalteren.domain.jpa.Personmal;

public final class PersonmalProvider {

    private PersonmalProvider(){

    }

    public static Personmal.PersonmalBuilder personmalA() {
        return Personmal.builder()
                .personmalNavn("TestMal")
                .personmalBeskrivelse("Mal-beskrivelse")
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
                .adresse("STORGATA")
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

    public static Personmal.PersonmalBuilder personmalB() {
        return Personmal.builder()
                .personmalNavn("En annen testmal")
                .personmalBeskrivelse("En annen mal, som også er tom")
                .fodtEtter("")
                .fodtFor("")
                .kjonn("")
                .statsborgerskap("")
                .spesreg("")
                .spesregDato("")
                .doedsdato("")
                .sivilstand("")
                .innvandretFraLand("")
                .minAntallBarn(0)
                .maxAntallBarn(0)
                .adresse("")
                .gateHusnr("")
                .gatePostnr("")
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
