package no.nav.tps.forvalteren.service.command.testdata.restreq;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import no.nav.tps.forvalteren.domain.jpa.Adresse;
import no.nav.tps.forvalteren.repository.jpa.AdresseRepository;
import no.nav.tps.forvalteren.repository.jpa.PersonRepository;
import no.nav.tps.forvalteren.service.IdentpoolService;

@Service
public class PersonService {

    @Autowired
    private PersonRepository personRepository;

    @Autowired
    private AdresseRepository adresseRepository;

    @Autowired
    private IdentpoolService identpoolService;

    @Transactional
    public void slettPersoner(List<String> identer) {

        identpoolService.recycleIdents(identer);

        Optional<List<Adresse>> adresser = adresseRepository.findAdresseByPersonIdentIn(identer);
        if (adresser.isPresent()) {
            adresseRepository.deleteByIdIn(adresser.get().stream().map(Adresse::getId).collect(Collectors.toList()));
        }

        personRepository.deleteByIdentIn(identer);
    }
}
