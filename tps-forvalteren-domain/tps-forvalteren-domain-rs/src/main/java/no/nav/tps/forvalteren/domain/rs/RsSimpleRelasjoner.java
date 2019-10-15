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

    private List<RsPartnerRequest> partner;

    private List<RsSimplePersonRequest> barn;

    public List<RsPartnerRequest> getPartner() {
        if (isNull(partner)) {
            partner = new ArrayList();
        }
        return partner;
    }

    public List<RsSimplePersonRequest> getBarn() {
        if (isNull(barn)) {
            barn = new ArrayList();
        }
        return barn;
    }
}