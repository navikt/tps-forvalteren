package no.nav.tps.forvalteren.domain.rs.skd.embedded;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class NavnFelter {

    private String slektsnavn;

    private String fornavn;

    private String mellomnavn;

    private String slekstnavnUgift;

    private String forkortetNavn;

    private String regDatoNavn;

}
