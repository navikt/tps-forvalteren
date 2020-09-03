package no.nav.tps.forvalteren.domain.ws.fasit;

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
public class QueueManager {

    private String name;
    private String hostname;
    private String port;
    private String channel;
}