package no.nav.tps.forvalteren.service.command.testdata;

import no.nav.tps.forvalteren.domain.jpa.Gruppe;
import no.nav.tps.forvalteren.domain.jpa.Person;
import no.nav.tps.forvalteren.repository.jpa.GruppeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class DeleteGruppeById {

    @Autowired
    private GruppeRepository gruppeRepository;

    @Autowired
    private DeletePersonerByIdIn deletePersonerByIdIn;

    public void execute(Long gruppeId){
        Gruppe gruppe = gruppeRepository.findById(gruppeId);
        List<Long> personIds = gruppe.getPersoner().stream().map(Person::getId).collect(Collectors.toList());
        deletePersonerByIdIn.execute(personIds);
        gruppeRepository.deleteById(gruppeId);
    }
}
