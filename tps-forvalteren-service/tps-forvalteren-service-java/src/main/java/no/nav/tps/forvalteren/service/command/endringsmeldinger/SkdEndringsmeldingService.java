package no.nav.tps.forvalteren.service.command.endringsmeldinger;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import no.nav.tps.forvalteren.domain.jpa.SkdEndringsmelding;
import no.nav.tps.forvalteren.domain.jpa.SkdEndringsmeldingGruppe;
import no.nav.tps.forvalteren.repository.jpa.SkdEndringsmeldingGruppeRepository;
import no.nav.tps.forvalteren.repository.jpa.SkdEndringsmeldingRepository;

@Service
public class SkdEndringsmeldingService {
    
    @Autowired
    private SkdEndringsmeldingRepository skdEndringsmeldingRepository;
    
    @Autowired
    private SkdEndringsmeldingGruppeRepository gruppeRepository;
    
    public Set<String> filtrerIdenterPaaAarsakskodeOgTransaksjonstype(Long gruppeId, List<String> aarsakskoder, String transaksjonstype) {
        SkdEndringsmeldingGruppe gruppe = gruppeRepository.findById(gruppeId);
        List<SkdEndringsmelding> meldinger = skdEndringsmeldingRepository.findByAarsakskodeInAndTransaksjonstypeAndGruppe(aarsakskoder, transaksjonstype, gruppe);
        return meldinger.stream().map(SkdEndringsmelding::getFoedselsnummer).collect(Collectors.toSet());
    }
}
