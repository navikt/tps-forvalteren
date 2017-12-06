package no.nav.tps.forvalteren.service.command.tps.skdmelding.skdparam.utils;

import org.springframework.stereotype.Component;

@Component
public class HusbokstavEncoder {

    public String encode(String husbokstav) {
        switch (husbokstav) {
        case "A":
            return "9901";
        case "B":
            return "9902";
        case "C":
            return "9903";
        case "D":
            return "9904";
        case "E":
            return "9905";
        case "F":
            return "9906";
        case "G":
            return "9907";
        case "H":
            return "9908";
        case "I":
            return "9909";
        case "J":
            return "9910";
        case "K":
            return "9911";
        case "L":
            return "9912";
        case "M":
            return "9913";
        case "N":
            return "9914";
        case "O":
            return "9915";
        case "P":
            return "9916";
        case "Q":
            return "9917";
        case "R":
            return "9918";
        case "S":
            return "9919";
        case "T":
            return "9920";
        case "U":
            return "9921";
        case "V":
            return "9922";
        case "W":
            return "9923";
        case "X":
            return "9924";
        case "Y":
            return "9925";
        case "Z":
            return "9926";
        case "Æ":
            return "9927";
        case "Ø":
            return "9928";
        case "Å":
            return "9929";
        case "Á":
            return "9930";
        default:
            throw new IllegalArgumentException("Husbokstaven: " + husbokstav + " er ikke en gyldig husbokstav.");
        }

    }
}
