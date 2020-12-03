package no.nav.tps.forvalteren.domain.rs.dolly;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RsFullmaktRequest {

    private LocalDateTime gyldigFom;

    private LocalDateTime gyldigTom;

    private String kilde;

    private List<String> omraader;

    private String identType;

    private Boolean harMellomnavn;
}
