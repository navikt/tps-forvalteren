package no.nav.tps.forvalteren.service.command.endringsmeldinger;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import no.nav.tps.forvalteren.repository.jpa.SkdEndringsmeldingRepository;

@Service
public class DeleteSkdEndringsmeldingByIdIn {

    @Autowired
    private SkdEndringsmeldingRepository repository;

    public void execute(List<Long> ids) {
        repository.deleteByIdIn(ids);
    }
}
