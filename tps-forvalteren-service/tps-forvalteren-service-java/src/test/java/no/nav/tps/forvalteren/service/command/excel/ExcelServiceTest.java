package no.nav.tps.forvalteren.service.command.excel;

import static com.google.common.base.Charsets.UTF_8;
import static java.util.Arrays.asList;
import static java.util.Collections.singletonList;
import static java.util.Objects.nonNull;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.when;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.time.LocalDateTime;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.core.io.Resource;

import no.nav.tps.forvalteren.domain.jpa.Adresse;
import no.nav.tps.forvalteren.domain.jpa.Gateadresse;
import no.nav.tps.forvalteren.domain.jpa.Person;
import no.nav.tps.forvalteren.service.command.testdata.restreq.PersonService;

@RunWith(MockitoJUnitRunner.class)
public class ExcelServiceTest {

    private static final String IDENT = "111111111111";

    @Mock
    private PersonService personService;

    @InjectMocks
    private ExcelService excelService;

    @Test
    public void getPersonFile_OK() throws Exception {

        when(personService.getPersonerByIdenter(singletonList(IDENT))).thenReturn(singletonList(buildPerson()));
        Resource resultat = excelService.getPersonFile(singletonList(IDENT));

        StringBuilder builder = new StringBuilder();
        BufferedReader reader = new BufferedReader(new InputStreamReader(resultat.getInputStream(), UTF_8));
        String line;
        while (nonNull((line = reader.readLine()))) {
            builder.append(line);
        }
        assertThat(builder.toString().replaceAll("\t", ""),
                is("Ident;Identtype;Kjønn;Sivilstand;Diskresjonskode;ErUtenFastBopel;Egenansatt;"
                        + "Etternavn;Fornavn;Gateadresse;Husnummer;Gatekode;Postnr;Kommunenr;Flyttedato;"
                        + "Postlinje1;Postlinje2;Postlinje3;Postland;InnvandretFraLand;GtVerdi;GtType;"
                        + "GtRegel;Språkkode;Statsborgerskap;TypeSikkerhetTiltak;BeskrivelseSikkerhetTiltak;"
                        + "Relasjon1-Type;Relasjon1-Ident;Relasjon2-Type;Relasjon2-Ident;Relasjon3-Type;"
                        + "Relasjon3-Ident\"111111111111\";FNR;M;GIFT;;false;false;MASKIN;GOD;Tveterveien;"
                        + "2B;\"12345\";\"1234\";\"5678\";2018-10-10;;;;;SAU;\"0617\";KNR;A;NB;;;"));
    }

    @Test
    public void getPersonFileMedKode6_OK() throws Exception {

        Person kode6Person = buildPerson();
        kode6Person.setBoadresse(null);

        when(personService.getPersonerByIdenter(singletonList(IDENT))).thenReturn(singletonList(kode6Person));
        Resource resultat = excelService.getPersonFile(singletonList(IDENT));

        StringBuilder builder = new StringBuilder();
        BufferedReader reader = new BufferedReader(new InputStreamReader(resultat.getInputStream(), UTF_8));
        String line;
        while (nonNull((line = reader.readLine()))) {
            builder.append(line);
        }
        assertThat(builder.toString().replaceAll("\t", ""),
                is("Ident;Identtype;Kjønn;Sivilstand;Diskresjonskode;ErUtenFastBopel;Egenansatt;"
                        + "Etternavn;Fornavn;Gateadresse;Husnummer;Gatekode;Postnr;Kommunenr;Flyttedato;"
                        + "Postlinje1;Postlinje2;Postlinje3;Postland;InnvandretFraLand;GtVerdi;GtType;"
                        + "GtRegel;Språkkode;Statsborgerskap;TypeSikkerhetTiltak;BeskrivelseSikkerhetTiltak;"
                        + "Relasjon1-Type;Relasjon1-Ident;Relasjon2-Type;Relasjon2-Ident;Relasjon3-Type;"
                        + "Relasjon3-Ident\"111111111111\";FNR;M;GIFT;;false;false;MASKIN;GOD;;;;;;"
                        + ";;;;;SAU;\"0617\";KNR;A;NB;;;"));
    }

    private static Person buildPerson() {
        Adresse adresse = Gateadresse.builder()
                .adresse("Tveterveien")
                .husnummer("2B")
                .gatekode("12345")
                .build();
        adresse.setFlyttedato(LocalDateTime.of(2018, 10, 10, 0, 0));
        adresse.setPostnr("1234");
        adresse.setKommunenr("5678");
        return Person.builder()
                .ident(IDENT)
                .identtype("FNR")
                .kjonn("M")
                .fornavn("GOD")
                .etternavn("MASKIN")
                .innvandretFraLand("SAU")
                .gtType("KNR")
                .gtVerdi("0617")
                .gtRegel("A")
                .sprakKode("NB")
                .sivilstand("GIFT")
                .boadresse(asList(adresse))
                .build();
    }
}