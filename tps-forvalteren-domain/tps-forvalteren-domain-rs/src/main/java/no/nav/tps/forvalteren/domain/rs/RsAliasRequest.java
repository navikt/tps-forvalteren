package no.nav.tps.forvalteren.domain.rs;

import static java.util.Objects.isNull;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RsAliasRequest {

    public enum IdentType {DNR, FNR}

    private Set<String> environments;
    private String ident;
    private List<AliasSpesification> aliaser;

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class AliasSpesification {

        private Boolean nyIdent;
        private IdentType identtype;
    }

    public Set<String> getEnvironments() {
        if (isNull(environments)) {
            environments = new HashSet();
        }
        return environments;
    }

    public List<AliasSpesification> getAliaser() {
        if (isNull(aliaser)) {
            aliaser = new ArrayList();
        }
        return aliaser;
    }
}
