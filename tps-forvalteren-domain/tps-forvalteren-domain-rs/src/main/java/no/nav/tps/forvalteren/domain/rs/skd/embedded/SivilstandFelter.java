package no.nav.tps.forvalteren.domain.rs.skd.embedded;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SivilstandFelter {

    private String sivilstand;

    private String regdatoSivilstand;

    private String ektefellePartnerFdato;

    private String ektefellePartnerPnr;

    private String ektefellePartnerNavn;

    private String ektefellePartnerStatsb;

}
