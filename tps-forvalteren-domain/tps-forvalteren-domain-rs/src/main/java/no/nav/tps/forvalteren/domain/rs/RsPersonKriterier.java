package no.nav.tps.forvalteren.domain.rs;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RsPersonKriterier {

    @NotBlank
    @Size(min = 3, max = 3)
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
