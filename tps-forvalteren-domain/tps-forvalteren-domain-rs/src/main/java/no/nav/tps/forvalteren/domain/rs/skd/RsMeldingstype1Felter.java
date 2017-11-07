package no.nav.tps.forvalteren.domain.rs.skd;

import com.fasterxml.jackson.annotation.JsonTypeName;
import com.fasterxml.jackson.annotation.JsonUnwrapped;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import no.nav.tps.forvalteren.domain.rs.skd.embedded.AdresseFelter;
import no.nav.tps.forvalteren.domain.rs.skd.embedded.DnrFelter;
import no.nav.tps.forvalteren.domain.rs.skd.embedded.ForeldreFelter;
import no.nav.tps.forvalteren.domain.rs.skd.embedded.HeaderFelter;
import no.nav.tps.forvalteren.domain.rs.skd.embedded.NavnFelter;
import no.nav.tps.forvalteren.domain.rs.skd.embedded.SivilstandFelter;
import no.nav.tps.forvalteren.domain.rs.skd.embedded.StatsborgerskapFelter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonTypeName("T1")
public class RsMeldingstype1Felter extends RsMeldingstype {

    @JsonUnwrapped
    private HeaderFelter headerFelter = new HeaderFelter();

    private String datoDoed;

    @JsonUnwrapped
    private NavnFelter navnFelter = new NavnFelter();

    private String foedekommLand;

    private String foedested;

    private String familienummer;

    private String regdatoFamnr;

    private String personkode;

    private String spesRegType;

    private String datoSpesRegType;

    @JsonUnwrapped
    private SivilstandFelter sivilstandFelter = new SivilstandFelter();

    @JsonUnwrapped
    private AdresseFelter adresseFelter = new AdresseFelter();

    private String samemanntall;

    private String datoSamemanntall;

    private String umyndiggjort;

    private String datoUmyndiggjort;

    private String foreldreansvar;

    private String datoForeldreansvar;

    private String arbeidstillatelse;

    private String datoArbeidstillatelse;

    private String fremkonnummer;

    @JsonUnwrapped
    private ForeldreFelter foreldreFelter = new ForeldreFelter();

    private String tidligereFnrDnr;

    private String datoTidlFnrDnr;

    private String nyttFnr;

    private String datoNyttFnr;

    private String levendeDoed;

    private String kjonn;

    private String tildelingskode;

    private String foedselstype;

    private String morsSiviltilstand;

    private String ekteskPartnskNr;

    private String ektfEkteskPartnskNr;

    private String vigselstype;

    private String forsByrde;

    private String dombevilling;

    private String antallBarn;

    private String tidlSivilstand;

    private String ektfTidlSivilstand;

    private String hjemmel;

    private String fylke;

    private String vigselskomm;

    private String tidlSepDomBev;

    private String begjertAv;

    private String registrGrunnlag;

    private String doedssted;

    private String typeDoedssted;

    private String vigselsdato;

    private String medlKirken;

    private String sekvensnr;

    private String bolignr;

    private String dufId;

    private String brukerident;

    private String skolerets;

    private String tkNr;

    @JsonUnwrapped
    private DnrFelter dnrFelter = new DnrFelter();

    private String utvandringstype;

    private String grunnkrets;

    @JsonUnwrapped
    private StatsborgerskapFelter statsborgerskapFelter = new StatsborgerskapFelter();

    private String saksid;

    private String embete;

    private String sakstype;

    private String vedtaksdato;

    private String internVergeid;

    private String vergeFnrDnr;

    private String vergetype;

    private String mandattype;

    private String mandatTekst;

    private String reserverFramtidigBruk;

}