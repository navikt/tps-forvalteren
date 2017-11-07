package no.nav.tps.forvalteren.domain.rs.skd.embedded;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AdresseFelter {

    private String regdatoAdr;

    private String postadrRegDato;

    private String flyttedatoAdr;

    private String kommunenummer;

    private String gateGaard;

    private String husBruk;

    private String bokstavFestenr;

    private String undernr;

    private String adressenavn;

    private String adressetype;

    private String tilleggsadresse;

    private String postnummer;

    private String valgkrets;

    private String adresse1;

    private String adresse2;

    private String adresse3;

    private String postadrLand;

    private String innvandretFraLand;

    private String fraLandRegdato;

    private String fraLandFlyttedato;

    private String fraKommune;

    private String fraKommRegdato;

    private String fraKommFlyttedato;

    private String utvandretTilLand;

    private String tilLandRegdato;

    private String tilLandFlyttedato;

}
