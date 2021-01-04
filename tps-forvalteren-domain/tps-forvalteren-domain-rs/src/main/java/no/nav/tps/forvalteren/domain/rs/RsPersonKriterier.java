package no.nav.tps.forvalteren.domain.rs;

import java.time.LocalDateTime;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import no.nav.tps.forvalteren.domain.rs.skd.KjoennType;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RsPersonKriterier {

    @NotNull
    @NotBlank
    @Size(min = 3, max = 3)
    private String identtype;

    private KjoennType kjonn;

    private LocalDateTime foedtEtter;

    private LocalDateTime foedtFoer;

    @NotNull
    @Min(1)
    @Max(99)
    private Integer antall;

    private Boolean harMellomnavn;

    @Override public String toString() {
        return "RsPersonKriterier{" +
                "identtype='" + identtype + '\'' +
                ", kjonn=" + kjonn +
                ", foedtEtter=" + foedtEtter +
                ", foedtFoer=" + foedtFoer +
                ", antall=" + antall +
                '}';
    }
}
