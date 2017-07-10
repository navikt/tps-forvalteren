package no.nav.tps.forvalteren.service.command.testdata;

import no.nav.tps.forvalteren.common.java.message.MessageProvider;
import no.nav.tps.forvalteren.domain.jpa.Gruppe;
import no.nav.tps.forvalteren.repository.jpa.GruppeRepository;
import no.nav.tps.forvalteren.service.command.exceptions.GruppeNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static no.nav.tps.forvalteren.common.java.message.MessageConstants.GRUPPE_NOT_FOUND_KEY;

@Service
public class FindGruppeById {

    @Autowired
    private MessageProvider messageProvider;

    @Autowired
    private GruppeRepository gruppeRepository;

    public Gruppe execute(Long gruppeId){
        Gruppe gruppe = gruppeRepository.findById(gruppeId);

        if(gruppe == null){
            throw new GruppeNotFoundException(messageProvider.get(GRUPPE_NOT_FOUND_KEY, gruppeId));
        }

        return gruppe;
    }
}
