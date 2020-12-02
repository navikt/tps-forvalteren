package no.nav.tps.forvalteren.domain.rs;

import java.time.LocalDateTime;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RsFullmakt {

    private Long id;

    private List<String> omraader;

    private String kilde;

    private LocalDateTime gyldigFom;

    private LocalDateTime gyldigTom;

    private RsSimplePerson fullmektig;
}
