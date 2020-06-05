package no.nav.tps.forvalteren.consumer.rs.kodeverk;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class KodeverkResponse {

    private Map<String, List<Innhold>> betydninger;

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Innhold {

        private LocalDate gyldigFra;
        private LocalDate gyldigTil;
        private Beskrivelser beskrivelser;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Beskrivelser {

        private Termer norsk;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Termer {

        private String term;
        private String tekst;
    }
}
