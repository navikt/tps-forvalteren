package no.nav.tps.vedlikehold.service.testdata;

import org.joda.time.DateTime;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

/**
 * Created by Rizwan Ali Ahmed, Visma Consulting AS.
 */
public class GenererFiktiveFnr {

    private enum Gender{
        MANN, KVINNE
    }

    private int CATEGORY_1_RANGE_START = 0;
    private int CATEGORY_1_RANGE_END = 499;

    private int CATEGORY_2_RANGE_START = 500;
    private int CATEGORY_2_RANGE_END = 749;

    private int CATEGORY_3_RANGE_START = 500;
    private int CATEGORY_3_RANGE_END = 999;

    private int CATEGORY_4_RANGE_START = 900;
    private int CATEGORY_4_RANGE_END = 999;


    void genererNyFnr(DateTime date, Gender gender){

        DateFormat dateFormat = new SimpleDateFormat("ddmmyy");
        String fdato = dateFormat.format(date);


    }

    private List<Integer> genererNyPnr(DateTime date, Gender gender, int numberOfRequestedPnr){
        List<Integer> listOfPnr = new ArrayList<>();
        for(int i =  0; i < numberOfRequestedPnr; i++ ){
            List<Integer> rangeList = getRangeForIndividualRange(date);
            genererIndividnummer(rangeList.get(0),rangeList.get(1),gender);
        }
        return listOfPnr;
    }

    private List<Integer> getRangeForIndividualRange(DateTime date){
        List<Integer> rangeList = new ArrayList<>();
        if(inYearRange(date,1854,1899)){
            rangeList.addAll(Arrays.asList(CATEGORY_2_RANGE_START, CATEGORY_2_RANGE_END));
        }else if(inYearRange(date,1900,1999)){
            rangeList.addAll(Arrays.asList(CATEGORY_1_RANGE_START, CATEGORY_1_RANGE_END));
        }else if (inYearRange(date,2000,2039)){
            rangeList.addAll(Arrays.asList(CATEGORY_3_RANGE_START, CATEGORY_3_RANGE_END));
        }
        return rangeList;
    }

    private int genererIndividnummer(int range_start, int range_end, Gender gender){
        Random random = new Random();
        if(gender.equals(Gender.KVINNE)){
            return random.nextInt((range_end-range_start)/2)*2;
        }else{
            if(range_start % 2 == 0) --range_start;
            if(range_end % 2 == 0) ++range_end;
            return random.nextInt((range_end-range_start)/2+1)*2;
        }
    }

    private void personalNumberController(){
        //Todo: Rizwan, du må huske å fikse denne metoden.
    }


    GenererFiktiveFnr(){
        //Todo:
    }

    private boolean inYearRange(DateTime date, int rangeYearStart, int rangeYearEnd){
        return (date.getYear()>= rangeYearStart && date.getYear()<= rangeYearEnd);
    }

}
