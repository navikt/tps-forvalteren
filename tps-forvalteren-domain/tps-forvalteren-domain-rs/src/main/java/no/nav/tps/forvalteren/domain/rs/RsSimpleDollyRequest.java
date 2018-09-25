package no.nav.tps.forvalteren.domain.rs;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class RsSimpleDollyRequest {

    private String identtype;

    private String kjonn;

    private LocalDate foedtEtter;

    private LocalDate foedtFoer;

    private int antall;

}
