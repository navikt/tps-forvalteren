package no.nav.tps.forvalteren.domain.rs.skd;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RsSkdEndringsmeldingLogg {

    private String raw;

    private String beskrivelse;

    private String miljoe;

    private LocalDateTime utfoertDato;

    private String utfoertAv;
}
