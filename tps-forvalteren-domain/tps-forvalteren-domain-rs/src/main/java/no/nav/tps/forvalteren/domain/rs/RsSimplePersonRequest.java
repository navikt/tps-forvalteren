package no.nav.tps.forvalteren.domain.rs;

import java.time.LocalDate;
import java.time.LocalDateTime;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RsSimplePersonRequest {

    private String identtype;

    private String kjonn;

    private LocalDate foedtEtter;

    private LocalDate foedtFoer;

    private String sprakKode;

    private LocalDateTime datoSprak;

    private String statsborgerskap;

    private LocalDateTime statsborgerskapRegdato;

    private String spesreg;

    private LocalDateTime spesregDato;
}
