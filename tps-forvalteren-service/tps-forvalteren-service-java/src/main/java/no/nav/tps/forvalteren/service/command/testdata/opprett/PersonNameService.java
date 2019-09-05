package no.nav.tps.forvalteren.service.command.testdata.opprett;

import static java.lang.Boolean.TRUE;

import java.security.SecureRandom;
import java.util.List;
import org.springframework.stereotype.Service;

import no.nav.tps.forvalteren.domain.jpa.Person;

@Service
public class PersonNameService {

    private static final String[] fornavn = { "Blå", "Gul", "Grønn", "Rask", "Døll", "Nydelig", "Artig", "Stor", "Kriminell", "Luguber", "Smekker", "Gøyal", "Raus", "Naturlig", "Smart", "Kreativ", "God",
            "Vakker", "Snill", "Lunken", "Absurd", "Robust", "Blåøyd", "Tykkmaget", "Sedat", "Korrupt", "Godslig", "Nobel", "Slapp", "Triviell", "Talentfull", "Tvilsom", "Frodig", "Treig", "Smidig", "Kløktig", "Lur",
            "Liten", "Lealaus", "Tungsindig", "Kraftig", "Sterk", "Forfjamset", "Rakrygget"};

    private static final String[] mellomnavn = { "Skjelende", "Tvilende", "Leende", "Gryntende", "Måpende", "Famlende", "Gyngende", "Skjelvende", "Vaklende", "Hikkende", "Overstrålende", "Fallende", "Lurende",
            "Tikkende", "Flakkende", "Blunkende", "Skravlende", "Fraværende", "Ferierende", "Drøvtyggende", "Sprellende", "Tiltalende", "Sløvende", "Utmattende", "Glitrende" };

    private static final String[] etternavn = { "Ert", "Hest", "Dorull", "Hatt", "Maskin", "Kaffi", "Kake", "Potet", "Konsoll", "Bærepose", "Blyant", "Penn", "Bolle", "Saks", "Kopp", "Skilpadde", "Busk", "Nordmann",
            "Veggpryd", "Lapp", "Knott", "Mygg", "Veps", "Staude", "Midtpunkt", "Gaselle", "Tøffeldyr", "Sekk", "Snerk", "Trane", "Floskel", "Bamse", "Rhododenron", "Rispbærbusk", "Kameleon",
            "Taremel", "Tranflaske", "Muldvarp" };

    private static SecureRandom randGenerator = new SecureRandom();

    public void execute(Person person, Boolean harMellomnavn) {

        person.setFornavn(randomFornavn());
        person.setMellomnavn(TRUE.equals(harMellomnavn) ? randomMellomnavn() : null);
        person.setEtternavn(randomEtternavn());
    }

    public void execute(List<Person> personer) {

        for (Person person : personer) {
            execute(person, null);
        }
    }

    private static String randomFornavn() {
        return fornavn[randGenerator.nextInt(fornavn.length)];
    }

    private static String randomMellomnavn() {
        return mellomnavn[randGenerator.nextInt(mellomnavn.length)];
    }

    private static String randomEtternavn() {
        return etternavn[randGenerator.nextInt(etternavn.length)];
    }
}
