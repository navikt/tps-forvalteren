package no.nav.tps.forvalteren.domain.ws.kodeverk;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
public class Kodeverk {

    private String uri;

    private String navn;

    private LocalDate fom;

    private LocalDate tom;

    private int versjon;

    private List<Kode> koder;

}
