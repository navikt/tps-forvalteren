package no.nav.tps.forvalteren.consumer.rs.token;

import java.util.Arrays;
import java.util.List;

public class AccessScopes {

    private final List<String> scopes;

    public AccessScopes(String... scopes) {
        this.scopes = Arrays.asList(scopes);
    }

    public List<String> getScopes() {
        return scopes;
    }
}
