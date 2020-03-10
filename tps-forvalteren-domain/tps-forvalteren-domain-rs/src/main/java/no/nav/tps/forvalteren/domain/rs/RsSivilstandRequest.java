package no.nav.tps.forvalteren.domain.rs;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import no.nav.tps.forvalteren.domain.jpa.Sivilstatus;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RsSivilstandRequest {

    private String sivilstand;
    private LocalDateTime sivilstandRegdato;

    public boolean isSivilstandGift() {

        switch (Sivilstatus.lookup(getSivilstand())) {
        case GIFT:
        case SEPARERT:
        case REGISTRERT_PARTNER:
        case SEPARERT_PARTNER:
            return true;
        }

        return false;
    }
}
