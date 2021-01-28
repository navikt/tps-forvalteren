package no.nav.tps.forvalteren.domain.rs;

import static java.util.Objects.isNull;
import static org.apache.commons.lang3.BooleanUtils.isTrue;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import no.nav.tps.forvalteren.domain.rs.skd.KjoennType;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RsSimplePersonRequest {

    protected String ident;

    @NotBlank
    @Size(min = 3, max = 3)
    protected String identtype;

    protected KjoennType kjonn;

    protected Integer alder;

    protected LocalDateTime foedtEtter;

    protected LocalDateTime foedtFoer;

    protected String sprakKode;

    protected LocalDateTime datoSprak;

    protected String statsborgerskap;

    protected LocalDateTime statsborgerskapRegdato;

    protected LocalDateTime statsborgerskapTildato;

    protected String spesreg;

    protected LocalDateTime spesregDato;

    protected LocalDateTime egenAnsattDatoFom;

    protected LocalDateTime egenAnsattDatoTom;

    protected boolean utenFastBopel;

    protected RsRequestAdresse boadresse;

    protected List<RsPostadresse> postadresse;

    protected RsMidlertidigAdresseRequest midlertidigAdresse;

    private String utvandretTilLand;

    private LocalDateTime utvandretTilLandFlyttedato;

    protected Boolean harMellomnavn;

    private String innvandretFraLand;

    private LocalDateTime innvandretFraLandFlyttedato;

    private Boolean erForsvunnet;

    private LocalDateTime forsvunnetDato;

    private LocalDateTime doedsdato;

    private List<RsIdenthistorikkKriterium> identHistorikk;

    private Boolean harBankkontonr;

    private LocalDateTime bankkontonrRegdato;

    private String telefonLandskode_1;

    private String telefonnummer_1;

    private String telefonLandskode_2;

    private String telefonnummer_2;

    public List<RsPostadresse> getPostadresse() {
        if (isNull(postadresse)) {
            postadresse = new ArrayList();
        }
        return postadresse;
    }

    public List<RsIdenthistorikkKriterium> getIdentHistorikk() {
        if (isNull(identHistorikk)) {
            identHistorikk = new ArrayList();
        }
        return identHistorikk;
    }

    @JsonIgnore
    public boolean isKode6() {
        return "SPSF".equals(getSpesreg());
    }

    @JsonIgnore
    public boolean isMann() {
        return KjoennType.M == getKjonn();
    }

    @JsonIgnore
    public boolean isKvinne() {
        return KjoennType.K == getKjonn();
    }

    @JsonIgnore
    public boolean hasSpesificKjoenn() {
        return isKvinne() || isMann();
    }

    @JsonIgnore
    public boolean hasMellomnavn() {
        return isTrue(getHarMellomnavn());
    }
}