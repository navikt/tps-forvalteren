package no.nav.tps.forvalteren.service.command.tps.skdmelding.skdparam.utils;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import lombok.experimental.UtilityClass;
import no.nav.tps.forvalteren.domain.jpa.Person;
import no.nav.tps.forvalteren.domain.jpa.Relasjon;

@UtilityClass
public class ForeldreStrategy {

    /**
     * Decides unambigous parenthood across classes, mother first on ascending age
     */

    public List<Person> getEntydigeForeldre(List<Relasjon> relasjoner) {

        List<Person> foreldreMor = relasjoner.stream().filter(Relasjon::isMor)
                .map(Relasjon::getPersonRelasjonMed)
                .sorted(Comparator.comparing(Person::getIdent))
                .collect(Collectors.toList());
        List<Person> foreldreFar = relasjoner.stream().filter(Relasjon::isFar)
                .map(Relasjon::getPersonRelasjonMed)
                .sorted(Comparator.comparing(Person::getIdent))
                .collect(Collectors.toList());

        List<Person> foreldre = new ArrayList();
        Stream.of(foreldreMor, foreldreFar).forEach(foreldre::addAll);

        return foreldre;
    }
}
