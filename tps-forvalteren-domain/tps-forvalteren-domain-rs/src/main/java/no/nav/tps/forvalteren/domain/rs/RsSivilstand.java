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
public class RsSivilstand {

    private String sivilstand;
    private LocalDateTime sivilstandRegdato;
}
