package no.nav.tps.forvalteren.domain.rs;

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
@AllArgsConstructor
@NoArgsConstructor
public class TpsStatusPaaIdent {
    
    private String ident;
    private List<String> env;

    public List<String> getEnv() {
        if (env == null) {
            env = new ArrayList<>();
        }
        return env;
    }
}
