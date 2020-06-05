package no.nav.tps.forvalteren.domain.rs.kodeverk;
import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Kode {

    private String navn;

    private String term;

    private LocalDate fom;

    private LocalDate tom;

    private String uri;
}
