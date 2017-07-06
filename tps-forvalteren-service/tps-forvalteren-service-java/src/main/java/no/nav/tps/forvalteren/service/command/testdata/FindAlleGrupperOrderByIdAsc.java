package no.nav.tps.forvalteren.service.command.testdata;

import no.nav.tps.forvalteren.domain.jpa.Gruppe;
import no.nav.tps.forvalteren.repository.jpa.GruppeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FindAlleGrupperOrderByIdAsc {

    @Autowired
    private GruppeRepository gruppeRepository;

    public List<Gruppe> execute(){
        return gruppeRepository.findAllByOrderByIdAsc();
    }
}
