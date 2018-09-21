package no.nav.tps.forvalteren.service.command.endringsmeldinger.response;

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
public class StatusPaaAvspiltSkdMelding {
    private String foedselsnummer;
    private Long sekvensnummer;
    private String status;
}
