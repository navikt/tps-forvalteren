package no.nav.tps.forvalteren.domain.rs;

import java.time.LocalDate;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import org.hibernate.validator.constraints.NotBlank;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RsPersonKriterier {

    @NotBlank
    @Size(min = 3, max = 3)
    private String identtype;

    private String kjonn;

    private LocalDate foedtEtter;

    private LocalDate foedtFoer;

    private String fornavn;

    private String mellomnavn;

    private String etternavn;

    private String sivilstand;

    private String innvandretFraLand;

    @NotNull
    @Min(1)
    @Max(99)
    private int antall;

}
