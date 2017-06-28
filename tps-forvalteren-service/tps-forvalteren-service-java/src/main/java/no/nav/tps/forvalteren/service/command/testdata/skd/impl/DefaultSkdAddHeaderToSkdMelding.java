package no.nav.tps.forvalteren.service.command.testdata.skd.impl;

import no.nav.tps.forvalteren.service.command.testdata.skd.SkdAddHeaderToSkdMelding;
import org.springframework.stereotype.Service;

@Service
public class DefaultSkdAddHeaderToSkdMelding implements SkdAddHeaderToSkdMelding {

    private static final String whitespace20 = "                    ";
    //private static final String KJORE_NUMMER_AKA_SYSTEM_IDENT = "000005000";
    private static final String KJORE_NUMMER_AKA_SYSTEM_IDENT = "000004421";
    private static final String KODE_SYSTEM = "SKD ";
    private static final String MQ_HANDLE = "000000000";
    private static final String SKD_REFERANSE = whitespace20;

    private static final int INDEX_START_TILDELINGSKODE = 873;
    private static final int INDEX_SLUTT_TILDELINGSKODE = 874;

    private static final int INDEX_START_AARSAKSKODE = 26;
    private static final int INDEX_SLUTT_AARSAKSKODE = 28;

    private static final int INDEX_START_TRANSTYPE = 25;
    private static final int INDEX_SLUTT_TRANSTYPE = 26;

    private static final int INDEX_START_STATUSKODE = 36;
    private static final int INDEX_SLUTT_STATUSKODE = 37;

    public void execute(StringBuilder skdMelding) {
        String headerSkdMelding = MQ_HANDLE + KODE_SYSTEM + KJORE_NUMMER_AKA_SYSTEM_IDENT;
        String aasakskode = extractAArsakskode(skdMelding.toString());
        String transType = extractTranstype(skdMelding.toString());
        String tildelingsKode = extractTildelingskode(skdMelding.toString());
        headerSkdMelding = headerSkdMelding + aasakskode + transType + tildelingsKode + SKD_REFERANSE;
        skdMelding
                .reverse()
                .append(new StringBuilder(headerSkdMelding).reverse().toString())
                .reverse();
    }

    private String extractAArsakskode(String skdMelding) {
        return skdMelding.substring(INDEX_START_AARSAKSKODE,INDEX_SLUTT_AARSAKSKODE);
    }

    private String extractTranstype(String skdMelding) {
        return skdMelding.substring(INDEX_START_TRANSTYPE, INDEX_SLUTT_TRANSTYPE);
    }

    private String extractTildelingskode(String skdMelding){
        return skdMelding.substring(INDEX_START_TILDELINGSKODE,INDEX_SLUTT_TILDELINGSKODE);
    }

    private String extractStatusKode(String skdMelding) {
        String statusKode = skdMelding.substring(INDEX_START_STATUSKODE, INDEX_SLUTT_STATUSKODE);
        if (statusKode.equals("")) {
            return " ";
        }
        return statusKode;
    }

    //TODO Tildelingskode skal være 1 hvis personen ikke finnes fra før, og 2 hvis personen finnes fra før.??

    //TODO Spør spesefikt hvordan Tildelingskode bestemmes.
//    private String extractTildelingskode(String tildelingsKode, String aarskode, String statusKode) {
//        if (tildelingsKode.equals(" ")) tildelingsKode = "0";
//        if (aarskode.equals("01") && statusKode.equals("7")) tildelingsKode = "8";
//        if (aarskode.equals("01") && statusKode.equals(" ")) tildelingsKode = "9";
//        if (aarskode.equals("84") && statusKode.equals("7")) tildelingsKode = "8";
//        return tildelingsKode;
//    }

}
