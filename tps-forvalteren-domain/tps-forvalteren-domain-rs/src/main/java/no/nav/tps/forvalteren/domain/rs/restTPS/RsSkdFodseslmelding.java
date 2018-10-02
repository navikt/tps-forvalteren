package no.nav.tps.forvalteren.domain.rs.restTPS;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class RsSkdFodseslmelding {

    private String morFodselsnumemr;

    private LocalDate fodseldato;

    private String kjonn;

    private String fodeKommuneNrEllerLand;

    // 1=levendefodt   2=Doedfodt
    private int levendeDod;

    private String environment;
}
