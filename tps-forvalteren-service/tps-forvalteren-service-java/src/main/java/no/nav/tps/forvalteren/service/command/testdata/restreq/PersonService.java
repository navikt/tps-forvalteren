package no.nav.tps.forvalteren.service.command.testdata.restreq;

import static com.google.common.collect.Lists.partition;
import static java.util.stream.Collectors.toSet;
import static no.nav.tps.forvalteren.service.command.testdata.utils.TestdataConstants.ORACLE_MAX_IN_SET_ELEMENTS;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.transaction.Transactional;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import no.nav.tps.forvalteren.domain.jpa.Adresse;
import no.nav.tps.forvalteren.domain.jpa.Fullmakt;
import no.nav.tps.forvalteren.domain.jpa.IdentHistorikk;
import no.nav.tps.forvalteren.domain.jpa.InnvandretUtvandret;
import no.nav.tps.forvalteren.domain.jpa.MidlertidigAdresse;
import no.nav.tps.forvalteren.domain.jpa.Person;
import no.nav.tps.forvalteren.domain.jpa.Postadresse;
import no.nav.tps.forvalteren.domain.jpa.Relasjon;
import no.nav.tps.forvalteren.domain.jpa.Sivilstand;
import no.nav.tps.forvalteren.domain.jpa.Statsborgerskap;
import no.nav.tps.forvalteren.domain.jpa.Vergemaal;
import no.nav.tps.forvalteren.repository.jpa.AdresseRepository;
import no.nav.tps.forvalteren.repository.jpa.DoedsmeldingRepository;
import no.nav.tps.forvalteren.repository.jpa.FullmaktRepository;
import no.nav.tps.forvalteren.repository.jpa.IdenthistorikkRepository;
import no.nav.tps.forvalteren.repository.jpa.InnvandretUtvandretRepository;
import no.nav.tps.forvalteren.repository.jpa.MidlertidigAdresseRepository;
import no.nav.tps.forvalteren.repository.jpa.PersonRepository;
import no.nav.tps.forvalteren.repository.jpa.PostadresseRepository;
import no.nav.tps.forvalteren.repository.jpa.RelasjonRepository;
import no.nav.tps.forvalteren.repository.jpa.SivilstandRepository;
import no.nav.tps.forvalteren.repository.jpa.StatsborgerskapRepository;
import no.nav.tps.forvalteren.repository.jpa.VergemaalRepository;
import no.nav.tps.forvalteren.service.IdentpoolService;
import no.nav.tps.forvalteren.service.command.exceptions.NotFoundException;
import no.nav.tps.forvalteren.service.command.tps.skdmelding.TpsPersonService;

@Service
@RequiredArgsConstructor
public class PersonService {

    private final PersonRepository personRepository;
    private final AdresseRepository adresseRepository;
    private final PostadresseRepository postadresseRepository;
    private final MidlertidigAdresseRepository midlertidigAdresseRepository;
    private final RelasjonRepository relasjonRepository;
    private final DoedsmeldingRepository doedsmeldingRepository;
    private final IdenthistorikkRepository identhistorikkRepository;
    private final SivilstandRepository sivilstandRepository;
    private final IdentpoolService identpoolService;
    private final TpsPersonService tpsPersonService;
    private final VergemaalRepository vergemaalRepository;
    private final FullmaktRepository fullmaktRepository;
    private final InnvandretUtvandretRepository innvandretUtvandretRepository;
    private final StatsborgerskapRepository statsborgerskapRepository;

    public List<Person> getPersonerByIdenter(List<String> identer) {

        //Begrenser maks antall identer i SQL spørring
        List<List<String>> identLists = partition(identer, ORACLE_MAX_IN_SET_ELEMENTS);
        List<Person> resultat = new ArrayList<>(identer.size());
        for (List<String> subset : identLists) {
            resultat.addAll(personRepository.findByIdentIn(subset));
        }
        return resultat;
    }

    @Transactional
    public void deletePersons(List<String> miljoer, List<String> identer) {

        List<Person> persons = personRepository.findByIdentIn(new HashSet<>(identer));

        if (persons.isEmpty()) {
            throw new NotFoundException("Ingen personer funnet");
        }

        Set<Person> allConnectedPeople = Stream.of(
                persons,
                persons.stream()
                        .map(Person::getRelasjoner)
                        .flatMap(Collection::stream)
                        .map(Relasjon::getPersonRelasjonMed)
                        .collect(Collectors.toSet()),
                persons.stream()
                        .map(Person::getIdentHistorikk)
                        .flatMap(Collection::stream)
                        .map(IdentHistorikk::getAliasPerson)
                        .collect(Collectors.toSet()),
                persons.stream()
                        .map(Person::getFullmakt)
                        .flatMap(Collection::stream)
                        .map(Fullmakt::getFullmektig)
                        .collect(Collectors.toSet()),
                persons.stream()
                        .map(Person::getVergemaal)
                        .flatMap(Collection::stream)
                        .map(Vergemaal::getVerge)
                        .collect(Collectors.toSet()))
                .flatMap(Collection::stream)
                .collect(toSet());

        statsborgerskapRepository.deleteByIdIn(allConnectedPeople.stream()
                .map(Person::getStatsborgerskap)
                .flatMap(Collection::stream)
                .map(Statsborgerskap::getId)
                .collect(Collectors.toSet()));

        adresseRepository.deleteByIdIn(allConnectedPeople.stream()
                .map(Person::getBoadresse)
                .flatMap(Collection::stream)
                .map(Adresse::getId)
                .collect(Collectors.toSet()));

        postadresseRepository.deleteByIdIn(allConnectedPeople.stream()
                .map(Person::getPostadresse)
                .flatMap(Collection::stream)
                .map(Postadresse::getId)
                .collect(Collectors.toSet()));

        midlertidigAdresseRepository.deleteByIdIn(allConnectedPeople.stream()
                .map(Person::getMidlertidigAdresse)
                .flatMap(Collection::stream)
                .map(MidlertidigAdresse::getId)
                .collect(Collectors.toSet()));

        innvandretUtvandretRepository.deleteByIdIn(allConnectedPeople.stream()
                .map(Person::getInnvandretUtvandret)
                .flatMap(Collection::stream)
                .map(InnvandretUtvandret::getId)
                .collect(Collectors.toSet()));

        sivilstandRepository.deleteByIdIn(allConnectedPeople.stream()
                .map(Person::getSivilstander)
                .flatMap(Collection::stream)
                .map(Sivilstand::getId)
                .collect(Collectors.toSet()));

        relasjonRepository.deleteByIdIn(allConnectedPeople.stream()
                .map(Person::getRelasjoner)
                .flatMap(Collection::stream)
                .map(Relasjon::getPersonRelasjonMed)
                .map(Person::getRelasjoner)
                .flatMap(Collection::stream)
                .map(Relasjon::getId)
                .collect(Collectors.toSet()));

        identhistorikkRepository.deleteByIdIn(allConnectedPeople.stream()
                .map(Person::getIdentHistorikk)
                .flatMap(Collection::stream)
                .map(IdentHistorikk::getAliasPerson)
                .map(Person::getId)
                .collect(Collectors.toSet()));

        vergemaalRepository.deleteByIdIn(allConnectedPeople.stream()
                .map(Person::getVergemaal)
                .flatMap(Collection::stream)
                .map(Vergemaal::getId)
                .collect(Collectors.toSet()));

        fullmaktRepository.deleteByIdIn(allConnectedPeople.stream()
                .map(Person::getFullmakt)
                .flatMap(Collection::stream)
                .map(Fullmakt::getId)
                .collect(Collectors.toSet()));

        doedsmeldingRepository.deleteByPersonIdIn(allConnectedPeople.stream()
                .map(Person::getId)
                .collect(Collectors.toSet()));

        personRepository.deleteByIdIn(allConnectedPeople.stream()
                .map(Person::getId)
                .collect(Collectors.toSet()));

        //Wipe persons in TPS
        tpsPersonService.sendDeletePersonMeldinger(miljoer, allConnectedPeople.stream()
                .map(Person::getIdent)
                .collect(Collectors.toSet()));

        identpoolService.recycleIdents(allConnectedPeople.stream()
                .map(Person::getIdent)
                .collect(Collectors.toSet()));
    }

    @Transactional
    public void deleteIdenthistorikk(List<Person> persons) {

        Set<Long> identhistorikkIds = new HashSet();
        Set<Long> personIds = new HashSet();
        Set<String> idents = new HashSet();

        persons.forEach(person -> {
            person.getIdentHistorikk().forEach(identHistorikk -> {
                identhistorikkIds.add(identHistorikk.getId());
                identHistorikk.getAliasPerson().getIdentHistorikk().forEach(identHistorikk2 ->
                        identhistorikkIds.add(identHistorikk2.getId()));
                personIds.add(identHistorikk.getAliasPerson().getId());
                idents.add(identHistorikk.getAliasPerson().getIdent());
                identHistorikk.getAliasPerson().getIdentHistorikk().clear();
            });
            person.getIdentHistorikk().clear();
        });

        if (!identhistorikkIds.isEmpty()) {
            identhistorikkRepository.deleteByIdIn(identhistorikkIds);
            personRepository.deleteByIdIn(personIds);
            identpoolService.recycleIdents(idents);
        }
    }

    public List<Person> searchPerson(String request) {
        Optional<String> ident = Stream.of(request.split(" "))
                .filter(StringUtils::isNumeric)
                .findFirst();

        List<String> navn = List.of(request.split(" ")).stream()
                .filter(fragment -> StringUtils.isNotBlank(fragment) && !StringUtils.isNumeric(fragment))
                .collect(Collectors.toList());

        return personRepository.findByWildcardIdent(ident.orElse(null),
                !navn.isEmpty() ? navn.get(0).toUpperCase() : null,
                navn.size() > 1 ? navn.get(1).toUpperCase() : null,
                PageRequest.of(0, 10));
    }
}
