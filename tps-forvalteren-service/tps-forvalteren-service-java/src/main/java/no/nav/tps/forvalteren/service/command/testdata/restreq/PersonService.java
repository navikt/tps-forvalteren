package no.nav.tps.forvalteren.service.command.testdata.restreq;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import no.nav.tps.forvalteren.domain.jpa.Adresse;
import no.nav.tps.forvalteren.domain.jpa.Person;
import no.nav.tps.forvalteren.domain.jpa.Relasjon;
import no.nav.tps.forvalteren.repository.jpa.AdresseRepository;
import no.nav.tps.forvalteren.repository.jpa.DoedsmeldingRepository;
import no.nav.tps.forvalteren.repository.jpa.IdenthistorikkRepository;
import no.nav.tps.forvalteren.repository.jpa.PersonRepository;
import no.nav.tps.forvalteren.repository.jpa.RelasjonRepository;
import no.nav.tps.forvalteren.service.IdentpoolService;
import no.nav.tps.forvalteren.service.command.exceptions.NotFoundException;
import no.nav.tps.forvalteren.service.command.tps.skdmelding.TpsPersonService;

@Service
public class PersonService {

    @Autowired
    private PersonRepository personRepository;

    @Autowired
    private AdresseRepository adresseRepository;

    @Autowired
    private RelasjonRepository relasjonRepository;

    @Autowired
    private DoedsmeldingRepository doedsmeldingRepository;

    @Autowired
    private IdenthistorikkRepository identhistorikkRepository;

    @Autowired
    private IdentpoolService identpoolService;

    @Autowired
    private TpsPersonService tpsPersonService;

    @Transactional
    public void deletePersons(List<String> miljoer, List<String> identer) {

        Set<String> alleIdenter = new HashSet<>(identer);

        List<Person> persons = personRepository.findByIdentIn(identer);

        Set<Long> orignalPersonId = persons.stream().map(Person::getId).collect(Collectors.toSet());
        Set<Long> personIds = new HashSet(orignalPersonId);

        if (personIds.isEmpty()) {
            throw new NotFoundException("Ingen personer funnet");
        }

        Optional<List<Relasjon>> relasjoner = relasjonRepository.findByPersonRelasjonMedIdIn(personIds);
        if (relasjoner.isPresent()) {
            alleIdenter.addAll(relasjoner.get().stream().map(Relasjon::getPerson).map(Person::getIdent).collect(Collectors.toList()));
            personIds.addAll(relasjoner.get().stream().map(Relasjon::getPerson).map(Person::getId).collect(Collectors.toList()));

            Optional<List<Relasjon>> alleRelasjoner = relasjonRepository.findByPersonRelasjonMedIdIn(personIds);
            if (alleRelasjoner.isPresent()) {
                relasjonRepository.deleteByIdIn(alleRelasjoner.get().stream().map(Relasjon::getId).collect(Collectors.toSet()));
            }
        }

        Optional<List<Adresse>> adresser = adresseRepository.findAdresseByPersonIdIn(personIds);
        if (adresser.isPresent()) {
            adresseRepository.deleteByIdIn(adresser.get().stream().map(Adresse::getId).collect(Collectors.toList()));
        }

        deleteIdenthistorikk(persons);

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
}
