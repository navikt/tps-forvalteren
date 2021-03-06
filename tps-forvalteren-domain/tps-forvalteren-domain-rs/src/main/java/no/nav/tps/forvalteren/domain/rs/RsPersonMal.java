package no.nav.tps.forvalteren.domain.rs;

import java.time.LocalDate;
import java.time.LocalDateTime;
import javax.validation.constraints.Max;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RsPersonMal {

    @NotNull
    private Long id;

    @NotNull
    private String bruker;

    @NotNull
    private LocalDateTime opprettetDato;

    @NotBlank
    private String personmalNavn;

    private String personmalBeskrivelse;

    @Size(min = 1, max = 8)
    private LocalDate fodtEtter;

    @Size(min = 1, max = 8)
    private LocalDate fodtFor;

    @Size(max = 1)
    private String kjonn;

    @Size(min = 1, max = 3)
    private String statsborgerskap;

    @Size(max = 1)
    private String spesreg;

    @Size(min = 1, max = 8)
    private String spesregDato;

    @Size(max = 8)
    private String doedsdato;

    @Size(min = 1, max = 4)
    private String sivilstand;

    @Size(max = 3)
    private String innvandretFraLand;

    @Size(max = 2)
    private String minAntallBarn;

    @Size(max = 2)
    private String maxAntallBarn;

    @Size(max = 50)
    private String adresse;

    @Size(max = 4)
    private String gateHusnr;

    @Size(max = 4)
    private String gatePostnr;

    @Size(max = 4)
    private String gateKommunenr;

    @Size(max = 8)
    private String gateFlyttedatoFra;

    @Size(max = 8)
    private String gateFlyttedatoTil;

    @Size(max = 30)
    private String postLinje1;

    @Size(max = 30)
    private String postLinje2;

    @Size(max = 30)
    private String postLinje3;

    @Size(max = 3)
    private String postLand;

    @Size(max = 5)
    private String postGardnr;

    @Size(max = 4)
    private String postBruksnr;

    @Size(max = 4)
    private String postFestenr;

    @Size(max = 3)
    private String postUndernr;

    @Size(max = 4)
    private String postPostnr;

    @Size(max = 4)
    private String postKommunenr;

    @Size(max = 8)
    private String postFlyttedatoFra;

    @Size(max = 8)
    private String postFlyttedatoTil;

    @Max(99)
    private Integer antallIdenter;

    @Size(max = 3)
    private String identType;
}
