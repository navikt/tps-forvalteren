package no.nav.tps.forvalteren.consumer.rs.identpool;

import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class IdentPoolClient {
    
    @Value("${identpool.url}")
    private String BASE_URL;
    
    public List<String> hentNyeIdenter(HentIdenterRequest hentIdenterRequest) {
        RestTemplate restTemplate = new RestTemplate();
        return restTemplate.postForObject(BASE_URL + "/api/v1/identifikator", hentIdenterRequest, ArrayList.class);
    }
}
