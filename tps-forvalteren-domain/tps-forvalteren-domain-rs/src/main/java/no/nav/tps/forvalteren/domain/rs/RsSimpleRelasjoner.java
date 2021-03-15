package no.nav.tps.forvalteren.domain.rs;

import static java.util.Objects.isNull;

import java.util.ArrayList;
import java.util.List;

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
public class RsSimpleRelasjoner {

    private RsPartnerRequest partner;
    private List<RsPartnerRequest> partnere;

    private List<RsBarnRequest> barn;
    private List<RsForeldreRequest> foreldre;

    public List<RsPartnerRequest> getPartnere() {
        if (isNull(partnere)) {
            partnere = new ArrayList();
        }
        return partnere;
    }

    public List<RsBarnRequest> getBarn() {
        if (isNull(barn)) {
            barn = new ArrayList();
        }
        return barn;
    }

    public List<RsForeldreRequest> getForeldre() {
        if (isNull(foreldre)) {
            foreldre = new ArrayList<>();
        }
        return foreldre;
    }
}