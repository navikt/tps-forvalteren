package no.nav.tps.forvalteren.service;

import static java.util.Arrays.asList;
import static org.assertj.core.util.Sets.newHashSet;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import no.nav.tps.forvalteren.consumer.rs.identpool.IdentpoolConsumer;

@Service
public class IdentpoolService {

    @Autowired
    private IdentpoolConsumer identpoolConsumer;

    public Set<String> getWhitedlistedIdents() {

        ResponseEntity<String[]> response = identpoolConsumer.getWhitelistedIdent();
        return new HashSet<>(asList(response.getBody()));
    }

    public Set<String> whitelistAjustmentOfIdents(Collection<String> requestedIdenter, Set<String> availableInDB, Set<String> availableInEnvironment) {

        Set<String> adjustedIdents = newHashSet(availableInEnvironment);

        getWhitedlistedIdents().forEach(ident -> {
            if (requestedIdenter.contains(ident) && !availableInDB.contains(ident)) {
                adjustedIdents.add(ident);
            }
        });
        return adjustedIdents;
    }
}