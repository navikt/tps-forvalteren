package no.nav.tps.forvalteren.service.command.endringsmeldinger;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import no.nav.tps.forvalteren.domain.jpa.SkdEndringsmeldingGruppe;
import no.nav.tps.forvalteren.domain.rs.skd.RsSkdEndringsmeldingGruppe;
import no.nav.tps.forvalteren.repository.jpa.SkdEndringsmeldingGruppeRepository;

@Service
public class SkdEndringsmeldingsgruppeService {

    @Autowired
    private SkdEndringsmeldingGruppeRepository repository;

    public void save(SkdEndringsmeldingGruppe gruppe) {
        repository.save(gruppe);
    }

    public void deleteGruppeById(Long id) {
        repository.deleteById(id);
    }

    public SkdEndringsmeldingGruppe findGruppeById(Long gruppeId) {
        return repository.findById(gruppeId);
    }

    public List<SkdEndringsmeldingGruppe> findAllGrupper() {
        List<SkdEndringsmeldingGruppe> grupper = repository.findAllByOrderByIdAsc();

        grupper.forEach(gruppe -> gruppe.setSkdEndringsmeldinger(null));
        return grupper;
    }

    public RsSkdEndringsmeldingGruppe konfigurerKlonAvGruppe(SkdEndringsmeldingGruppe originalGruppe, String nyttNavn) {
        return RsSkdEndringsmeldingGruppe.builder()
                .beskrivelse(String.format("Klon av gruppe %s med id %d", originalGruppe.getNavn(), originalGruppe.getId()))
                .navn(nyttNavn)
                .build();
    }
}
