package no.nav.tps.forvalteren.domain.rs.dolly;

import static java.util.Objects.isNull;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.Size;
import org.hibernate.validator.constraints.NotBlank;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import no.nav.tps.forvalteren.domain.rs.RsAdresse;
import no.nav.tps.forvalteren.domain.rs.RsPostadresse;
import no.nav.tps.forvalteren.domain.rs.RsSimpleRelasjoner;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RsPersonBestillingKriteriumRequest {

    private Set<String> environments;

    private List<String> opprettFraIdenter;

    @NotBlank
    @Size(min = 3, max = 3)
    private String identtype;

    private LocalDateTime foedtEtter;

    private LocalDateTime foedtFoer;

    @Min(1)
    @Max(99)
    private Integer antall;

    private RsSimpleRelasjoner relasjoner;

    private String kjonn;

    private String statsborgerskap;

    private LocalDateTime statsborgerskapRegdato;

    @Size(min = 4, max = 4)
    private String spesreg;

    private String sivilstand;

    private LocalDateTime spesregDato;

    private LocalDateTime doedsdato;

    private RsAdresse boadresse;

    private List<RsPostadresse> postadresse;

    private LocalDateTime regdato;

    private LocalDateTime egenAnsattDatoFom;

    private LocalDateTime egenAnsattDatoTom;

    @Size(min = 4, max = 4)
    private String typeSikkerhetsTiltak;

    private LocalDateTime sikkerhetsTiltakDatoFom;

    private LocalDateTime sikkerhetsTiltakDatoTom;

    @Size(min = 1, max = 50)
    private String beskrSikkerhetsTiltak;

    @Size(min = 2, max = 2)
    private String sprakKode;

    private LocalDateTime datoSprak;

    private boolean utenFastBopel;

    public List<String> getOpprettFraIdenter() {
        if (isNull(opprettFraIdenter)) {
            opprettFraIdenter = new ArrayList();
        }
        return opprettFraIdenter;
    }

    public RsSimpleRelasjoner getRelasjoner() {
        if (relasjoner == null) {
            relasjoner = new RsSimpleRelasjoner();
        }
        return relasjoner;
    }
}