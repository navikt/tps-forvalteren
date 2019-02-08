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
public class RsMelding {

    private Long meldingNummer;
    private String meldingsType;
    private String systemkilde;
    private LocalDateTime tidspunkt;
    private String ident;
}
