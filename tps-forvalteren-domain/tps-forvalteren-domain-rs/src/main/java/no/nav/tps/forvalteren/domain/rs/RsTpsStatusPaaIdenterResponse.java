package no.nav.tps.forvalteren.domain.rs;

import java.util.List;
import java.util.stream.Collectors;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class RsTpsStatusPaaIdenterResponse {
    private List<TpsStatusPaaIdent> statusPaaIdenter;
    
    public void addEnvToTheseIdents(String env, List<String> idents) {
        List<TpsStatusPaaIdent> tpsStatusPaaIdentList = findTpsStatusPaaIdentObject(idents);
        tpsStatusPaaIdentList.forEach(tpsStatusPaaIdent -> tpsStatusPaaIdent.addEnv(env));
    }
    
    public List<TpsStatusPaaIdent> findTpsStatusPaaIdentObject(List<String> identer) {
        return statusPaaIdenter.stream()
                .filter(tpsStatusPaaIdent -> identer.contains(tpsStatusPaaIdent.getIdent()))
                .collect(Collectors.toList());
    }
}
