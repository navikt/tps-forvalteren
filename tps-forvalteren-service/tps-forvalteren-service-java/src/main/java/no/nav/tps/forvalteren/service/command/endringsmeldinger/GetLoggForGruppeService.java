package no.nav.tps.forvalteren.service.command.endringsmeldinger;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import no.nav.tps.forvalteren.domain.jpa.SkdEndringsmeldingLogg;
import no.nav.tps.forvalteren.repository.jpa.SkdEndringsmeldingLoggRepository;

@Service
public class GetLoggForGruppeService {

    @Autowired
    private SkdEndringsmeldingLoggRepository skdEndringsmeldingLoggRepository;

    public List<SkdEndringsmeldingLogg> execute(Long gruppeId) {
        return skdEndringsmeldingLoggRepository.findAllByMeldingsgruppeId(gruppeId);
    }
}
