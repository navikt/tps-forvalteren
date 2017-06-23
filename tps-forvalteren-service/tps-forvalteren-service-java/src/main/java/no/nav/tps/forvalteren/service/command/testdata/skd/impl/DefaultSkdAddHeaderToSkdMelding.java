package no.nav.tps.forvalteren.service.command.testdata.skd.impl;

import no.nav.tps.forvalteren.service.command.testdata.skd.SkdAddHeaderToSkdMelding;
import org.springframework.stereotype.Service;

@Service
public class DefaultSkdAddHeaderToSkdMelding implements SkdAddHeaderToSkdMelding {

    private static final String whitespace20 = "                    ";
    private static final String kjoreNummer = "000005000";
    private static final String kodeSystem = "SKD ";
    private static final String mqHandle = "000000000";
    private static final String skdRef = whitespace20;

    public void execute(StringBuilder skdMelding) {
        String headerSkdMelding = mqHandle + kodeSystem + kjoreNummer;
        String statusKode = extractStatusKode(skdMelding.toString());
        String aasakskode = extractAArsakskode(skdMelding.toString());
        String transType = extractTranstype(skdMelding.toString());
        String tildelingsKode = getTildelingskode("0", aasakskode, statusKode);     //TODO Tildelingskode skal være 1 hvis personen ikke finnes fra før, og 2 hvis personen finnes fra før.
        headerSkdMelding = headerSkdMelding + aasakskode + transType + tildelingsKode + skdRef;
        skdMelding.reverse().append(headerSkdMelding).reverse();
    }

    //TODO Spør spesefikt hvordan Tildelingskode bestemmes.
    private String getTildelingskode(String tildelingsKode, String aarskode, String statusKode) {
        if (tildelingsKode.equals(" ")) tildelingsKode = "0";
        if (aarskode.equals("01") && statusKode.equals("7")) tildelingsKode = "8";
        if (aarskode.equals("01") && statusKode.equals(" ")) tildelingsKode = "9";
        if (aarskode.equals("84") && statusKode.equals("7")) tildelingsKode = "8";
        return tildelingsKode;
    }

    //TODO Kanskje gjøre noe med hvis man bare får 1 char av en eller nanen grunn.
    private String extractAArsakskode(String skdMelding) {
        String aarskode =  skdMelding.substring(26, 28);
        if(aarskode.equals("")) return "  ";
        return aarskode;
    }

    private String extractTranstype(String skdMelding) {
        String transType = skdMelding.substring(25, 26);
        if (transType.equals("")) return " ";
        return transType;
    }

    private String extractStatusKode(String skdMelding) {
        String statusKode = skdMelding.substring(36, 37);
        if (statusKode.equals("")) return " ";
        return statusKode;
    }

}
