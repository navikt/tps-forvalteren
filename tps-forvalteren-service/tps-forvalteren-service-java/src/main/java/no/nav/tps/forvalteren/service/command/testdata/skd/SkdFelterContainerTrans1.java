package no.nav.tps.forvalteren.service.command.testdata.skd;

import static no.nav.tps.forvalteren.domain.service.tps.config.SkdConstants.AARSAKSKODE;
import static no.nav.tps.forvalteren.domain.service.tps.config.SkdConstants.DOEDSDATO;
import static no.nav.tps.forvalteren.domain.service.tps.config.SkdConstants.EKTEFELLE_PARTNER_FODSELSDATO;
import static no.nav.tps.forvalteren.domain.service.tps.config.SkdConstants.EKTEFELLE_PARTNER_PERSONNUMMMER;
import static no.nav.tps.forvalteren.domain.service.tps.config.SkdConstants.FLYTTEDATO_ADR;
import static no.nav.tps.forvalteren.domain.service.tps.config.SkdConstants.FODSELSDATO;
import static no.nav.tps.forvalteren.domain.service.tps.config.SkdConstants.FORNAVN;
import static no.nav.tps.forvalteren.domain.service.tps.config.SkdConstants.FRA_LAND_FLYTTEDATO;
import static no.nav.tps.forvalteren.domain.service.tps.config.SkdConstants.FRA_LAND_REGDATO;
import static no.nav.tps.forvalteren.domain.service.tps.config.SkdConstants.MASKINDATO;
import static no.nav.tps.forvalteren.domain.service.tps.config.SkdConstants.MASKINTID;
import static no.nav.tps.forvalteren.domain.service.tps.config.SkdConstants.MELLOMNAVN;
import static no.nav.tps.forvalteren.domain.service.tps.config.SkdConstants.PERSONNUMMER;
import static no.nav.tps.forvalteren.domain.service.tps.config.SkdConstants.REGDATO_SIVILSTAND;
import static no.nav.tps.forvalteren.domain.service.tps.config.SkdConstants.REG_DATO;
import static no.nav.tps.forvalteren.domain.service.tps.config.SkdConstants.REG_DATO_ADR;
import static no.nav.tps.forvalteren.domain.service.tps.config.SkdConstants.REG_DATO_FAM_NR;
import static no.nav.tps.forvalteren.domain.service.tps.config.SkdConstants.SIVILSTAND;
import static no.nav.tps.forvalteren.domain.service.tps.config.SkdConstants.SLEKTSNAVN;
import static no.nav.tps.forvalteren.domain.service.tps.config.SkdConstants.STATSBORGERSKAP;
import static no.nav.tps.forvalteren.domain.service.tps.config.SkdConstants.STATUSKODE;
import static no.nav.tps.forvalteren.domain.service.tps.config.SkdConstants.TILDELINGSKODE;
import static no.nav.tps.forvalteren.domain.service.tps.config.SkdConstants.TRANSTYPE;

import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class SkdFelterContainerTrans1 implements SkdFelterContainer {

    private static final String WHITESPACE_5_STK = "     ";
    private static final String WHITESPACE_10_STK = WHITESPACE_5_STK + WHITESPACE_5_STK;
    private static final String WHITESPACE_25_STK = WHITESPACE_5_STK + WHITESPACE_10_STK + WHITESPACE_10_STK;
    private static final String WHITESPACE_40_STK = WHITESPACE_10_STK + WHITESPACE_10_STK + WHITESPACE_10_STK + WHITESPACE_10_STK;
    private static final String WHITESPACE_30_STK = WHITESPACE_5_STK + WHITESPACE_25_STK;
    private static final String WHITESPACE_50_STK = WHITESPACE_25_STK + WHITESPACE_25_STK;
    private static final String WHITESPACE_100_STK = WHITESPACE_50_STK + WHITESPACE_50_STK;

    public List<SkdFeltDefinisjon> hentSkdFelter() {
        ArrayList<SkdFeltDefinisjon> skdFelter = new ArrayList<>();

        leggTilSkdFeltDefinisjonerDel1(skdFelter);
        leggTilSkdFeltDefinisjonerDel2(skdFelter);
        leggTilSkdFeltDefinisjonerDel3(skdFelter);
        leggTilSkdFeltDefinisjonerDel4(skdFelter);

        return skdFelter;
    }

    /* Delt opp definisjon i flere deler grunnet Sonar klager paa metodetorrelse */

    private void leggTilSkdFeltDefinisjonerDel1(ArrayList<SkdFeltDefinisjon> skdFelter) {

        skdFelter.add(new SkdFeltDefinisjon(FODSELSDATO, "000000", 1, 6, 1, 6));
        skdFelter.add(new SkdFeltDefinisjon(PERSONNUMMER, "00000", 2, 5, 7, 11));
        skdFelter.add(new SkdFeltDefinisjon(MASKINDATO, "00000000", 3, 8, 12, 19));
        skdFelter.add(new SkdFeltDefinisjon(MASKINTID, "000000", 4, 6, 20, 25));
        skdFelter.add(new SkdFeltDefinisjon(TRANSTYPE, "0", 5, 1, 26, 26));
        skdFelter.add(new SkdFeltDefinisjon(AARSAKSKODE, "00", 6, 2, 27, 28));
        skdFelter.add(new SkdFeltDefinisjon(REG_DATO, "00000000", 7, 8, 29, 36));
        skdFelter.add(new SkdFeltDefinisjon(STATUSKODE, " ", 8, 1, 37, 37));
        skdFelter.add(new SkdFeltDefinisjon(DOEDSDATO, "00000000", 9, 8, 38, 45));
        skdFelter.add(new SkdFeltDefinisjon(SLEKTSNAVN, WHITESPACE_50_STK, 10, 50, 46, 95));
        skdFelter.add(new SkdFeltDefinisjon(FORNAVN, WHITESPACE_50_STK, 11, 50, 96, 145));
        skdFelter.add(new SkdFeltDefinisjon(MELLOMNAVN, WHITESPACE_50_STK, 12, 50, 146, 195));
        skdFelter.add(new SkdFeltDefinisjon("slekstnavnUgiftT", WHITESPACE_50_STK, 13, 50, 196, 245));
        skdFelter.add(new SkdFeltDefinisjon("forkortetNavn", WHITESPACE_25_STK, 14, 25, 246, 270));
        skdFelter.add(new SkdFeltDefinisjon("regDatoNavn", "00000000", 15, 8, 271, 278));
        skdFelter.add(new SkdFeltDefinisjon("foedekommLand", "    ", 16, 4, 279, 282));
        skdFelter.add(new SkdFeltDefinisjon("foedested", "                    ", 17, 20, 283, 302));
        skdFelter.add(new SkdFeltDefinisjon(STATSBORGERSKAP, "000", 18, 3, 303, 305));
        skdFelter.add(new SkdFeltDefinisjon("regdatoStatsb", "00000000", 19, 8, 306, 313));
        skdFelter.add(new SkdFeltDefinisjon("familienummer", "00000000000", 20, 11, 314, 324));
        skdFelter.add(new SkdFeltDefinisjon(REG_DATO_FAM_NR, "00000000", 21, 8, 325, 332));
        skdFelter.add(new SkdFeltDefinisjon("personkode", "0", 22, 1, 333, 333));
        skdFelter.add(new SkdFeltDefinisjon("spesRegType", "0", 23, 1, 334, 334));
        skdFelter.add(new SkdFeltDefinisjon("datoSpesRegType", "00000000", 24, 8, 335, 342));
        skdFelter.add(new SkdFeltDefinisjon(SIVILSTAND, "0", 25, 1, 343, 343));
        skdFelter.add(new SkdFeltDefinisjon(REGDATO_SIVILSTAND, "00000000", 26, 8, 344, 351));
    }

    private void leggTilSkdFeltDefinisjonerDel2(ArrayList<SkdFeltDefinisjon> skdFelter) {
        skdFelter.add(new SkdFeltDefinisjon(EKTEFELLE_PARTNER_FODSELSDATO, "000000", 28, 6, 352, 357));
        skdFelter.add(new SkdFeltDefinisjon(EKTEFELLE_PARTNER_PERSONNUMMMER, "00000", 29, 5, 358, 362));
        skdFelter.add(new SkdFeltDefinisjon("ektefellePartnerNavn", WHITESPACE_50_STK, 30, 50, 363, 412));
        skdFelter.add(new SkdFeltDefinisjon("ektefellePartnerStatsb", "000", 31, 3, 413, 415));

        skdFelter.add(new SkdFeltDefinisjon(REG_DATO_ADR, "00000000", 33, 8, 416, 423));
        skdFelter.add(new SkdFeltDefinisjon(FLYTTEDATO_ADR, "00000000", 34, 8, 424, 431));
        skdFelter.add(new SkdFeltDefinisjon("kommunenummer", "0000", 35, 4, 432, 435));
        skdFelter.add(new SkdFeltDefinisjon("gateGaard", "00000", 36, 5, 436, 440));
        skdFelter.add(new SkdFeltDefinisjon("husBruk", "0000", 37, 4, 441, 444, true));
        skdFelter.add(new SkdFeltDefinisjon("bokstavFestenr", "0000", 38, 4, 445, 448));
        skdFelter.add(new SkdFeltDefinisjon("undernr", "000", 39, 3, 449, 451));
        skdFelter.add(new SkdFeltDefinisjon("adressenavn", WHITESPACE_25_STK, 40, 25, 452, 476));
        skdFelter.add(new SkdFeltDefinisjon("adressetype", " ", 41, 1, 477, 477));
        skdFelter.add(new SkdFeltDefinisjon("tilleggsadresse", WHITESPACE_25_STK, 42, 25, 478, 502));
        skdFelter.add(new SkdFeltDefinisjon("postnummer", "0000", 43, 4, 503, 506));
        skdFelter.add(new SkdFeltDefinisjon("valgkrets", "0000", 44, 4, 507, 510));

        skdFelter.add(new SkdFeltDefinisjon("adresse1", WHITESPACE_30_STK, 46, 30, 511, 540));
        skdFelter.add(new SkdFeltDefinisjon("adresse2", WHITESPACE_30_STK, 47, 30, 541, 570));
        skdFelter.add(new SkdFeltDefinisjon("adresse3", WHITESPACE_30_STK, 48, 30, 571, 600));
        skdFelter.add(new SkdFeltDefinisjon("postadrLand", "000", 49, 3, 601, 603));
        skdFelter.add(new SkdFeltDefinisjon("innvandretFraLand", "000", 50, 3, 604, 606));
        skdFelter.add(new SkdFeltDefinisjon(FRA_LAND_REGDATO, "00000000", 51, 8, 607, 614));
        skdFelter.add(new SkdFeltDefinisjon(FRA_LAND_FLYTTEDATO, "00000000", 52, 8, 615, 622));
        skdFelter.add(new SkdFeltDefinisjon("fraKommune", "0000", 53, 4, 623, 626));
        skdFelter.add(new SkdFeltDefinisjon("fraKommRegdato", "00000000", 54, 8, 627, 634));
        skdFelter.add(new SkdFeltDefinisjon("fraKommFlyttedato", "00000000", 55, 8, 635, 642));
        skdFelter.add(new SkdFeltDefinisjon("utvandretTilLand", "000", 56, 3, 643, 645));
        skdFelter.add(new SkdFeltDefinisjon("tilLandRegdato", "00000000", 57, 8, 646, 653));
        skdFelter.add(new SkdFeltDefinisjon("tilLandFlyttedato", "00000000", 58, 8,654,661));
        skdFelter.add(new SkdFeltDefinisjon("samemanntall", " ", 59, 1, 662, 662));
        skdFelter.add(new SkdFeltDefinisjon("datoSamemanntall", "00000000", 60, 8, 663, 670));
        skdFelter.add(new SkdFeltDefinisjon("umyndiggjort", " ", 61, 1, 671, 671));
        skdFelter.add(new SkdFeltDefinisjon("datoUmyndiggjort", "00000000", 62, 8, 672, 679));
        skdFelter.add(new SkdFeltDefinisjon("foreldreansvar", " ", 63, 1, 680, 680));
        skdFelter.add(new SkdFeltDefinisjon("datoForeldreansvar", "00000000", 64, 8, 681, 688));
        skdFelter.add(new SkdFeltDefinisjon("arbeidstillatelse", " ", 65, 1, 689, 689));
        skdFelter.add(new SkdFeltDefinisjon("datoArbeidstillatelse", "00000000", 66, 8, 690, 697));
        skdFelter.add(new SkdFeltDefinisjon("fremkonnummer", "00000000", 67, 8, 698, 705));
    }

    private void leggTilSkdFeltDefinisjonerDel3(ArrayList<SkdFeltDefinisjon> skdFelter) {

        skdFelter.add(new SkdFeltDefinisjon("morsFodselsdato", "000000", 69, 6, 706, 711));
        skdFelter.add(new SkdFeltDefinisjon("morsPersonnummer", "00000", 70, 5, 712, 716));
        skdFelter.add(new SkdFeltDefinisjon("morsNavn", WHITESPACE_50_STK, 71, 50, 717, 766));
        skdFelter.add(new SkdFeltDefinisjon("morsStatsbSkap", "000", 72, 3, 767, 769));
        skdFelter.add(new SkdFeltDefinisjon("farsFodselsdato", "000000", 74, 6, 770, 775));
        skdFelter.add(new SkdFeltDefinisjon("farsPersonnummer", "00000", 75, 5, 776, 780));
        skdFelter.add(new SkdFeltDefinisjon("farsFarsNavn", WHITESPACE_50_STK, 76, 50, 781, 830));
        skdFelter.add(new SkdFeltDefinisjon("farsStatsbSkap", "000", 77, 3, 831, 833));
        skdFelter.add(new SkdFeltDefinisjon("tidligereFnrDnr", "00000000000", 78, 11, 834, 844));
        skdFelter.add(new SkdFeltDefinisjon("datoTidlFnrDnr", "00000000", 79, 8, 845, 852));
        skdFelter.add(new SkdFeltDefinisjon("nyttFnr", "00000000000", 80, 11, 853, 863));
        skdFelter.add(new SkdFeltDefinisjon("datoNyttFnr", "00000000", 81, 8, 864, 871));

        skdFelter.add(new SkdFeltDefinisjon("levendeDoed", " ", 82, 1, 872, 872));
        skdFelter.add(new SkdFeltDefinisjon("kjonn", " ", 83, 1, 873, 873));
        skdFelter.add(new SkdFeltDefinisjon(TILDELINGSKODE, " ", 84, 1, 874, 874));
        skdFelter.add(new SkdFeltDefinisjon("foedselstype", "  ", 85, 2, 875, 876));
        skdFelter.add(new SkdFeltDefinisjon("morsSiviltilstand", " ", 86, 1, 877, 877));
        skdFelter.add(new SkdFeltDefinisjon("ekteskPartnskNr", " ", 87, 1, 878, 878));
        skdFelter.add(new SkdFeltDefinisjon("ektfEkteskPartnskNr", " ", 88, 1, 879, 879));
        skdFelter.add(new SkdFeltDefinisjon("vigselstype", " ", 89, 1, 880, 880));
        skdFelter.add(new SkdFeltDefinisjon("forsByrde", "  ", 90, 2, 881, 882));
        skdFelter.add(new SkdFeltDefinisjon("dombevilling", " ", 91, 1, 883, 883));
        skdFelter.add(new SkdFeltDefinisjon("antallBarn", "  ", 92, 2, 884, 885));
        skdFelter.add(new SkdFeltDefinisjon("tidlSivilstand", " ", 93, 1, 886, 886));
        skdFelter.add(new SkdFeltDefinisjon("ektfTidlSivilstand", " ", 94, 1, 887, 887));
        skdFelter.add(new SkdFeltDefinisjon("hjemmel", " ", 95, 1, 888, 888));
        skdFelter.add(new SkdFeltDefinisjon("fylke", "  ", 96, 2, 889, 890));
        skdFelter.add(new SkdFeltDefinisjon("vigselskomm", "0000", 97, 4, 891, 894));
        skdFelter.add(new SkdFeltDefinisjon("tidlSepDomBev", " ", 98, 1, 895, 895));
        skdFelter.add(new SkdFeltDefinisjon("begjertAv", " ", 99, 1, 896, 896));
        skdFelter.add(new SkdFeltDefinisjon("registrGrunnlag", " ", 100, 1, 897, 897));
        skdFelter.add(new SkdFeltDefinisjon("doedssted", "    ", 101, 4, 898, 901));
        skdFelter.add(new SkdFeltDefinisjon("typeDoedssted", " ", 102, 1, 902, 902));
        skdFelter.add(new SkdFeltDefinisjon("vigselsdato", "00000000", 103, 8, 903, 910));
        skdFelter.add(new SkdFeltDefinisjon("medlKirken", " ", 104, 1, 911, 911));
        skdFelter.add(new SkdFeltDefinisjon("sekvensnr", "000000", 105, 6, 912, 917));
        skdFelter.add(new SkdFeltDefinisjon("bolignr", "     ", 106, 5, 918, 922));
        skdFelter.add(new SkdFeltDefinisjon("dufId", "000000000000", 107, 12, 923, 934));
    }

    private void leggTilSkdFeltDefinisjonerDel4(ArrayList<SkdFeltDefinisjon> skdFelter) {

        skdFelter.add(new SkdFeltDefinisjon("brukerident", "        ", 108, 8, 935, 942));
        skdFelter.add(new SkdFeltDefinisjon("skolerets", "0000", 109, 4, 943, 946));
        skdFelter.add(new SkdFeltDefinisjon("tkNr", "0000", 110, 4, 947, 950));
        skdFelter.add(new SkdFeltDefinisjon("dnrHjemlandsadresse1", WHITESPACE_40_STK, 111, 40, 951, 990));
        skdFelter.add(new SkdFeltDefinisjon("dnrHjemlandsadresse2", WHITESPACE_40_STK, 112, 40, 991, 1030));
        skdFelter.add(new SkdFeltDefinisjon("dnrHjemlandsadresse3", WHITESPACE_40_STK, 113, 40, 1031, 1070));
        skdFelter.add(new SkdFeltDefinisjon("dnrHjemlandLandkode", "000", 114, 3, 1071, 1073));
        skdFelter.add(new SkdFeltDefinisjon("dnrHjemlandRegDato", "00000000", 115, 8, 1074, 1081));
        skdFelter.add(new SkdFeltDefinisjon("dnrIdKontroll", " ", 116, 1, 1082, 1082));
        skdFelter.add(new SkdFeltDefinisjon("postadrRegDato", "00000000", 117, 8, 1083, 1090));
        skdFelter.add(new SkdFeltDefinisjon("utvandringstype", " ", 118, 1, 1091, 1091));
        skdFelter.add(new SkdFeltDefinisjon("grunnkrets", "0000", 119, 4, 1092, 1095));
        skdFelter.add(new SkdFeltDefinisjon("statsborgerskap2", "000", 120, 3, 1096, 1098));
        skdFelter.add(new SkdFeltDefinisjon("regdatoStatsb2", "00000000", 121, 8, 1099, 1106));
        skdFelter.add(new SkdFeltDefinisjon("statsborgerskap3", "000", 122, 3, 1107, 1109));
        skdFelter.add(new SkdFeltDefinisjon("regdatoStatsb3", "00000000", 123, 8, 1110, 1117));
        skdFelter.add(new SkdFeltDefinisjon("statsborgerskap4", "000", 124, 3, 1118, 1120));
        skdFelter.add(new SkdFeltDefinisjon("regdatoStatsb4", "00000000", 125, 8, 1121, 1128));
        skdFelter.add(new SkdFeltDefinisjon("statsborgerskap5", "000", 126, 3, 1129, 1131));
        skdFelter.add(new SkdFeltDefinisjon("regdatoStatsb5", "00000000", 127, 8,1132, 1139));
        skdFelter.add(new SkdFeltDefinisjon("statsborgerskap6", "000", 128, 3, 1140, 1142));
        skdFelter.add(new SkdFeltDefinisjon("regdatoStatsb6", "00000000", 129, 8, 1143, 1150));
        skdFelter.add(new SkdFeltDefinisjon("statsborgerskap7", "000", 130, 3, 1151, 1153));
        skdFelter.add(new SkdFeltDefinisjon("regdatoStatsb7", "00000000", 131, 8, 1154, 1161));
        skdFelter.add(new SkdFeltDefinisjon("statsborgerskap8", "000", 132, 3, 1162,1164));
        skdFelter.add(new SkdFeltDefinisjon("regdatoStatsb8", "00000000", 132, 8,1165, 1172));
        skdFelter.add(new SkdFeltDefinisjon("statsborgerskap9", "000", 133, 3, 1173, 1175));
        skdFelter.add(new SkdFeltDefinisjon("regdatoStatsb9", "00000000", 134, 8, 1176, 1183));
        skdFelter.add(new SkdFeltDefinisjon("statsborgerskap10", "000", 135, 3, 1184, 1186));
        skdFelter.add(new SkdFeltDefinisjon("regdatoStatsb10", "00000000", 136, 8, 1187, 1194));
        skdFelter.add(new SkdFeltDefinisjon("bibehold", " ", 137, 1, 1195, 1195));
        skdFelter.add(new SkdFeltDefinisjon("regdatoBibehold", "00000000", 138, 8, 1196, 1203));
        skdFelter.add(new SkdFeltDefinisjon("saksid", "0000000", 139, 7, 1204, 1210));
        skdFelter.add(new SkdFeltDefinisjon("embete", "    ", 140, 4, 1211, 1214));
        skdFelter.add(new SkdFeltDefinisjon("sakstype", "   ", 141, 3, 1215, 1217));
        skdFelter.add(new SkdFeltDefinisjon("vedtaksdato", "        ", 142, 8, 1218, 1225));
        skdFelter.add(new SkdFeltDefinisjon("internVergeid", "0000000", 143, 7, 1226, 1232));
        skdFelter.add(new SkdFeltDefinisjon("vergeFnrDnr", "00000000000", 144, 11, 1233, 1243));
        skdFelter.add(new SkdFeltDefinisjon("vergetype", "   ", 145, 3, 1244, 1246));
        skdFelter.add(new SkdFeltDefinisjon("mandattype", "   ", 146, 3, 1247, 1249));
        skdFelter.add(new SkdFeltDefinisjon("mandatTekst", WHITESPACE_100_STK, 147, 100, 1250, 1349));
        skdFelter.add(new SkdFeltDefinisjon("reserverFramtidigBruk", WHITESPACE_100_STK + WHITESPACE_50_STK + " ", 148, 151));
    }
}
