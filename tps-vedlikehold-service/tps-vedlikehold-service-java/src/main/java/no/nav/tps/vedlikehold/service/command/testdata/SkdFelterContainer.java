package no.nav.tps.vedlikehold.service.command.testdata;

import no.nav.tps.vedlikehold.domain.service.tps.testdata.SkdFeltDefinisjon;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Peter Fløgstad on 09.01.2017.
 */
@Component
public class SkdFelterContainer {

    private static final String whitespace5stk = "     ";
    private static final String whitespace10stk = whitespace5stk+whitespace5stk;
    private static final String whitespace25stk = "                         ";
    private static final String whitespace40stk = whitespace10stk+whitespace10stk+whitespace10stk+whitespace10stk;
    private static final String whitespace30stk = whitespace5stk + whitespace5stk;
    private static final String whitespace50stk = whitespace25stk+whitespace25stk;
    private static final String whitespace100stk = whitespace50stk+whitespace50stk;
    private static final String whitespace200stk = whitespace100stk+whitespace100stk;

    public List<SkdFeltDefinisjon> hentSkdFelter(){
        ArrayList<SkdFeltDefinisjon> SkdFelter = new ArrayList<>();

        SkdFelter.add(new SkdFeltDefinisjon("T1-FODSELSDATO", "000000", 1,6,1,6));
        SkdFelter.add(new SkdFeltDefinisjon("T1-PERSONNUMMER", "00000", 2, 5,7,11));
        SkdFelter.add(new SkdFeltDefinisjon("T1-MASKINDATO", "00000000", 3, 8,12,19));
        SkdFelter.add(new SkdFeltDefinisjon("T1-MASKINTID", "000000", 4, 6,20,25));
        SkdFelter.add(new SkdFeltDefinisjon("T1-TRANSTYPE", "0", 5, 1,26,26));
        SkdFelter.add(new SkdFeltDefinisjon("T1-AARSAKSKODE", "00", 6, 2,27,28));
        SkdFelter.add(new SkdFeltDefinisjon("T1-REG-DATO", "00000000", 7, 8,29,36));
        SkdFelter.add(new SkdFeltDefinisjon("T1-STATUSKODE", " ", 8, 1,37,37));
        SkdFelter.add(new SkdFeltDefinisjon("T1-DATO-DOED", "00000000", 9, 8,38,45));
        SkdFelter.add(new SkdFeltDefinisjon("T1-SLEKTSNAVN", whitespace50stk, 10,50,46,95));
        SkdFelter.add(new SkdFeltDefinisjon("T1-FORNAVN", whitespace50stk, 11,50,96,145));
        SkdFelter.add(new SkdFeltDefinisjon("T1-MELLOMNAVN", whitespace50stk, 12,50,146,195));
        SkdFelter.add(new SkdFeltDefinisjon("T1-SLEKSTNAVN-UGIFT", whitespace50stk, 13,50,196,245));
        SkdFelter.add(new SkdFeltDefinisjon("T1-FORKORTET-NAVN", whitespace25stk, 14,25,246,270));
        SkdFelter.add(new SkdFeltDefinisjon("T1-REG-DATO-NAVN","00000000", 15,8,271,278));


        SkdFelter.add(new SkdFeltDefinisjon("T1-FOEDEKOMM-LAND","    ", 16,4));
        SkdFelter.add(new SkdFeltDefinisjon("T1-FOEDESTED","                    ", 17,20));
        SkdFelter.add(new SkdFeltDefinisjon("T1-STATSBORGERSKAP","000", 18,3));
        SkdFelter.add(new SkdFeltDefinisjon("T1-REGDATO-STATSB","00000000", 19,8));
        SkdFelter.add(new SkdFeltDefinisjon("T1-FAMILIENUMMER","00000000000",20,11));
        SkdFelter.add(new SkdFeltDefinisjon("T1-REGDATO-FAMNR","00000000",21,8));
        SkdFelter.add(new SkdFeltDefinisjon("T1-PERSONKODE","0",22,1));
        SkdFelter.add(new SkdFeltDefinisjon("T1-SPES-REG-TYPE","0",23,1));
        SkdFelter.add(new SkdFeltDefinisjon("T1-DATO-SPES-REG-TYPE","00000000",24,8));
        SkdFelter.add(new SkdFeltDefinisjon("T1-SIVILSTAND","0",25,1));
        SkdFelter.add(new SkdFeltDefinisjon("T1-REGDATO-SIVILSTAND","00000000",26,8));
        SkdFelter.add(new SkdFeltDefinisjon("T1-EKTEFELLE-PARTNER-FNR","0",27,1));       // Usikker, manglet data på confluence
        SkdFelter.add(new SkdFeltDefinisjon("T1-EKTEFELLE-PARTNER-FDATO","000000",28,6));
        SkdFelter.add(new SkdFeltDefinisjon("T1-EKTEFELLE-PARTNER-PNR","00000",29,5));
        SkdFelter.add(new SkdFeltDefinisjon("T1-EKTEFELLE-PARTNER-NAVN",whitespace50stk,30,50));
        SkdFelter.add(new SkdFeltDefinisjon("T1-EKTEFELLE-PARTNER-STATSB","000",31,3));
        SkdFelter.add(new SkdFeltDefinisjon("T1-AKTUELL-ADRESSE","0",32,1));
        SkdFelter.add(new SkdFeltDefinisjon("T1-REGDATO-ADR","00000000",33,8));
        SkdFelter.add(new SkdFeltDefinisjon("T1-FLYTTEDATO-ADR","00000000",34,8));
        SkdFelter.add(new SkdFeltDefinisjon("T1-KOMMUNEENUMMER","0000",35,4));
        SkdFelter.add(new SkdFeltDefinisjon("T1-GATE-GAARD","00000",36,5));
        SkdFelter.add(new SkdFeltDefinisjon("T1-HUS-BRUK","0000",37,4));
        SkdFelter.add(new SkdFeltDefinisjon("T1-BOKSTAV-FESTENR","0000",38,4));
        SkdFelter.add(new SkdFeltDefinisjon("T1-UNDERNR","000",39,3));
        SkdFelter.add(new SkdFeltDefinisjon("T1-ADRESSENAVN",whitespace25stk,40,25));
        SkdFelter.add(new SkdFeltDefinisjon("T1-ADRESSETYPE"," ",41,1));
        SkdFelter.add(new SkdFeltDefinisjon("T1-TILLEGGSADRESSE",whitespace25stk,42,25));
        SkdFelter.add(new SkdFeltDefinisjon("T1-POSTNUMMER","0000",43,4));
        SkdFelter.add(new SkdFeltDefinisjon("T1-VALGKRETS","0000",44,4));
        SkdFelter.add(new SkdFeltDefinisjon("T1-POSTADRESSE","0",45,1));             // Spesiell i gjen
        SkdFelter.add(new SkdFeltDefinisjon("T1-ADRESSE1",whitespace30stk,46,30));
        SkdFelter.add(new SkdFeltDefinisjon("T1-ADRESSE2",whitespace30stk,47,30));
        SkdFelter.add(new SkdFeltDefinisjon("T1-ADRESSE3",whitespace30stk,48,30));
        SkdFelter.add(new SkdFeltDefinisjon("T1-POSTADR-LAND","000",49,3));
        SkdFelter.add(new SkdFeltDefinisjon("T1-INNVANDRET-FRA-LAND","000",50,3));
        SkdFelter.add(new SkdFeltDefinisjon("T1-FRA-LAND-REGDATO","00000000",51,8));
        SkdFelter.add(new SkdFeltDefinisjon("T1-FRA-LAND-FLYTTEDATO","00000000",52,8));
        SkdFelter.add(new SkdFeltDefinisjon("T1-FRA-KOMMUNE","0000",53,4));
        SkdFelter.add(new SkdFeltDefinisjon("T1-FRA-KOMM-REGDATO","00000000",54,8));
        SkdFelter.add(new SkdFeltDefinisjon("T1-FRA-KOMM-FLYTTEDATO","00000000",55,8));
        SkdFelter.add(new SkdFeltDefinisjon("T1-UTVANDRET-TIL-LAND","000",56,3));
        SkdFelter.add(new SkdFeltDefinisjon("T1-TIL-LAND-REGDATO","00000000",57,8));
        SkdFelter.add(new SkdFeltDefinisjon("T1-TIL-LAND-FLYTTEDATO","00000000",58,8));
        SkdFelter.add(new SkdFeltDefinisjon("T1-SAMEMANNTALL"," ",59,1));
        SkdFelter.add(new SkdFeltDefinisjon("T1-DATO-SAMEMANNTALL","00000000",60,8));
        SkdFelter.add(new SkdFeltDefinisjon("T1-UMYNDIGGJORT"," ",61,1));
        SkdFelter.add(new SkdFeltDefinisjon("T1-DATO-UMYNDIGGJORT","00000000",62,8));
        SkdFelter.add(new SkdFeltDefinisjon("T1-FORELDREANSVAR"," ",63,1));
        SkdFelter.add(new SkdFeltDefinisjon("T1-DATO-FORELDREANSVAR","00000000",64,8));
        SkdFelter.add(new SkdFeltDefinisjon("T1-ARBEIDSTILLATELSE"," ",65,1));
        SkdFelter.add(new SkdFeltDefinisjon("T1-DATO-ARBEIDSTILLATELSE","00000000",66,8));
        SkdFelter.add(new SkdFeltDefinisjon("T1-FREMKONNUMMER","00000000",67,8));
        SkdFelter.add(new SkdFeltDefinisjon("T1-MORS-FODSELSNR","0",68,1));      //Spesiell igjen
        SkdFelter.add(new SkdFeltDefinisjon("T1-MORS-FODSELSDATO","000000",69,6));
        SkdFelter.add(new SkdFeltDefinisjon("T1-MORS-PERSONNUMMER","00000",70,5));
        SkdFelter.add(new SkdFeltDefinisjon("T1-MORS-NAVN",whitespace50stk,71,50));
        SkdFelter.add(new SkdFeltDefinisjon("T1-MORS-STATSB-SKAP","000",72,3));
        SkdFelter.add(new SkdFeltDefinisjon("T1-FARS-FODSELSNR","0",73,1));              // Spesiell igjen
        SkdFelter.add(new SkdFeltDefinisjon("T1-FARS-FODSELSDATO","000000",74,6));
        SkdFelter.add(new SkdFeltDefinisjon("T1-FARS-PERSONNUMMER","00000",75,5));
        SkdFelter.add(new SkdFeltDefinisjon("T1-FARS-FARS-NAVN",whitespace50stk,76,50));
        SkdFelter.add(new SkdFeltDefinisjon("T1-FARS-STATSB-SKAP","000",77,3));

        SkdFelter.add(new SkdFeltDefinisjon("T1-TIDLIGERE-FNR-DNR","00000000000",78,11));
        SkdFelter.add(new SkdFeltDefinisjon("T1-DATO-TIDL-FNR-DNR","00000000",79,8));
        SkdFelter.add(new SkdFeltDefinisjon("T1-NYTT-FNR","00000000000",80,11));
        SkdFelter.add(new SkdFeltDefinisjon("T1-DATO-NYTT-FNR","00000000",81,8));


        SkdFelter.add(new SkdFeltDefinisjon("T1-LEVENDE-DOED"," ",82,1));
        SkdFelter.add(new SkdFeltDefinisjon("T1-KJONN"," ",83,1));
        SkdFelter.add(new SkdFeltDefinisjon("T1-TILDELINGSKODE"," ",84,1));
        SkdFelter.add(new SkdFeltDefinisjon("T1-FOEDSELSTYPE","  ",85,2));
        SkdFelter.add(new SkdFeltDefinisjon("T1-MORS-SIVILTILSTAND"," ",86,1));
        SkdFelter.add(new SkdFeltDefinisjon("T1-EKTESK-PARTNSK-NR"," ",87,1));
        SkdFelter.add(new SkdFeltDefinisjon("T1-EKTF-EKTESK-PARTNSK-NR"," ",88,1));
        SkdFelter.add(new SkdFeltDefinisjon("T1-VIGSELSTYPE"," ",89,1));
        SkdFelter.add(new SkdFeltDefinisjon("T1-FORS-BYRDE","  ",90,2));
        SkdFelter.add(new SkdFeltDefinisjon("T1-DOMBEVILLING"," ",91,1));
        SkdFelter.add(new SkdFeltDefinisjon("T1-ANTALL-BARN","  ",92,2));
        SkdFelter.add(new SkdFeltDefinisjon("T1-TIDL-SIVILSTAND"," ",93,1));
        SkdFelter.add(new SkdFeltDefinisjon("T1-EKTF-TIDL-SIVILSTAND"," ",94,1));
        SkdFelter.add(new SkdFeltDefinisjon("T1-HJEMMEL"," ",95,1));
        SkdFelter.add(new SkdFeltDefinisjon("T1-FYLKE","  ",96,2));
        SkdFelter.add(new SkdFeltDefinisjon("T1-VIGSELSKOMM","0000",97,4));
        SkdFelter.add(new SkdFeltDefinisjon("T1-TIDL-SEP-DOM-BEV"," ",98,1));
        SkdFelter.add(new SkdFeltDefinisjon("T1-BEGJERT-AV"," ",99,1));
        SkdFelter.add(new SkdFeltDefinisjon("T1-REGISTR-GRUNNLAG"," ",100,1));
        SkdFelter.add(new SkdFeltDefinisjon("T1-DOEDSSTED","    ",101,4));
        SkdFelter.add(new SkdFeltDefinisjon("T1-TYPE-DOEDSSTED"," ",102,1));
        SkdFelter.add(new SkdFeltDefinisjon("T1-VIGSELSDATO","00000000",103,8));
        SkdFelter.add(new SkdFeltDefinisjon("T1-MEDL-KIRKEN"," ",104,1));
        SkdFelter.add(new SkdFeltDefinisjon("T1-SEKVENSNR","000000",105,6));
        SkdFelter.add(new SkdFeltDefinisjon("T1-BOLIGNR","     ",106,5));
        SkdFelter.add(new SkdFeltDefinisjon("T1-DUF-ID","000000000000",107,12));
        SkdFelter.add(new SkdFeltDefinisjon("T1-BRUKERIDENT","        ",108,8));
        SkdFelter.add(new SkdFeltDefinisjon("T1-SKOLERETS","0000",109,4));
        SkdFelter.add(new SkdFeltDefinisjon("T1-TK-NR","0000",110,4));
        SkdFelter.add(new SkdFeltDefinisjon("T1-DNR-HJEMLANDSADRESSE1",whitespace40stk,111,40));
        SkdFelter.add(new SkdFeltDefinisjon("T1-DNR-HJEMLANDSADRESSE2",whitespace40stk,112,40));
        SkdFelter.add(new SkdFeltDefinisjon("T1-DNR-HJEMLANDSADRESSE3",whitespace40stk,113,40));
        SkdFelter.add(new SkdFeltDefinisjon("T1-DNR-HJEMLAND-LANDKODE","000",114,3));
        SkdFelter.add(new SkdFeltDefinisjon("T1-DNR-HJEMLAND-REG-DATO","00000000",115,8));
        SkdFelter.add(new SkdFeltDefinisjon("T1-DNR-ID-KONTROLL"," ",116,1));
        SkdFelter.add(new SkdFeltDefinisjon("T1-POSTADR-REG-DATO","00000000",117,8));
        SkdFelter.add(new SkdFeltDefinisjon("T1-UTVANDRINGSTYPE"," ",118,1));
        SkdFelter.add(new SkdFeltDefinisjon("T1-GRUNNKRETS","0000",119,4));
        SkdFelter.add(new SkdFeltDefinisjon("T1-STATSBORGERSKAP2","000",120,3));
        SkdFelter.add(new SkdFeltDefinisjon("T1-REGDATO-STATSB2","00000000",121,8));
        SkdFelter.add(new SkdFeltDefinisjon("T1-STATSBORGERSKAP3","000",122,3));
        SkdFelter.add(new SkdFeltDefinisjon("T1-REGDATO-STATSB3","00000000",123,8));
        SkdFelter.add(new SkdFeltDefinisjon("T1-STATSBORGERSKAP4","000",124,3));
        SkdFelter.add(new SkdFeltDefinisjon("T1-REGDATO-STATSB4","00000000",125,8));
        SkdFelter.add(new SkdFeltDefinisjon("T1-STATSBORGERSKAP5","000",126,3));
        SkdFelter.add(new SkdFeltDefinisjon("T1-REGDATO-STATSB5","00000000",127,8));
        SkdFelter.add(new SkdFeltDefinisjon("T1-STATSBORGERSKAP6","000",128,3));
        SkdFelter.add(new SkdFeltDefinisjon("T1-REGDATO-STATSB6","00000000",129,8));
        SkdFelter.add(new SkdFeltDefinisjon("T1-STATSBORGERSKAP7","000",130,3));
        SkdFelter.add(new SkdFeltDefinisjon("T1-REGDATO-STATSB7","00000000",131,8));
        SkdFelter.add(new SkdFeltDefinisjon("T1-STATSBORGERSKAP8","000",132,3));
        SkdFelter.add(new SkdFeltDefinisjon("T1-REGDATO-STATSB8","00000000",132,8));
        SkdFelter.add(new SkdFeltDefinisjon("T1-STATSBORGERSKAP9","000",133,3));
        SkdFelter.add(new SkdFeltDefinisjon("T1-REGDATO-STATSB9","00000000",134,8));
        SkdFelter.add(new SkdFeltDefinisjon("T1-STATSBORGERSKAP10","000",135,3));
        SkdFelter.add(new SkdFeltDefinisjon("T1-REGDATO-STATSB10","00000000",136,8));
        SkdFelter.add(new SkdFeltDefinisjon("T1-BIBEHOLD"," ",137,1));
        SkdFelter.add(new SkdFeltDefinisjon("T1-REGDATO-BIBEHOLD","00000000",138,8));
        SkdFelter.add(new SkdFeltDefinisjon("T1-RESERVER-FRAMTIDIG-BRUK",whitespace200stk+whitespace50stk+whitespace40stk+whitespace5stk+"  ",139,297));

        return SkdFelter;
    }
}

/* Et eksempel på SKD-Meldinger. --> Innvandring

231086267632013050208074810220130429100000000PETURSSON                                         SIGURDUR FREYR                                                                                                                                                                 000000009105AKRANES             105000000002310862676320130429100000000010000000000000000000                                                  000201304292013042820120526503260000000ALTAVEIEN                O                         95150022                                                                                          0001052013042920130428000000000000000000000000000000000000000 00000000 00000000 00000000 000000000000000000000000000                                                  00000000000000                                                  00063108606721201304290000000000000000000  1                0000   0000 00000000 001653     000000000000M78420  00130000                                                                                                                        00000000000 00000000 0501

 */
