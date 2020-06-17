package no.nav.tps.forvalteren.service.command.testdata;

import java.util.HashMap;
import java.util.Map;
import org.springframework.stereotype.Component;

import no.nav.tps.forvalteren.domain.jpa.Person;

@Component
public class TpsServiceroutineFnrRequest {
    
    public Map buildRequest(String ident, String environment) {
        Map<String, Object> params = new HashMap<>();
        params.put("fnr", ident);
        params.put("aksjonsKode", "B0");
        params.put("environment", environment);
        return params;
    }

    public Map buildRequest(Person person, String environment) {
        return buildRequest(person.getIdent(), environment);
    }
}
