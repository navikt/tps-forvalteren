package no.nav.tps.vedlikehold.service.command.testdata;

import no.nav.tps.vedlikehold.domain.service.command.tps.testdata.TestDataPerson;

import java.util.concurrent.ThreadLocalRandom;

public class NavnGeneratorForTestPersoner {

    // Fornavn Gutter
    static final String[] gutteNavn = {"TestLars, TestMartin, TestPeter"};

    // Fornavn Jenter
    static final String[] jenteNavn = {"TestList, TestTine, TestTiril"};

    // Mellomnavn
    static final String[] mellomnavn = {"Mellom"};

    // Etternavn
    static final String[] etternavn = {"Nordmann, Thomassen, Kaiassen"};

    public static void setTilfeldigNavnForTestPerson(TestDataPerson testDataPerson){
        testDataPerson.setFornavn(testDataPerson.getKjonn().equals("MANN") ? genererTilfeldigGuttefornavn() : genererTilfeldigJentefornavn());
        testDataPerson.setEtternavn(genererTilfeldigEtternavn());
    }

    private static String genererTilfeldigJentefornavn(){
        return jenteNavn[ThreadLocalRandom.current().nextInt(0,jenteNavn.length)];
    }

    private static String genererTilfeldigGuttefornavn(){
        return gutteNavn[ThreadLocalRandom.current().nextInt(0,gutteNavn.length )];
    }

    private static String genererTilfeldigEtternavn(){
        return etternavn[ThreadLocalRandom.current().nextInt(0,etternavn.length)];
    }

    private static String genererTilfeldigMellomnavn(){
        return mellomnavn[ThreadLocalRandom.current().nextInt(0,mellomnavn.length)];
    }
}
