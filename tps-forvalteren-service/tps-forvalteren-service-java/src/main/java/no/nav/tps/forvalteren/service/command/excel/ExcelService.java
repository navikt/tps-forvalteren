package no.nav.tps.forvalteren.service.command.excel;

import static com.google.common.base.Charsets.UTF_8;
import static com.google.common.collect.Lists.partition;
import static java.lang.Boolean.TRUE;
import static java.lang.String.format;
import static java.time.format.DateTimeFormatter.ISO_LOCAL_DATE;
import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;
import no.nav.tps.forvalteren.domain.jpa.Gateadresse;
import no.nav.tps.forvalteren.domain.jpa.Matrikkeladresse;
import no.nav.tps.forvalteren.domain.jpa.Person;
import no.nav.tps.forvalteren.domain.jpa.Relasjon;
import no.nav.tps.forvalteren.service.command.exceptions.TpsfTechnicalException;
import no.nav.tps.forvalteren.service.command.testdata.restreq.PersonService;

@Slf4j
@Service
public class ExcelService {

    private static final char SEP = ';';
    private static final String SEP_STRING_START = ";\"\t";
    private static final String SEP_STRING_END = "\";";
    private static final String SEP_STRING_DUAL = "\";\"\t";

    @Autowired
    private PersonService personService;

    public Resource getPersonFile(List<String> identer) {

        //Begrenser maks antall identer i SQL spørring
        List<List<String>> identLists = partition(identer, 1000);
        List<Person> resultat = new ArrayList<>(identer.size());

        identLists.forEach(subset ->
                resultat.addAll(personService.getPersonerByIdenter(subset)));

        try {
            File excelFile = File.createTempFile("Excel-", ".csv");
            try (Writer writer = new OutputStreamWriter(new FileOutputStream(excelFile), UTF_8)) {
                writer.write(getHeader());
                for (Person person : resultat) {
                    writer.write(buildPersonRow(person));
                }
            }

            return new FileSystemResource(excelFile);

        } catch (IOException e) {
            log.error(e.getMessage(), e);
            throw new TpsfTechnicalException("Generering av Excel-fil feilet", e);
        }
    }

    private static String getHeader() {
        return format("Ident;Identtype;Kjønn;Sivilstand;Diskresjonskode;ErUtenFastBopel;Egenansatt;"
                + "Etternavn;Fornavn;Gateadresse;Husnummer;Gatekode;Postnr;Kommunenr;Flyttedato;"
                + "Postlinje1;Postlinje2;Postlinje3;Postland;InnvandretFraLand;GtVerdi;GtType;GtRegel;"
                + "Språkkode;Statsborgerskap;TypeSikkerhetTiltak;BeskrivelseSikkerhetTiltak;"
                + "Relasjon1-Type;Relasjon1-Ident;Relasjon2-Type;Relasjon2-Ident;Relasjon3-Type;Relasjon3-Ident%n");
    }

    private static String buildPersonRow(Person person) {
        StringBuilder row = new StringBuilder();
        row.append(buildPerson(person));
        for (Relasjon relasjon : person.getRelasjoner()) {
            row.append(buildPerson(relasjon.getPersonRelasjonMed()));
        }
        return row.toString().replaceAll("null", "");
    }

    private static String buildPerson(Person person) {
        StringBuilder row = new StringBuilder()
                .append(SEP_STRING_START)
                .append(person.getIdent())
                .append(SEP_STRING_END)
                .append(person.getIdenttype()).append(SEP)
                .append(person.getKjonn()).append(SEP)
                .append(person.getSivilstand()).append(SEP)
                .append(person.getSpesreg()).append(SEP)
                .append(isUtenFastBobel(person)).append(SEP)
                .append(isEgenAnsatt(person)).append(SEP)
                .append(person.getEtternavn()).append(SEP)
                .append(person.getFornavn()).append(SEP)
                .append(getBoadresse(person)).append(SEP)
                .append(getPostadresse(person)).append(SEP)
                .append(person.getInnvandretFraLand())
                .append(SEP_STRING_START)
                .append(person.getGtVerdi())
                .append(SEP_STRING_END)
                .append(person.getGtType()).append(SEP)
                .append(person.getGtRegel()).append(SEP)
                .append(person.getSprakKode()).append(SEP)
                .append(!person.getStatsborgerskap().isEmpty() ? person.getStatsborgerskap().get(0).getStatsborgerskap() : "").append(SEP)
                .append(person.getTypeSikkerhetsTiltak()).append(SEP)
                .append(person.getBeskrSikkerhetsTiltak())
                .append(getRelasjoner(person));

        return row.append(format("%n")).substring(1);
    }

    private static String getRelasjoner(Person person) {
        StringBuilder row = new StringBuilder();
        for (Relasjon relasjon : person.getRelasjoner()) {
            row.append(SEP)
                    .append(relasjon.getRelasjonTypeNavn())
                    .append(SEP_STRING_START)
                    .append(relasjon.getPersonRelasjonMed().getIdent())
                    .append('\"');
        }
        return row.toString();
    }

    private static boolean isUtenFastBobel(Person person) {
        return TRUE.equals(person.getUtenFastBopel());
    }

    private static boolean isEgenAnsatt(Person person) {
        return TRUE.equals(nonNull(person.getEgenAnsattDatoFom())) && TRUE.equals(isNull(person.getEgenAnsattDatoTom()));
    }

    private static String getBoadresse(Person person) {

        if (!person.getBoadresse().isEmpty()) {
            return person.getBoadresse().get(0) instanceof Gateadresse ?
                    getGateadresse(person) : getMatrikkeladresse(person);
        } else {
            return ";;;;;";
        }
    }

    private static String getGateadresse(Person person) {
        return new StringBuilder()
                .append(((Gateadresse) person.getBoadresse().get(0)).getAdresse())
                .append(SEP)
                .append(((Gateadresse) person.getBoadresse().get(0)).getHusnummer())
                .append(SEP_STRING_START)
                .append(((Gateadresse) person.getBoadresse().get(0)).getGatekode())
                .append(SEP_STRING_DUAL)
                .toString()
                .concat(commonAdresse(person));
    }

    private static String getMatrikkeladresse(Person person) {
        return !person.getBoadresse().isEmpty() ?
                new StringBuilder()
                        .append(((Matrikkeladresse) person.getBoadresse().get(0)).getMellomnavn())
                        .append(";GNR: ")
                        .append(((Matrikkeladresse) person.getBoadresse().get(0)).getGardsnr())
                        .append(";BNR: ")
                        .append(((Matrikkeladresse) person.getBoadresse().get(0)).getBruksnr())
                        .append(SEP_STRING_START)
                        .toString()
                        .concat(commonAdresse(person))

                : format("%s%s%s%s%s", SEP, SEP, SEP, SEP, SEP);
    }

    private static String getPostadresse(Person person) {
        return !person.getPostadresse().isEmpty() ?
                new StringBuilder()
                        .append(person.getPostadresse().get(0).getPostLinje1())
                        .append(SEP)
                        .append(person.getPostadresse().get(0).getPostLinje2())
                        .append(SEP)
                        .append(person.getPostadresse().get(0).getPostLinje3())
                        .append(SEP)
                        .append(person.getPostadresse().get(0).getPostLand())
                        .toString()
                : format("%s%s%s", SEP, SEP, SEP);
    }

    private static String commonAdresse(Person person) {
        return new StringBuilder()
                .append(person.getBoadresse().get(0).getPostnr())
                .append(SEP_STRING_DUAL)
                .append(person.getBoadresse().get(0).getKommunenr())
                .append(SEP_STRING_END)
                .append(person.getBoadresse().get(0).getFlyttedato().format(ISO_LOCAL_DATE))
                .toString();
    }
}
