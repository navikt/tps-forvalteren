package no.nav.tps.forvalteren.domain.rs.skd.embedded;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class HeaderFelter {

    private String fodselsdato;

    private String personnummer;

    private String maskindato;

    private String maskintid;

    private String transtype;

    private String aarsakskode;

    private String regDato;

    private String statuskode;

}
