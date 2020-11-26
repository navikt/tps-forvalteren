package no.nav.tps.forvalteren.domain.rs.dolly;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RsFullmaktRequest {

    private LocalDate gyldigFom;

    private LocalDate gyldigTom;

    private String kilde;

    private List<String> omraader;

    private String identType;

    private Boolean harMellomnavn;
}
