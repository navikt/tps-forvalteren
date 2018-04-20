package no.nav.tps.forvalteren.service.command.testdata.opprett;

import lombok.Getter;

@Getter
public enum LandKode {
    ARGENTINA   ("705", "ARG"),
    AUSTRALIA   ("805", "AUS"),
    TYSKLAND    ("144", "DEU"),
    LATVIA      ("124", "LAT"),
    DANMARK     ("101", "DAN"),
    ENGLAND     ("139", "GBR"),
    POLEN       ("131", "POL"),
    PORTUGAL    ("132", "PRT"),
    SOMALIA     ("346", "SOM"),
    SVERIGE     ("106", "SWE");

    private final String landkodesiffer;
    private final String landkode;

    LandKode(String landkodesiffer, String landkode){
        this.landkodesiffer = landkodesiffer;
        this.landkode = landkode;
    }


}
