package no.nav.tps.forvalteren.consumer.rs.identpool;

import java.net.URI;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;


public class IdentpoolConsumer {

    private static final String IDENT_WHITELIST_URL = "/api/v1/identifikator/whitelist";

    @Value("${identpool.host}")
    private String identpoolHost;

    @Autowired
    private RestTemplate restTemplate;

    public ResponseEntity getWhitelistedIdent() {

        return restTemplate.exchange(RequestEntity.get(
                URI.create(identpoolHost + IDENT_WHITELIST_URL))
                .build(), String[].class);
    }
}
