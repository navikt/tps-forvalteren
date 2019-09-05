package no.nav.tps.forvalteren.service.command.testdata.restreq;

import static java.util.Objects.isNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import no.nav.tps.forvalteren.domain.jpa.Adresse;
import no.nav.tps.forvalteren.domain.jpa.Person;
import no.nav.tps.forvalteren.domain.jpa.Relasjon;
import no.nav.tps.forvalteren.repository.jpa.AdresseRepository;
import no.nav.tps.forvalteren.repository.jpa.DoedsmeldingRepository;
import no.nav.tps.forvalteren.repository.jpa.PersonRepository;
import no.nav.tps.forvalteren.repository.jpa.RelasjonRepository;
import no.nav.tps.forvalteren.service.IdentpoolService;
import no.nav.tps.forvalteren.service.command.exceptions.NotFoundException;

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
    private IdentpoolService identpoolService;

    @Transactional
    public void slettPersoner(List<String> identer) {

        List<String> alleIdenter = new ArrayList(identer);

        List<Long> personIds = personRepository.findByIdentIn(identer).stream().map(Person::getId).collect(Collectors.toList());
        if (isNull(personIds) || personIds.isEmpty()) {
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

        doedsmeldingRepository.deleteByPersonIdIn(personIds);
        personRepository.deleteByIdIn(personIds);

        identpoolService.recycleIdents(alleIdenter);
    }
}
