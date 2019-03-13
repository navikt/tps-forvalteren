package no.nav.tps.forvalteren.service.command.testdata;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;
import static no.nav.tps.forvalteren.service.command.testdata.opprett.DummyAdresseUtil.createDummyAdresse;
import static no.nav.tps.forvalteren.service.command.tps.skdmelding.skdparam.utils.NullcheckUtil.nullcheckSetDefaultValue;

import java.util.Set;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import no.nav.tps.forvalteren.domain.jpa.Person;
import no.nav.tps.forvalteren.domain.jpa.Postadresse;
import no.nav.tps.forvalteren.repository.jpa.AdresseRepository;
import no.nav.tps.forvalteren.repository.jpa.PersonRepository;
import no.nav.tps.forvalteren.repository.jpa.RelasjonRepository;
import no.nav.tps.forvalteren.service.command.testdata.utils.HentDatoFraIdentService;
import no.nav.tps.forvalteren.service.command.testdata.utils.HentUtdaterteRelasjonIder;
import no.nav.tps.forvalteren.service.command.testdata.utils.OppdaterRelasjonReferanser;

@Service
public class UpdatePersonService {

    @Autowired
    private PersonRepository personRepository;

    @Autowired
    private AdresseRepository adresseRepository;

    @Autowired
    private RelasjonRepository relasjonRepository;

    @Autowired
    private OppdaterRelasjonReferanser oppdaterRelasjonReferanser;

    @Autowired
    private HentUtdaterteRelasjonIder hentUtdaterteRelasjonIder;

    @Autowired
    private UpdateAdresseOgSpesregForUfb updateAdresseOgSpesregForUfb;

    @Autowired
    private HentDatoFraIdentService hentDatoFraIdentService;

    @Transactional
    public void update(Person person) {

        Set<Long> utdaterteRelasjonIder = null;

        Person storedPerson = personRepository.findById(person.getId());
        if (nonNull(storedPerson)) {
            oppdaterRelasjonReferanser.execute(person, storedPerson);
            utdaterteRelasjonIder = hentUtdaterteRelasjonIder.execute(person, storedPerson);
            adresseRepository.deleteAllByPerson(storedPerson);
            updateAdresseOgSpesregForUfb.updateAttributesForUfb(person, storedPerson);
        }

        if (isNull(person.getBoadresse())) {
            person.setBoadresse(createDummyAdresse());
        }

        person.getBoadresse().setFlyttedato(
                nullcheckSetDefaultValue(person.getBoadresse().getFlyttedato(), hentDatoFraIdentService.extract(person.getIdent())));
        person.getBoadresse().setPerson(person);

        if (nonNull(person.getPostadresse())) {
            for (Postadresse adr : person.getPostadresse()) {
                adr.setPerson(person);
            }
        }

        if (nonNull(utdaterteRelasjonIder)) {
            relasjonRepository.deleteByIdIn(utdaterteRelasjonIder);
        }

        personRepository.save(person);
    }
}
