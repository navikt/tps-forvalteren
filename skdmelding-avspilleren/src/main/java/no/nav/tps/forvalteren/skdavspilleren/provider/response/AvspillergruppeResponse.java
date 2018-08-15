package no.nav.tps.forvalteren.skdavspilleren.provider.response;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AvspillergruppeResponse {
    
    private Long id;
    private String navn;
    private String beskrivelse;
    private LocalDateTime opprettetDato;
    private String opprettetAv;
    private LocalDateTime endretDato;
    private String endretAv;
}
