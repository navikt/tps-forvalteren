package no.nav.tps.forvalteren.domain.test.provider;

import java.time.LocalDate;

import no.nav.tps.forvalteren.domain.jpa.Personmal;

public final class PersonmalProvider {

    private PersonmalProvider() {

    }

    public static Personmal.PersonmalBuilder personmalA() {
        return Personmal.builder()
                .personmalNavn("TestMal")
                .personmalBeskrivelse("Mal-beskrivelse")
                .fodtEtter(LocalDate.of(2010, 1, 1))
                .fodtFor(LocalDate.of(2018, 1, 1))
                .kjonn("K")
                .statsborgerskap("001")
                .spesreg("1")
                .spesregDato(LocalDate.of(2018, 10, 1))
                .doedsdato(LocalDate.now())
                .egenAnsattDatoFom(LocalDate.of(2016, 10, 10))
                .egenAnsattDatoTom(LocalDate.of(2017,4,5))
                .sivilstand("1")
                .innvandretFraLand("218")
                .innvandretFraLandFlyttedato(LocalDate.of(2018, 10, 1))
                .innvandretFraLandRegdato(LocalDate.of(2017, 1, 2))
                .sikkerhetsTiltakDatoFom(LocalDate.of(2015, 6,7))
                .sikkerhetsTiltakDatoTom(LocalDate.of(2016, 7, 8))
                .beskrSikkerhetsTiltak("Farlig fyr dette")
                .minAntallBarn(0)
                .maxAntallBarn(0)
                .adresse("STORGATA")
                .gateHusnr("2")
                .gatePostnr("0010")
                .gateKommunenr("")
                .gateFlyttedatoFra(null)
                .gateFlyttedatoTil(null)
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
                .postFlyttedatoFra(null)
                .postFlyttedatoTil(null)
                .identType("FNR")
                .antallIdenter(1);
    }

    public static Personmal.PersonmalBuilder personmalB() {
        return Personmal.builder()
                .personmalNavn("En annen testmal")
                .personmalBeskrivelse("En annen mal, som også er tom")
                .fodtEtter(null)
                .fodtFor(null)
                .kjonn("")
                .statsborgerskap("")
                .spesreg("")
                .spesregDato(null)
                .doedsdato(null)
                .sivilstand("")
                .innvandretFraLand("")
                .minAntallBarn(0)
                .maxAntallBarn(0)
                .adresse("")
                .gateHusnr("")
                .gatePostnr("")
                .gateKommunenr("")
                .gateFlyttedatoFra(null)
                .gateFlyttedatoTil(null)
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
                .postFlyttedatoFra(null)
                .postFlyttedatoTil(null)
                .identType("FNR")
                .antallIdenter(1);
    }
}
