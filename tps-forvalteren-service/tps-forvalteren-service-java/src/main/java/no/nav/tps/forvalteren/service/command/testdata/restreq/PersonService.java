package no.nav.tps.forvalteren.service.command.testdata.restreq;

import lombok.RequiredArgsConstructor;
import no.nav.tps.forvalteren.domain.jpa.Adresse;
import no.nav.tps.forvalteren.domain.jpa.Person;
import no.nav.tps.forvalteren.domain.jpa.Relasjon;
import no.nav.tps.forvalteren.domain.jpa.Sivilstand;
import no.nav.tps.forvalteren.repository.jpa.AdresseRepository;
import no.nav.tps.forvalteren.repository.jpa.DoedsmeldingRepository;
import no.nav.tps.forvalteren.repository.jpa.FullmaktRepository;
import no.nav.tps.forvalteren.repository.jpa.IdenthistorikkRepository;
import no.nav.tps.forvalteren.repository.jpa.PersonRepository;
import no.nav.tps.forvalteren.repository.jpa.RelasjonRepository;
import no.nav.tps.forvalteren.repository.jpa.SivilstandRepository;
import no.nav.tps.forvalteren.repository.jpa.VergemaalRepository;
import no.nav.tps.forvalteren.service.IdentpoolService;
import no.nav.tps.forvalteren.service.command.exceptions.NotFoundException;
import no.nav.tps.forvalteren.service.command.tps.skdmelding.TpsPersonService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.google.common.collect.Lists.partition;
import static java.util.Objects.nonNull;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toSet;
import static no.nav.tps.forvalteren.service.command.testdata.utils.TestdataConstants.ORACLE_MAX_IN_SET_ELEMENTS;

@Service
@RequiredArgsConstructor
public class PersonService {

    private final PersonRepository personRepository;
    private final AdresseRepository adresseRepository;
    private final RelasjonRepository relasjonRepository;
    private final DoedsmeldingRepository doedsmeldingRepository;
    private final IdenthistorikkRepository identhistorikkRepository;
    private final SivilstandRepository sivilstandRepository;
    private final IdentpoolService identpoolService;
    private final TpsPersonService tpsPersonService;
    private final VergemaalRepository vergemaalRepository;
    private final FullmaktRepository fullmaktRepository;

    public List<Person> getPersonerByIdenter(List<String> identer) {

        //Begrenser maks antall identer i SQL spørring
        List<List<String>> identLists = partition(identer, ORACLE_MAX_IN_SET_ELEMENTS);
        List<Person> resultat = new ArrayList<>(identer.size());
        for (List<String> subset : identLists) {
            resultat.addAll(personRepository.findByIdentIn(subset));
        }
        return resultat;
    }

    public Page<List<Person>> getPersonerByIdenterPaginert(List<String> identer, Integer pageNo, Integer pageSize) {

        return personRepository.findPersonByIdentInOrderByEndretDato(identer, PageRequest.of(pageNo, pageSize));
    }

    @Transactional
    public void deletePersons(List<String> miljoer, List<String> identer) {

        Set<String> alleIdenter = new HashSet<>(identer);

        List<Person> persons = personRepository.findByIdentIn(identer);

        Set<Long> orignalPersonId = persons.stream().map(Person::getId).collect(toSet());
        Set<Long> personIds = new HashSet(orignalPersonId);

        if (personIds.isEmpty()) {
            throw new NotFoundException("Ingen personer funnet");
        }

        Optional<List<Relasjon>> relasjoner = relasjonRepository.findByPersonRelasjonMedIdIn(personIds);
        if (relasjoner.isPresent()) {
            alleIdenter.addAll(relasjoner.get().stream().map(Relasjon::getPerson).map(Person::getIdent).collect(toList()));
            personIds.addAll(relasjoner.get().stream().map(Relasjon::getPerson).map(Person::getId).collect(toList()));

            Optional<List<Relasjon>> alleRelasjoner = relasjonRepository.findByPersonRelasjonMedIdIn(personIds);
            if (alleRelasjoner.isPresent()) {
                relasjonRepository.deleteByIdIn(alleRelasjoner.get().stream().map(Relasjon::getId).collect(toSet()));
            }

            Set<Long> sivilstandIds = alleRelasjoner.get().stream().filter(relasjon -> nonNull(relasjon.getPersonRelasjonMed()))
                    .map(Relasjon::getPersonRelasjonMed)
                    .flatMap(person -> person.getSivilstander().stream())
                    .map(Sivilstand::getId)
                    .collect(toSet());

            sivilstandRepository.deleteByIdIn(sivilstandIds);
        }

        Optional<List<Adresse>> adresser = adresseRepository.findAdresseByPersonIdIn(personIds);
        if (adresser.isPresent()) {
            adresseRepository.deleteByIdIn(adresser.get().stream().map(Adresse::getId).collect(toList()));
        }

        deleteIdenthistorikk(persons);

        vergemaalRepository.deleteByIdIn(personIds.stream().collect(toList()));

        fullmaktRepository.deleteByIdIn(personIds.stream().collect(toList()));

        doedsmeldingRepository.deleteByPersonIdIn(personIds);

        personRepository.deleteByIdIn(personIds);

        //Wipe persons in TPS
        tpsPersonService.sendDeletePersonMeldinger(miljoer, alleIdenter);

        identpoolService.recycleIdents(alleIdenter);
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
