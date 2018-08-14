package no.nav.tps.forvalteren.skdavspilleren.service.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AvspiltSkdMeldingTilTpsRespons {
    private Long sekvensnummer;
    private String status;
}
