package no.nav.tps.forvalteren.domain.rs.dolly;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RsVergemaalRequest {

    private String embete;

    private String sakType;

    private LocalDateTime vedtakDato;

    private String mandatType;

    private String identType;

    private Boolean harMellomnavn;
}
