package no.nav.tps.forvalteren.domain.rs.kodeverk;

import java.time.LocalDate;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

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
