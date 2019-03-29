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
import no.nav.tps.forvalteren.domain.jpa.Person;
import no.nav.tps.forvalteren.domain.jpa.Relasjon;
import no.nav.tps.forvalteren.repository.jpa.PersonRepository;
import no.nav.tps.forvalteren.service.command.exceptions.TpsfTechnicalException;

@Slf4j
@Service
public class ExcelService {

    @Autowired
    private PersonRepository personRepository;

    public Resource getPersonFile(List<String> identer) {

        //Begrenser maks antall identer i SQL spørring
        List<List<String>> identLists = partition(identer, 1000);
        List<Person> resultat = new ArrayList<>(identer.size());
        for (List<String> subset : identLists) {
            resultat.addAll(personRepository.findByIdentIn(subset));
        }

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
                .append(";\"\t")
                .append(person.getIdent())
                .append("\";")
                .append(person.getIdenttype())
                .append(';')
                .append(person.getKjonn())
                .append(';')
                .append(person.getSivilstand())
                .append(';')
                .append(person.getSpesreg())
                .append(';')
                .append(TRUE.equals(person.getUtenFastBopel()))
                .append(';')
                .append(TRUE.equals(nonNull(person.getEgenAnsattDatoFom())) && TRUE.equals(isNull(person.getEgenAnsattDatoTom())))
                .append(';')
                .append(person.getEtternavn())
                .append(';')
                .append(person.getFornavn())
                .append(';')
                .append(nonNull(person.getBoadresse()) ? ((Gateadresse) person.getBoadresse()).getAdresse() : null)
                .append(';')
                .append(nonNull(person.getBoadresse()) ? ((Gateadresse) person.getBoadresse()).getHusnummer() : null)
                .append(';')
                .append(nonNull(person.getBoadresse()) ? ((Gateadresse) person.getBoadresse()).getGatekode() : null)
                .append(';')
                .append(nonNull(person.getBoadresse()) ? person.getBoadresse().getPostnr() : null)
                .append(';')
                .append(nonNull(person.getBoadresse()) ? person.getBoadresse().getKommunenr() : null)
                .append(';')
                .append(nonNull(person.getBoadresse()) ? person.getBoadresse().getFlyttedato().format(ISO_LOCAL_DATE) : null)
                .append(';')
                .append(!person.getPostadresse().isEmpty() ? person.getPostadresse().get(0).getPostLinje1() : null)
                .append(';')
                .append(!person.getPostadresse().isEmpty() ? person.getPostadresse().get(0).getPostLinje2() : null)
                .append(';')
                .append(!person.getPostadresse().isEmpty() ? person.getPostadresse().get(0).getPostLinje3() : null)
                .append(';')
                .append(!person.getPostadresse().isEmpty() ? person.getPostadresse().get(0).getPostLand() : null)
                .append(';')
                .append(person.getInnvandretFraLand())
                .append(';')
                .append(person.getGtVerdi())
                .append(';')
                .append(person.getGtType())
                .append(';')
                .append(person.getGtRegel())
                .append(';')
                .append(person.getSprakKode())
                .append(';')
                .append(person.getStatsborgerskap())
                .append(';')
                .append(person.getTypeSikkerhetsTiltak())
                .append(';')
                .append(person.getBeskrSikkerhetsTiltak());

        for (Relasjon relasjon : person.getRelasjoner()) {
            row.append(';')
                    .append(relasjon.getRelasjonTypeNavn())
                    .append(";\"\t")
                    .append(relasjon.getPersonRelasjonMed().getIdent())
                    .append('\"');
        }

        return row.append(format("%n")).substring(1);
    }
}
