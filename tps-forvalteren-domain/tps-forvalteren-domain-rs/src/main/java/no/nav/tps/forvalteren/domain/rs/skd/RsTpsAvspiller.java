package no.nav.tps.forvalteren.domain.rs.skd;

import static java.util.Objects.isNull;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import no.nav.tps.forvalteren.domain.jpa.TpsAvspillerProgress;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RsTpsAvspiller {

    private Long bestillingId;
    private LocalDateTime periodeFra;
    private LocalDateTime periodeTil;
    private String kildeKoe;
    private String utloepKoe;
    private String format;
    private String request;
    private Long antall;
    private LocalDateTime tidspunkt;
    private boolean ferdig;
    private boolean avbrutt;
    private Integer progressAntall;
    private Map<Long, TpsAvspillerProgress> progressMap;

    public Map<Long, TpsAvspillerProgress> getProgressMap() {
        if (isNull(progressMap)) {
            progressMap = new HashMap();
        }
        return progressMap;
    }
}
