package no.nav.tps.forvalteren.domain.rs;

import static java.util.Objects.isNull;

import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RsPartnerRequest extends RsSimplePersonRequest{

    private Integer partnerNr;
    private List<RsSivilstandRequest> sivilstander;
    private Boolean harFellesAdresse;

    public List<RsSivilstandRequest> getSivilstander() {
        if (isNull(sivilstander)) {
            sivilstander = new ArrayList();
        }
        return sivilstander;
    }
}