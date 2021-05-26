package no.nav.tps.forvalteren.consumer.rs.adresser.command;

import java.util.concurrent.Callable;
import org.springframework.http.HttpHeaders;
import org.springframework.web.reactive.function.client.WebClient;

import lombok.RequiredArgsConstructor;
import no.nav.tps.forvalteren.domain.rs.VegadresseDTO;

@RequiredArgsConstructor
public class AdresseServiceCommand implements Callable<VegadresseDTO[]> {

    private static final String VEGADRESSE_URL = "/api/v1/adresser/veg";

    private final WebClient webClient;
    private final String query;
    private final Integer antall;
    private final String token;

    @Override
    public VegadresseDTO[] call() {

        return webClient.get()
                .uri(builder -> builder.path(VEGADRESSE_URL).query(query).build())
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                .header("antall", antall.toString())
                .retrieve()
                .bodyToMono(VegadresseDTO[].class)
                .block();
    }
}
