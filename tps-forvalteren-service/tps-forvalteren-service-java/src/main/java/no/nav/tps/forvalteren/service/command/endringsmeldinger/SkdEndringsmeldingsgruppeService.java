package no.nav.tps.forvalteren.service.command.endringsmeldinger;

import static no.nav.tps.forvalteren.service.command.endringsmeldinger.SkdEndringsmeldingService.ANTALL_MELDINGER_PER_PAGE;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import ma.glasnost.orika.MapperFacade;
import no.nav.tps.forvalteren.domain.jpa.SkdEndringsmeldingGruppe;
import no.nav.tps.forvalteren.domain.rs.skd.RsSkdEndringsmeldingGruppe;
import no.nav.tps.forvalteren.repository.jpa.SkdEndringsmeldingGruppeRepository;
import no.nav.tps.forvalteren.repository.jpa.SkdEndringsmeldingRepository;

@Service
@RequiredArgsConstructor
public class SkdEndringsmeldingsgruppeService {

    private final SkdEndringsmeldingGruppeRepository endringsmeldingGruppeRepository;
    private final SkdEndringsmeldingRepository endringsmeldingRepository;
    private final MapperFacade mapperFacade;

    public void save(SkdEndringsmeldingGruppe gruppe) {
        endringsmeldingGruppeRepository.save(gruppe);
    }

    public void deleteGruppeById(Long id) {
        endringsmeldingGruppeRepository.deleteById(id);
    }

    public SkdEndringsmeldingGruppe findGruppeById(Long gruppeId) {
        return endringsmeldingGruppeRepository.findById(gruppeId);
    }

    public List<RsSkdEndringsmeldingGruppe> findAllGrupper() {

        List<SkdEndringsmeldingGruppe> grupper = endringsmeldingGruppeRepository.findAllByOrderByIdAsc();
        grupper.forEach(gruppe -> gruppe.setSkdEndringsmeldinger(null));

        Map<Long, Long> forekomster = endringsmeldingRepository.countAllBySkdEndringsmeldingGruppeId().stream()
                .collect(Collectors.toMap(forekomst -> forekomst.getGruppe(), forekomst -> forekomst.getAntall()));

        return grupper.stream().map(gruppe -> {
            RsSkdEndringsmeldingGruppe endringsmeldingGruppe = mapperFacade.map(gruppe, RsSkdEndringsmeldingGruppe.class);
            endringsmeldingGruppe.setAntallSider(
                    forekomster.getOrDefault(gruppe.getId(), 0L) / ANTALL_MELDINGER_PER_PAGE + 1);
            return endringsmeldingGruppe;
        })
                .collect(Collectors.toList());
    }

    public RsSkdEndringsmeldingGruppe konfigurerKlonAvGruppe(SkdEndringsmeldingGruppe originalGruppe, String nyttNavn) {
        return RsSkdEndringsmeldingGruppe.builder()
                .beskrivelse(String.format("Klon av gruppe %s med id %d", originalGruppe.getNavn(), originalGruppe.getId()))
                .navn(nyttNavn)
                .build();
    }
}
