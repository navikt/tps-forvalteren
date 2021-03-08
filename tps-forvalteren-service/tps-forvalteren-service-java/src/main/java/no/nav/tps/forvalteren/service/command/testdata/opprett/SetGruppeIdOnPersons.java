package no.nav.tps.forvalteren.service.command.testdata.opprett;

import no.nav.tps.forvalteren.common.message.MessageProvider;
import no.nav.tps.forvalteren.domain.jpa.Gruppe;
import no.nav.tps.forvalteren.domain.jpa.Person;
import no.nav.tps.forvalteren.repository.jpa.GruppeRepository;
import no.nav.tps.forvalteren.service.command.exceptions.GruppeNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static no.nav.tps.forvalteren.common.message.MessageConstants.GRUPPE_NOT_FOUND_KEY;

@Service
public class SetGruppeIdOnPersons {

    @Autowired
    private MessageProvider messageProvider;

    @Autowired
    private GruppeRepository gruppeRepository;

    public void setGruppeId(List<Person> persons, Long gruppeId) {
        Gruppe gruppe = gruppeRepository.findById(gruppeId);
        if (gruppe == null) {
            throw new GruppeNotFoundException(messageProvider.get(GRUPPE_NOT_FOUND_KEY, gruppeId));
        }
        for (Person person : persons) {
            person.setGruppe(gruppe);
        }
    }

}
