package no.nav.tps.forvalteren.domain.rs;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RsGateadresse {

    private Long id;
    private String boGateadresse;
    private String boHusnummer;
    private String boGatekode;
    private String boPostnummer;
    private String boKommunenr;
    private LocalDateTime boFlytteDato;

}
