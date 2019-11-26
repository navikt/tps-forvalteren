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
public class RsTpsStatusPaaIdenterResponse {
    
    private List<TpsStatusPaaIdent> statusPaaIdenter;

    public List<TpsStatusPaaIdent> getStatusPaaIdenter() {
        if (isNull(statusPaaIdenter)) {
            statusPaaIdenter = new ArrayList();
        }
        return statusPaaIdenter;
    }
}
