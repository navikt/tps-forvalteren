package no.nav.tps.vedlikehold.service.command.testdata;

import no.nav.tps.vedlikehold.domain.service.command.tps.testdata.SkdFeltDefinisjon;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by Peter Fløgstad on 09.01.2017.
 */

@Component
public class SkdMeldingFormatter {

    private static final String whitespace20 = "                    ";
    private static final String kjoreNummer = "000005000";
    private static final String kodeSystem = "SKD ";
    private static final String mqHandle = "000000000";
    private static final String skdRef = whitespace20;

    @Autowired
    private SkdFelterContainer skdFelterContainer;

    public String convertToSkdMeldingInnhold(Map<String, String> skdInputMap) {
        List<SkdFeltDefinisjon> skdFelter = skdFelterContainer.hentSkdFelter();
        skdFelter.forEach(skdFeltDefinisjon -> {
            skdFeltDefinisjon.setVerdi(skdInputMap.getOrDefault(skdFeltDefinisjon.getNokkelNavn(), null));
            addDefaultValueToEndOfString(skdFeltDefinisjon);
        });
        return skdFelter.stream()
                .sorted(Comparator.comparingInt(SkdFeltDefinisjon::getIdRekkefolge))
                .map(skdFeltDefinisjon -> skdFeltDefinisjon.getVerdi() == null ? skdFeltDefinisjon.getDefaultVerdi() : skdFeltDefinisjon.getVerdi())
                .collect(Collectors.joining());
    }

    public String createHeaderToSkdMelding(String skdMelding) {
        String headerSkdMelding = mqHandle + kodeSystem + kjoreNummer;
        String statusKode = extractStatusKode(skdMelding);
        String aasakskode = extractAArsakskode(skdMelding);
        String transType = extractTranstype(skdMelding);
        String tildelingsKode = getTildelingskode("0", aasakskode, statusKode);     //TODO Tildelingskode skal være 1 hvis personen ikke finnes fra før, og 2 hvis personen finnes fra før.
        headerSkdMelding = headerSkdMelding + aasakskode + transType + tildelingsKode + skdRef;
        return headerSkdMelding;
    }

    private void addDefaultValueToEndOfString(SkdFeltDefinisjon skdFeltDefinisjon) {
        if (skdFeltDefinisjon.getVerdi() == null) return;
        skdFeltDefinisjon.setVerdi(skdFeltDefinisjon.getVerdi() + skdFeltDefinisjon.getDefaultVerdi().substring(skdFeltDefinisjon.getVerdi().length()));
    }

    //TODO Spør spesefikt hvordan Tildelingskode bestemmes.
    public String getTildelingskode(String tildelingsKode, String aarskode, String statusKode) {
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

