package no.nav.tps.forvalteren.skdavspilleren.service.requests;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class OpprettAvspillergruppeRequest {
    
    private String navn;
    private String beskrivelse;
    private String opprettetAv;
}
