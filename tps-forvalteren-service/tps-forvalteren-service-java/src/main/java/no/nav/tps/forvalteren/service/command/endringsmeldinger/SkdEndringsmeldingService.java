package no.nav.tps.forvalteren.service.command.endringsmeldinger;

import static no.nav.tps.forvalteren.service.command.testdata.utils.TestdataConstants.ORACLE_MAX_IN_SET_ELEMENTS;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.common.collect.Lists;

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
    
    public void deleteById(List<Long> ids) {
        if (ids.size() > ORACLE_MAX_IN_SET_ELEMENTS) {
            List<List<Long>> partitionsIds = Lists.partition(ids, 1000);
            for (List<Long> partition : partitionsIds) {
                skdEndringsmeldingRepository.deleteByIdIn(partition);
            }
        } else {
            skdEndringsmeldingRepository.deleteByIdIn(ids);
        }
    }
}
