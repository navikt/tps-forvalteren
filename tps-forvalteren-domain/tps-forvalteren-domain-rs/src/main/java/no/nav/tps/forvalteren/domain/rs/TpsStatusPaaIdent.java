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
    
    public void add(String environment) {
        getEnv().add(environment);
    }
    
    public List<String> getEnv() {
        return env== null ? new ArrayList<>() : env;
    }
}
