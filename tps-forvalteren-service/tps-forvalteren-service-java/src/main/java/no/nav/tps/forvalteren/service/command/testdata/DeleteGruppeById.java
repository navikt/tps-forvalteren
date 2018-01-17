package no.nav.tps.forvalteren.service.command.testdata;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import no.nav.tps.forvalteren.domain.jpa.Gruppe;
import no.nav.tps.forvalteren.domain.jpa.Person;
import no.nav.tps.forvalteren.repository.jpa.DoedsmeldingRepository;
import no.nav.tps.forvalteren.repository.jpa.GruppeRepository;
import no.nav.tps.forvalteren.repository.jpa.PersonRepository;

@Service
public class DeleteGruppeById {

    @Autowired
    private GruppeRepository gruppeRepository;

    @Autowired
    private PersonRepository personRepository;

    @Autowired
    private DeleteRelasjonerByIdIn deleteRelasjonerByIdIn;

    @Autowired
    private DoedsmeldingRepository doedsmeldingRepository;
    
    public void execute(Long gruppeId) {
        Gruppe gruppe = gruppeRepository.findById(gruppeId);
        List<Long> personIds = gruppe.getPersoner().stream()
                .map(Person::getId)
                .collect(Collectors.toList());
        doedsmeldingRepository.deleteByPersonIdIn(personIds);
        deleteRelasjonerByIdIn.execute(personIds);
        personRepository.deleteByGruppeId(gruppeId);
        gruppeRepository.deleteById(gruppeId);
    }
}
