package no.nav.tps.vedlikehold.domain.service.command.tps.testdata;

/**
 * Created by F148888 on 03.01.2017.
 */

//TODO Lag en builder slik at man kan sette hva man vil til SKD meldingen?
public class SkdMelding {

    char[] skdMelding;

    public SkdMelding(){
        skdMelding = new char[1500];
    }
}

/*
231086267632013050208074810220130429100000000PETURSSON                                         SIGURDUR FREYR                                                                                                                                                                 000000009105AKRANES             105000000002310862676320130429100000000010000000000000000000                                                  000201304292013042820120526503260000000ALTAVEIEN                O                         95150022                                                                                          0001052013042920130428000000000000000000000000000000000000000 00000000 00000000 00000000 000000000000000000000000000                                                  00000000000000                                                  00063108606721201304290000000000000000000  1                0000   0000 00000000 001653     000000000000M78420  00130000                                                                                                                        00000000000 00000000 0501
 */