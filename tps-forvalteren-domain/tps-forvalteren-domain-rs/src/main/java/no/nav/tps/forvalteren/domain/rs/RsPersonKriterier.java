package no.nav.tps.forvalteren.domain.rs;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Getter
@Setter
public class RsPersonKriterier {

    @NotNull
    private String identtype;

    private Character kjonn;

    private LocalDate foedtEtter;

    private LocalDate foedtFoer;

    private String fornavn;

    private String mellomnavn;

    private String etternavn;

    @NotNull
    @Min(1)
    @Max(99)
    private int antall;

}
