package no.nav.tps.forvalteren.domain.rs;

import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TpsStatusPaaIdent {
    
    private String ident;
    private List<String> env;
    
    public void addEnv(String environment) {
        getEnv().add(environment);
    }
    
    public List<String> getEnv() {
        if (env == null) {
            env = new ArrayList<>();
        }
        return env;
    }
}
