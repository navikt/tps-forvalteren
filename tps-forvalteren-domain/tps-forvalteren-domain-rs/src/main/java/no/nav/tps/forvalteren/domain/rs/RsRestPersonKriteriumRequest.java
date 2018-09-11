package no.nav.tps.forvalteren.domain.rs;

import com.sun.istack.internal.Nullable;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import org.hibernate.validator.constraints.NotBlank;

@Getter
@Setter
public class RsRestPersonKriteriumRequest {

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
    private Character kjonn;

    private String statsborgerskap;

    @Size(min = 1, max = 1)
    private String spesreg;

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
}
