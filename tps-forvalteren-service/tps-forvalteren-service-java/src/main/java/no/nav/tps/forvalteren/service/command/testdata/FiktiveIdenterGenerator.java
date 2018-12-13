package no.nav.tps.forvalteren.service.command.testdata;

import static java.lang.Character.getNumericValue;
import static java.lang.String.format;
import static java.util.Collections.emptyList;
import static no.nav.tps.forvalteren.service.command.tps.skdmelding.skdparam.utils.NullcheckUtil.nullcheckSetDefaultValue;

import java.security.SecureRandom;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.springframework.stereotype.Service;

import no.nav.tps.forvalteren.domain.rs.RsPersonKriterier;
import no.nav.tps.forvalteren.service.command.testdata.utils.BiasedRandom;
import no.nav.tps.forvalteren.service.command.testdata.utils.DateGenerator;

@Service
public class FiktiveIdenterGenerator {

    private static final String DNR = "DNR";
    private static final String FNR = "FNR";
    private static final String BNR = "BNR";
    private static final int MULTIPLY_ANT_IDENTER = 2;
    private static final LocalDateTime DEFAULT_FODT_FOER_DATE = LocalDate.now().atStartOfDay();
    private static final LocalDateTime DEFAULT_FODT_ETTER_DATE = DEFAULT_FODT_FOER_DATE.minusYears(100);

    //Starter på 1 fordi individ nummer "000" er reservert for F-DAT nummer. Spesielt nummer.
    private static final int CATEGORY1_NUMBER_RANGE_START = 1;
    private static final int CATEGORY1_NUMBER_RANGE_END = 499;
    private static final int CATEGORY1_TIME_PERIOD_START = 1900;
    private static final int CATEGORY1_TIME_PERIOD_END = 1999;

    private static final int CATEGORY2_NUMBER_RANGE_START = 500;
    private static final int CATEGORY2_NUMBER_RANGE_END = 749;
    private static final int CATEGORY2_TIME_PERIOD_START = 1854;
    private static final int CATEGORY2_TIME_PERIOD_END = 1899;

    private static final int CATEGORY_3_NUMBER_RANGE_START = 500;
    private static final int CATEGORY_3_NUMBER_RANGE_END = 999;
    private static final int CATEGORY_3_TIME_PERIOD_START = 2000;
    private static final int CATEGORY_3_TIME_PERIOD_END = 2039;

    private static final int CATEGORY4_NUMBER_RANGE_START = 900;
    private static final int CATEGORY4_NUMBER_RANGE_END = 999;
    private static final int CATEGORY4_TIME_PERIOD_START = 1949;
    private static final int CATEGORY4_TIME_PERIOD_END = 1999;

    private static final int[] KONTROLL_SIFFER_1 = { 3, 7, 6, 1, 8, 9, 4, 5, 2 };
    private static final int[] KONTROLL_SIFFER_2 = { 5, 4, 3, 2, 7, 6, 5, 4, 3, 2 };

    private static final SecureRandom randomNumberProvider = new SecureRandom();

    public Set<String> genererFiktiveIdenter(RsPersonKriterier kriteria) {

        HashSet<String> identSet = new HashSet<>();
        while (identSet.size() != (kriteria.getAntall() * MULTIPLY_ANT_IDENTER)) {
            StringBuilder identitetBuilder = new StringBuilder();
            LocalDateTime fodselsdatoDate = genererFodsselsdatoBasertPaaKriterie(kriteria);
            String fodselsdato = genererFnrDnrBnrStringified(kriteria.getIdenttype(), fodselsdatoDate);
            List<Integer> rangeList = hentKategoriIntervallForDato(fodselsdatoDate);
            identitetBuilder.append(fodselsdato).append(genererIndividnummer(rangeList.get(0), rangeList.get(1), kriteria.getKjonn()));
            int forsteKontrollSiffer = genererKontrollsiffer(identitetBuilder, KONTROLL_SIFFER_1);
            identitetBuilder.append(forsteKontrollSiffer);
            int andreKontrollSiffer = genererKontrollsiffer(identitetBuilder, KONTROLL_SIFFER_2);
            identitetBuilder.append(andreKontrollSiffer);
            if (forsteKontrollSiffer == 10 || andreKontrollSiffer == 10) {
                // Hvis kontrollsiffer er 10, så må fodselsnummeret forkastes, og man prøver å lage et nytt.
                continue;
            }
            identSet.add(identitetBuilder.toString());
        }
        return identSet;
    }

    private static String genererFnrDnrBnrStringified(String identtype, LocalDateTime date) {
        String foedselsdato = date.format(DateTimeFormatter.ofPattern("ddMMyy"));

        switch (identtype) {
        case DNR:
            return Integer.toString(getNumericValue(foedselsdato.charAt(0)) + 4) + foedselsdato.substring(1);
        case BNR:
            return foedselsdato.substring(0, 2) + Integer.toString(getNumericValue(foedselsdato.charAt(2)) + 2) + foedselsdato.substring(3);
        case FNR:
        default:
            return foedselsdato;
        }
    }

    private static LocalDateTime genererFodsselsdatoBasertPaaKriterie(RsPersonKriterier kriterier) {

        return DateGenerator.genererRandomDatoInnenforIntervalInclusiveDatoEtterExclusiveDatoFoer(
                nullcheckSetDefaultValue(kriterier.getFoedtEtter(), DEFAULT_FODT_ETTER_DATE),
                nullcheckSetDefaultValue(kriterier.getFoedtFoer(), DEFAULT_FODT_FOER_DATE));
    }

    private static List<Integer> hentKategoriIntervallForDato(LocalDateTime date) {

        if (isInYearRange(date, CATEGORY1_TIME_PERIOD_START, CATEGORY1_TIME_PERIOD_END)) {
            return Arrays.asList(CATEGORY1_NUMBER_RANGE_START, CATEGORY1_NUMBER_RANGE_END);
        } else if (isInYearRange(date, CATEGORY2_TIME_PERIOD_START, CATEGORY2_TIME_PERIOD_END)) {
            return Arrays.asList(CATEGORY2_NUMBER_RANGE_START, CATEGORY2_NUMBER_RANGE_END);
        } else if (isInYearRange(date, CATEGORY_3_TIME_PERIOD_START, CATEGORY_3_TIME_PERIOD_END)) {
            return Arrays.asList(CATEGORY_3_NUMBER_RANGE_START, CATEGORY_3_NUMBER_RANGE_END);
        } else if (isInYearRange(date, CATEGORY4_TIME_PERIOD_START, CATEGORY4_TIME_PERIOD_END)) {
            return Arrays.asList(CATEGORY4_NUMBER_RANGE_START, CATEGORY4_NUMBER_RANGE_END);
        }
        return emptyList();
    }

    private static String genererIndividnummer(int rangeStart, int rangeSlutt, String kjonn) {

        String kjoennPaaIdent = isKvinne(kjonn) || isMann(kjonn) ? kjonn : lagTilfeldigKvinneEllerMann();
        int individnummer = BiasedRandom.lagTopptungRandom(rangeStart, rangeSlutt);

        //Kvinne har partall og mann har oddetall
        if (isKvinne(kjoennPaaIdent) && isOdd(individnummer) || isMann(kjoennPaaIdent) && isEven(individnummer)) {
            individnummer += 1;
        }

        if (individnummer > rangeSlutt) {
            individnummer -= 2;
        }

        return format("%03d", individnummer);
    }

    /**
     * <pre>
     * Lager kontrollsiffer for et fodselsnummer utifra satt kontrollsifferformel.
     * kontrollsiffer-1 = 11 - ((3*d + 7*d + 6*m +1*m + 8*å + 9*å + 4*i + 5*i + 2*i)  mod 11)
     * kontrollsiffer-2 = 11 - ((5*d + 4*d + 3*m + 2*m + 7*å + 6* å + 5*i + 4*i + 3*i + 2 *k1)  mod 11)
     * </pre>
     *
     * @param datoMedIndvid:              Fodselsnummer
     * @param formelMultiplierSifferListe Array med tallene som skal multipliseres med fodselsnummer i kontrollsifferformelen
     * @return Kontrollsiffer
     */
    private static int genererKontrollsiffer(StringBuilder datoMedIndvid, int... formelMultiplierSifferListe) {
        int kontrollsiffer = 0;

        for (int i = 0; i < formelMultiplierSifferListe.length; i++) {
            kontrollsiffer += getNumericValue(datoMedIndvid.charAt(i)) * formelMultiplierSifferListe[i];
        }
        return (11 - (kontrollsiffer % 11)) % 11;
    }

    private static boolean isInYearRange(LocalDateTime date, int rangeYearStart, int rangeYearEnd) {
        return (date.getYear() >= rangeYearStart && date.getYear() <= rangeYearEnd);
    }

    private static boolean isKvinne(String kjonn) {
        return "K".equals(kjonn);
    }

    private static boolean isMann(String kjonn) {
        return "M".equals(kjonn);
    }

    private static boolean isEven(int number) {
        return number % 2 == 0;
    }

    private static boolean isOdd(int number) {
        return number % 2 != 0;
    }

    private static String lagTilfeldigKvinneEllerMann() {
        return randomNumberProvider.nextDouble() < 0.5 ? "K" : "M";
    }
}
