package no.nav.tps.forvalteren.common.java.util;

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
    private Integer port;
    private String channel;
}