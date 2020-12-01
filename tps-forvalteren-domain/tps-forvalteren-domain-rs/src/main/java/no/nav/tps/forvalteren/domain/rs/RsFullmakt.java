package no.nav.tps.forvalteren.domain.rs;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import no.nav.tps.forvalteren.domain.jpa.Person;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RsFullmakt {

    private Person person;

    private List<String> omraader;

    private String kilde;

    private LocalDateTime gyldigFom;

    private LocalDateTime gyldigTom;

    private Person fullmektig;

}
