package no.nav.tps.forvalteren.domain.rs.skd.embedded;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ForeldreFelter {

    private String morsFodselsdato;

    private String morsPersonnummer;

    private String morsNavn;

    private String morsStatsbSkap;

    private String farsFodselsdato;

    private String farsPersonnummer;

    private String farsFarsNavn;

    private String farsStatsbSkap;

}
