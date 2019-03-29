package no.nav.tps.forvalteren.domain.rs;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RsSimplePersonRequest {

    private String identtype;

    private String kjonn;

    private LocalDateTime foedtEtter;

    private LocalDateTime foedtFoer;

    private String sprakKode;

    private LocalDateTime datoSprak;

    private String statsborgerskap;

    private LocalDateTime statsborgerskapRegdato;

    private String spesreg;

    private LocalDateTime spesregDato;

    private LocalDateTime egenAnsattDatoFom;

    private LocalDateTime egenAnsattDatoTom;

    private boolean utenFastBopel;

    private RsAdresse boadresse;
}
