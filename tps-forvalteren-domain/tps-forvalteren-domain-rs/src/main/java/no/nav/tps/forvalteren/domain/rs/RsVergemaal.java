package no.nav.tps.forvalteren.domain.rs;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RsVergemaal {

    private Long id;

    private String embete;

    private String sakType;

    private LocalDateTime vedtakDato;

    private RsSimplePerson verge;

    private String mandatType;
}
