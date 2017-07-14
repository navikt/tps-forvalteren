package no.nav.tps.forvalteren.domain.ws.kodeverk;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;


@Setter
@Getter
public class Kode {

    private String navn;

    private String term;

    private LocalDate fom;

    private LocalDate tom;

    private String uri;

    private Kodeverk kodeverk;
}
