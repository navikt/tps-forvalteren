package no.nav.tps.forvalteren.domain.rs.dolly;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import org.hibernate.validator.constraints.NotBlank;

import lombok.Getter;
import lombok.Setter;
import no.nav.tps.forvalteren.domain.rs.RsAdresse;
import no.nav.tps.forvalteren.domain.rs.RsPostadresse;
import no.nav.tps.forvalteren.domain.rs.RsSimpleRelasjoner;

@Getter
@Setter
public class RsPersonBestillingKriteriumRequest {

    private List<String> environments;

    @NotBlank
    @Size(min = 3, max = 3)
    private String identtype;

    private LocalDate foedtEtter;

    private LocalDate foedtFoer;

    @NotNull
    @Min(1)
    @Max(99)
    private int antall;

    private RsSimpleRelasjoner relasjoner;

    private boolean withAdresse;

    @NotBlank
    @Size(min = 3, max = 3)
    private String kjonn;

    private String statsborgerskap;

    @Size(min = 1, max = 1)
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
}