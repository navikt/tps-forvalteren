package no.nav.tps.forvalteren.service.command.testdata;

import static java.lang.Character.getNumericValue;
import static java.lang.String.format;
import static java.util.Collections.emptyList;

import java.security.SecureRandom;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
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

    private static final int MULTIPLY_ANT_IDENTER = 2;
    private static final LocalDateTime DEFAULT_FODT_ETTER_DATE = LocalDate.of(1900, Month.JANUARY, 1).atStartOfDay();
    private static final LocalDateTime DEFAULT_FODT_FOER_DATE = LocalDate.now().atStartOfDay();

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

    private String genererFnrDnrBnrStringified(String identtype, LocalDateTime date) {
        switch (identtype) {
        case "DNR":
            return genererNyttDnummer(date);
        case "BNR":
            return genererNyttBNummer(date);
        default:
            return genererNyttFnr(date);
        }
    }

    private String genererNyttFnr(LocalDateTime date) {
        return localDateToDDmmYYStringFormat(date);
    }

    private String genererNyttDnummer(LocalDateTime date) {
        String fodselsdato = localDateToDDmmYYStringFormat(date);

        int forsteSiffer = getNumericValue(fodselsdato.charAt(0)) + 4;
        return Integer.toString(forsteSiffer) + fodselsdato.substring(1);
    }

    private String genererNyttBNummer(LocalDateTime date) {
        String fodselsdato = localDateToDDmmYYStringFormat(date);

        int maanedSiffer = getNumericValue(fodselsdato.charAt(2)) + 2;
        return fodselsdato.substring(0, 2) + Integer.toString(maanedSiffer) + fodselsdato.substring(3);
    }

    private String localDateToDDmmYYStringFormat(LocalDateTime date) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("ddMMyy");
        return date.format(formatter);
    }

    private LocalDateTime genererFodsselsdatoBasertPaaKriterie(RsPersonKriterier kriterier) {
        LocalDateTime mustBeAfterDate;
        LocalDateTime mustBeBeforeDate;
        if (kriterier.getFoedtEtter() == null) {
            mustBeAfterDate = DEFAULT_FODT_ETTER_DATE;
        } else {
            mustBeAfterDate = kriterier.getFoedtEtter();
        }
        if (kriterier.getFoedtFoer() == null) {
            mustBeBeforeDate = DEFAULT_FODT_FOER_DATE;
        } else {
            mustBeBeforeDate = kriterier.getFoedtFoer();
        }
        return DateGenerator.genererRandomDatoInnenforIntervalInclusiveDatoEtterExclusiveDatoFoer(mustBeAfterDate, mustBeBeforeDate);
    }

    private List<Integer> hentKategoriIntervallForDato(LocalDateTime date) {

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

    private String genererIndividnummer(int rangeStart, int rangeSlutt, String kjonn) {

        String kjoennPaaIdent = isKvinne(kjonn) || isMann(kjonn) ? kjonn : lagTilfeldigKvinneEllerMann();
        int individnummer = BiasedRandom.lagBunntungRandom(rangeStart, rangeSlutt);

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
    private int genererKontrollsiffer(StringBuilder datoMedIndvid, int... formelMultiplierSifferListe) {
        int kontrollsiffer = 0;

        for (int i = 0; i < formelMultiplierSifferListe.length; i++) {
            kontrollsiffer += getNumericValue(datoMedIndvid.charAt(i)) * formelMultiplierSifferListe[i];
        }
        return (11 - (kontrollsiffer % 11)) % 11;
    }

    private boolean isInYearRange(LocalDateTime date, int rangeYearStart, int rangeYearEnd) {
        return (date.getYear() >= rangeYearStart && date.getYear() <= rangeYearEnd);
    }

    private boolean isKvinne(String kjonn) {
        return "K".equals(kjonn);
    }

    private boolean isMann(String kjonn) {
        return "M".equals(kjonn);
    }

    private boolean isEven(int number) {
        return number % 2 == 0;
    }

    private boolean isOdd(int number) {
        return number % 2 != 0;
    }

    private String lagTilfeldigKvinneEllerMann() {
        return randomNumberProvider.nextDouble() < 0.5 ? "K" : "M";
    }
}
