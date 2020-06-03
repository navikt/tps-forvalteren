package no.nav.tps.forvalteren.domain.jpa;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;
import static javax.persistence.CascadeType.ALL;
import static no.nav.tps.forvalteren.domain.jpa.InnvandretUtvandret.InnUtvandret.INNVANDRET;
import static no.nav.tps.forvalteren.domain.jpa.InnvandretUtvandret.InnUtvandret.UTVANDRET;
import static org.apache.commons.lang3.BooleanUtils.isTrue;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import no.nav.tps.forvalteren.domain.jpa.embedded.ChangeStamp;

@Entity
@Getter
@Setter
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "T_PERSON")
public class Person extends ChangeStamp {

    private static final String SEQ = "T_PERSON_SEQ";

    @Id
    @SequenceGenerator(name = SEQ, sequenceName = SEQ, allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = SEQ)
    @Column(name = "PERSON_ID", nullable = false, updatable = false)
    private Long id;

    @Column(name = "IDENT", nullable = false, unique = true, length = 11)
    private String ident;

    @Column(name = "IDENTTYPE", nullable = false)
    private String identtype;

    @Column(name = "KJONN", nullable = false)
    private String kjonn;

    @Column(name = "FORNAVN", length = 50)
    private String fornavn;

    @Column(name = "MELLOMNAVN", length = 50)
    private String mellomnavn;

    @Column(name = "ETTERNAVN", length = 50)
    private String etternavn;

    @Column(name = "FORKORTET_NAVN")
    private String forkortetNavn;

    @OrderBy("id desc")
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "person", cascade = ALL)
    private List<Statsborgerskap> statsborgerskap;

    @Column(name = "SPESREG", length = 1)
    private String spesreg;

    @Column(name = "SPESREG_DATO")
    private LocalDateTime spesregDato;

    @Column(name = "DOEDSDATO")
    private LocalDateTime doedsdato;

    @Column(name = "SIVILSTAND", length = 4)
    private String sivilstand;

    @OrderBy("sivilstandRegdato desc, id desc")
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "person", cascade = ALL)
    private List<Sivilstand> sivilstander;

    @Column(name = "SIVILSTAND_REGDATO")
    private LocalDateTime sivilstandRegdato;

    @OrderBy("flyttedato desc, id desc")
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "person", cascade = ALL)
    private List<InnvandretUtvandret> innvandretUtvandret;

    @Column(name = "EGEN_ANSATT_DATO_FOM")
    private LocalDateTime egenAnsattDatoFom;

    @Column(name = "EGEN_ANSATT_DATO_TOM")
    private LocalDateTime egenAnsattDatoTom;

    @Column(name = "TYPE_SIKKERHETSTILTAK", length = 4)
    private String typeSikkerhetsTiltak;

    @Column(name = "SIKKERHETSTILTAK_DATO_FOM")
    private LocalDateTime sikkerhetsTiltakDatoFom;

    @Column(name = "SIKKERHETSTILTAK_DATO_TOM")
    private LocalDateTime sikkerhetsTiltakDatoTom;

    @Column(name = "BESKR_SIKKERHETSTILTAK", length = 50)
    private String beskrSikkerhetsTiltak;

    @OrderBy("id desc")
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "person", cascade = ALL)
    private List<Adresse> boadresse;

    @OrderBy("id desc")
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "person", cascade = ALL)
    private List<Postadresse> postadresse;

    @Column(name = "REGDATO")
    private LocalDateTime regdato;

    @JoinColumn(name = "GRUPPE_ID")
    @ManyToOne(fetch = FetchType.LAZY)
    private Gruppe gruppe;

    @OrderBy("relasjonTypeNavn desc, id desc")
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "person", cascade = ALL)
    private List<Relasjon> relasjoner;

    @OrderBy("historicIdentOrder desc")
    @OneToMany(fetch = FetchType.EAGER, mappedBy = "person", cascade = ALL)
    private List<IdentHistorikk> identHistorikk;

    @OrderBy("id desc")
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "person", cascade = ALL)
    private List<MidlertidigAdresse> midlertidigAdresses;

    @Column(name = "OPPRETTET_DATO")
    private LocalDateTime opprettetDato;

    @Column(name = "OPPRETTET_AV")
    private String opprettetAv;

    @Column(name = "SPRAK_KODE")
    private String sprakKode;

    @Column(name = "DATO_SPRAK")
    private LocalDateTime datoSprak;

    @Column(name = "TKNR")
    private String tknr;

    @Column(name = "TKNAVN")
    private String tknavn;

    @Column(name = "GT_TYPE")
    private String gtType;

    @Column(name = "GT_VERDI")
    private String gtVerdi;

    @Column(name = "GT_REGEL")
    private String gtRegel;

    @Column(name = "UTEN_FAST_BOPEL")
    private Boolean utenFastBopel;

    @Column(name = "PERSON_STATUS")
    private String personStatus;

    @Column(name = "FORSVUNNET_DATO")
    private LocalDateTime forsvunnetDato;

    @Column(name = "BANKKONTONR")
    private String bankkontonr;

    @Column(name = "BANKKONTONR_REGDATO")
    private LocalDateTime bankkontonrRegdato;

    @Size(max = 6)
    @Column(name = "TELEFON_LANDSKODE_1")
    private String telefonLandskode_1;

    @Column(name = "TELEFONNUMMER_1")
    private String telefonnummer_1;

    @Column(name = "TELEFON_LANDSKODE_2")
    private String telefonLandskode_2;

    @Column(name = "TELEFONNUMMER_2")
    private String telefonnummer_2;

    @Transient
    private String replacedByIdent;

    @Transient
    private LocalDateTime aliasRegdato;

    public List<Postadresse> getPostadresse() {
        if (isNull(postadresse)) {
            postadresse = new ArrayList();
        }
        return postadresse;
    }

    public List<Relasjon> getRelasjoner() {
        if (isNull(relasjoner)) {
            relasjoner = new ArrayList();
        }
        return relasjoner;
    }

    public List<IdentHistorikk> getIdentHistorikk() {
        if (isNull(identHistorikk)) {
            identHistorikk = new ArrayList();
        }
        return identHistorikk;
    }

    public List<Sivilstand> getSivilstander() {
        if (isNull(sivilstander)) {
            sivilstander = new ArrayList();
        }
        return sivilstander;
    }

    public List<Adresse> getBoadresse() {
        if (isNull(boadresse)) {
            boadresse = new ArrayList();
        }
        return boadresse;
    }

    public List<Statsborgerskap> getStatsborgerskap() {
        if (isNull(statsborgerskap)) {
            statsborgerskap = new ArrayList();
        }
        return statsborgerskap;
    }

    public List<InnvandretUtvandret> getInnvandretUtvandret() {
        if (isNull(innvandretUtvandret)) {
            innvandretUtvandret = new ArrayList();
        }
        return innvandretUtvandret;
    }

    public String getLandkodeOfFirstInnvandret() {

        return getInnvandretUtvandret().stream()
                .filter(innutvandret -> INNVANDRET == innutvandret.getInnutvandret())
                .map(InnvandretUtvandret::getLandkode)
                .reduce((a, b) -> b).orElse(null);
    }

    public LocalDateTime getFlyttedatoOfFirstInnvandret() {

        return getInnvandretUtvandret().stream()
                .filter(innutvandret -> INNVANDRET == innutvandret.getInnutvandret())
                .map(InnvandretUtvandret::getFlyttedato)
                .reduce((a, b) -> b).orElse(null);
    }

    public Person toUppercase() {
        if (isNotBlank(getFornavn())) {
            setFornavn(getFornavn().toUpperCase());
        }
        if (isNotBlank(getMellomnavn())) {
            setMellomnavn(getMellomnavn().toUpperCase());
        }
        if (isNotBlank(getEtternavn())) {
            setEtternavn(getEtternavn().toUpperCase());
        }
        if (isNotBlank(getForkortetNavn())) {
            setForkortetNavn(getForkortetNavn().toUpperCase());
        }
        if (isNotBlank(getIdenttype())) {
            setIdenttype(getIdenttype().toUpperCase());
        }

        getBoadresse().forEach(Adresse::toUppercase);
        getPostadresse().forEach(Postadresse::toUppercase);

        return this;
    }

    public boolean isUtenFastBopel() {
        return isTrue(utenFastBopel) || "UFB".equals(getSpesreg());
    }

    public boolean isKode6() {
        return "SPSF".equals(getSpesreg());
    }

    public boolean isForsvunnet() {
        return nonNull(getForsvunnetDato());
    }

    public boolean isEgenansatt() {
        return nonNull(getEgenAnsattDatoFom()) && isNull(getEgenAnsattDatoTom());
    }

    public boolean isUtvandret() {
        return !getInnvandretUtvandret().isEmpty() &&
                UTVANDRET == getInnvandretUtvandret().get(0).getInnutvandret();
    }

    public boolean isDoedFoedt() {
        return "FDAT".equals(getIdenttype());
    }
}