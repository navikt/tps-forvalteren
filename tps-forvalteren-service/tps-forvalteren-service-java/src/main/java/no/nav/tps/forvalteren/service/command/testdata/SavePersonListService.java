package no.nav.tps.forvalteren.service.command.testdata;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import no.nav.tps.forvalteren.domain.jpa.Person;
import no.nav.tps.forvalteren.domain.jpa.Postadresse;
import no.nav.tps.forvalteren.repository.jpa.AdresseRepository;
import no.nav.tps.forvalteren.repository.jpa.PersonRepository;
import no.nav.tps.forvalteren.repository.jpa.RelasjonRepository;
import no.nav.tps.forvalteren.service.command.testdata.utils.HentUtdaterteRelasjonIder;
import no.nav.tps.forvalteren.service.command.testdata.utils.OppdaterRelasjonReferanser;

@Service
public class SavePersonListService {

    @Autowired
    private PersonRepository personRepository;

    @Autowired
    private AdresseRepository adresseRepository;

    @Autowired
    private RelasjonRepository relasjonRepository;

    @Autowired
    private UppercaseDataInPerson uppercaseDataInPerson;

    @Autowired
    private OppdaterRelasjonReferanser oppdaterRelasjonReferanser;

    @Autowired
    private HentUtdaterteRelasjonIder hentUtdaterteRelasjonIder;

    @Transactional
    public void execute(List<Person> personer) {

        for (Person person : personer) {
            Set<Long> utdaterteRelasjonIder = new HashSet<>();

            Person personDb = personRepository.findById(person.getId());
            if (personDb != null) {
                oppdaterRelasjonReferanser.execute(person, personDb);//dette er vel mer synkronisering av relasjonsreferanser enn opprettelse?
                utdaterteRelasjonIder = hentUtdaterteRelasjonIder.execute(person, personDb); //denne itererer gjennom de samme relasjonene på samme måte som linjen ovenfor, men i stedet for å sette id fra relasjonDB til person.getRelasjon på de relasjoner de har felles,så returnerer den id til de relasjoner som den ikke har felles. Derfor kan disse to metoden samles til én. Begge metodene blir kun brukt her. TODO refaktureringsoppgave for å samle disse to metodene til en metode for synkronisering av relasjoner.
                adresseRepository.deleteAllByPerson(personDb);
            }

            uppercaseDataInPerson.execute(person);
            if (person.getPostadresse() != null) {
                for (Postadresse adr : person.getPostadresse()) {
                    adr.setPerson(person);
                }
            }

            if (person.getBoadresse() != null) {
                person.getBoadresse().setPerson(person);
            }

            if (!utdaterteRelasjonIder.isEmpty()) {
                relasjonRepository.deleteByIdIn(utdaterteRelasjonIder);
            }

            personRepository.save(person);
        }

    }

}