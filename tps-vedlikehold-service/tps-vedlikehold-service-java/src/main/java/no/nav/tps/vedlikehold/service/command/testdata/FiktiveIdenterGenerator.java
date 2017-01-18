package no.nav.tps.vedlikehold.service.command.testdata;

import no.nav.tps.vedlikehold.domain.service.command.tps.testdata.Kjonn;
import no.nav.tps.vedlikehold.domain.service.command.tps.testdata.TestDataRequest;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

/**
 * Created by Rizwan Ali Ahmed, Visma Consulting AS.
 */
@Service
public class FiktiveIdenterGenerator {

    private static final int CATEGORY1_RANGE_START = 0;
    private static final int CATEGORY1_RANGE_END = 499;
    private static final int CATEGORY1_PERIOD_START = 1900;
    private static final int CATEGORY1_PERIOD_END = 1999;

    private static final int CATEGORY2_RANGE_START = 500;
    private static final int CATEGORY2_RANGE_END = 749;
    private static final int CATEGORY2_PERIOD_START = 1854;
    private static final int CATEGORY2_PERIOD_END = 1899;

    private static final int CATEGORY_3_RANGE_START = 500;
    private static final int CATEGORY_3_RANGE_END = 999;
    private static final int CATEGORY_3_PERIOD_START = 2000;
    private static final int CATEGORY_3_PERIOD_END = 2039;

    private static final int CATEGORY4_RANGE_START = 900;
    private static final int CATEGORY4_RANGE_END = 999;
    private static final int CATEGORY4_PERIOD_START = 1949;
    private static final int CATEGORY4_PERIOD_END = 1999;

    final private int[] KONTROLL_SIFFER_C1 = {3,7,6,1,8,9,4,5,2};
    final private int[] KONTROLL_SIFFER_C2 = {5,4,3,2,7,6,5,4,3,2};


    public List<String> genererFiktiveIdenter(TestDataRequest request) {
        if(request.getIdentType().equals("Fnr")){
            return genererNyttFnr(request);
        }else{
            return genererNyttDnr(request);
        }
    }

    private List<String> genererNyttFnr(TestDataRequest request){
        String fdato = localDateToDDMMYYStringFormat(request.getDato());
        return genererIdenter(request,fdato);
    }

    private List<String> genererNyttDnr(TestDataRequest request){
        String fdato = localDateToDDMMYYStringFormat(request.getDato());

        int forsteSiffer = Character.getNumericValue(fdato.charAt(0)) + 4;
        String dfdato =  Integer.toString(forsteSiffer) + fdato.substring(1);

        return genererIdenter(request,dfdato);
    }

    private String localDateToDDMMYYStringFormat(LocalDate date){
       String dateString =  date.toString();
       return dateString.substring(8,10) + dateString.substring(5,7) + dateString.substring(2,4) ; //1988-01-01
    }


    private List<String> genererIdenter(TestDataRequest request, String fdato){
        StringBuilder identitetBuilder;
        List<String> identListe = new ArrayList<>();
        for(int i =  0; i < request.getAntallIdenter(); i++ ){
            identitetBuilder = new StringBuilder();
            List<Integer> rangeList = getRangeForIndividualRange(request.getDato());
            identitetBuilder.append(fdato).append(genererIndividnummer(rangeList.get(0),rangeList.get(1),request.getKjonn()));
            identitetBuilder.append(getFirstKontrollSiffer(identitetBuilder.toString()));
            identitetBuilder.append(getSecondKontrollSiffer(identitetBuilder.toString()));
            identListe.add(identitetBuilder.toString());
        }
        return identListe;
    }

    private List<Integer> getRangeForIndividualRange(LocalDate date){
        List<Integer> rangeList = new ArrayList<>();
        if(isInYearRange(date, CATEGORY1_PERIOD_START, CATEGORY1_PERIOD_END)){
            rangeList.addAll(Arrays.asList(CATEGORY1_RANGE_START, CATEGORY1_RANGE_END));
        }else if(isInYearRange(date, CATEGORY2_PERIOD_START, CATEGORY2_PERIOD_END)){
            rangeList.addAll(Arrays.asList(CATEGORY2_RANGE_START, CATEGORY2_RANGE_END));
        }else if (isInYearRange(date,CATEGORY_3_PERIOD_START,CATEGORY_3_PERIOD_END)){
            rangeList.addAll(Arrays.asList(CATEGORY_3_RANGE_START, CATEGORY_3_RANGE_END));
        }
        return rangeList;
    }

    private int genererIndividnummer(int range_start, int range_end, String kjonn){
        Random random = new Random();
        int individNummber;

        if(kjonn.equals(Kjonn.KVINNE)){         //KVINNE: Individnummer avsluttes med partall
            individNummber =  random.nextInt((range_end-range_start)/2)*2;
        }else{                                  // MANN: Individnummer avsluttes med oddetall
            if(range_start % 2 == 0) --range_start;
            if(range_end % 2 == 0) ++range_end;
            individNummber = random.nextInt((range_end-range_start)/2+1)*2;
        }
        return individNummber;
    }

    private int getFirstKontrollSiffer(String unprocessedFnr){
        char[] fnrCharacters = unprocessedFnr.toCharArray();
        return getKontrollSiffer(fnrCharacters, KONTROLL_SIFFER_C1);
    }

    private int getSecondKontrollSiffer(String unprocessedFnr){
        char[] fnrCharacters = unprocessedFnr.toCharArray();
        return getKontrollSiffer(fnrCharacters, KONTROLL_SIFFER_C2);
    }

    private int getKontrollSiffer(char[] fnrCharacters, int[] kontrollSiffer){
        int sumOfMulplier = 0;
        for (int i = 0; i < fnrCharacters.length; i++){     //TODO Den feiler her fordi  "fnrCharacters" blir for lang på et tidspunkt. Den blir 11 i length, mens "kontrollSiffer" kun er 10. IndexOutOfBounds.
            sumOfMulplier += kontrollSiffer[i]*Character.getNumericValue(fnrCharacters[i]);
        }
        return 11-(sumOfMulplier % 11);
    }

    private boolean isInYearRange(LocalDate date, int rangeYearStart, int rangeYearEnd){
        return (date.getYear()>= rangeYearStart && date.getYear()<= rangeYearEnd);
    }
}
