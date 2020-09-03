package no.nav.tps.forvalteren.consumer.rs.environments;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FetchEnvironmentsApplication {

    private String id;
    private String application;
    private String environment;
    private String version;
}