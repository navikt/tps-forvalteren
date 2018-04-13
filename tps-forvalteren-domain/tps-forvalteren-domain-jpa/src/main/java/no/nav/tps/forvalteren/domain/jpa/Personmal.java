package no.nav.tps.forvalteren.domain.jpa;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import no.nav.tps.forvalteren.domain.jpa.embedded.ChangeStamp;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "T_PERSONMAL")
public class Personmal extends ChangeStamp {
    private static final String SEQ = "T_PERSONMAL_SEQ";

    @Id
    @SequenceGenerator(name = SEQ, sequenceName = SEQ, allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = SEQ)
    @Column(name = "PERSONMAL_ID", nullable = false, updatable = false)
    private Long personmalId;

    @Column(name = "PERSONMAL_NAVN", nullable = false)
    private String personmalNavn;

    @Column(name = "PERSONMAL_BESKRIVELSE")
    private String personmalBeskrivelse;

    @Column(name = "FODT_ETTER")
    private String fodtEtter;

    @Column(name = "FODT_FOR")
    private String fodtFor;

    @Column(name = "KJONN")
    private String kjonn;

    @Column(name = "STATSBORGERSKAP")
    private String statsborgerskap;

    @Column(name = "SPESREG")
    private String spesreg;

    @Column(name = "SPESREG_DATO")
    private String spesregDato;

    @Column(name = "DOEDSDATO")
    private String doedsdato;

    @Column(name = "SIVILSTAND")
    private String sivilstand;

    @Column(name = "INNVANDRET_FRA_LAND")
    private String innvandretFraLand;

    @Column(name = "MIN_ANTALL_BARN")
    private int minAntallBarn;

    @Column(name = "MAX_ANTALL_BARN")
    private int maxAntallBarn;

    @Column(name = "GATEADRESSE")
    private String gateadresse;

    @Column(name = "GATE_HUSNUMMER")
    private String gateHusnr;

    @Column(name = "GATE_POSTNR")
    private String gatePostnr;

    @Column(name = "GATE_KOMMUNENR")
    private String gateKommunenr;

    @Column(name = "GATE_FLYTTEDATO_FRA")
    private String gateFlyttedatoFra;

    @Column(name = "GATE_FLYTTEDATO_TIL")
    private String gateFlyttedatoTil;

    @Column(name = "POST_LINJE1")
    private String postLinje1;

    @Column(name = "POST_LINJE2")
    private String postLinje2;

    @Column(name = "POST_LINJE3")
    private String postLinje3;

    @Column(name = "POST_LAND")
    private String postLand;

    @Column(name = "POST_GARDNR")
    private String postGardnr;

    @Column(name = "POST_BRUKSNR")
    private String postBruksnr;

    @Column(name = "POST_FESTENR")
    private String postFestenr;

    @Column(name = "POST_UNDERNR")
    private String postUndernr;

    @Column(name = "POST_POSTNR")
    private String postPostnr;

    @Column(name = "POST_KOMMUNENR")
    private String postKommunenr;

    @Column(name = "POST_FLYTTEDATO_FRA")
    private String postFlyttedatoFra;

    @Column(name = "POST_FLYTTEDATO_TIL")
    private String postFlyttedatoTil;
}
