package no.nav.tps.forvalteren.service;

import static java.util.Arrays.asList;

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
}