package no.nav.tps.forvalteren.service.command.endringsmeldinger;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.google.common.collect.Lists;

import no.nav.tps.forvalteren.domain.jpa.SkdEndringsmelding;
import no.nav.tps.forvalteren.domain.jpa.SkdEndringsmeldingGruppe;
import no.nav.tps.forvalteren.repository.jpa.SkdEndringsmeldingGruppeRepository;
import no.nav.tps.forvalteren.service.command.exceptions.NotFoundException;

@Service
public class GetMeldingIdFraGruppeService {

    @Autowired
    private SkdEndringsmeldingGruppeRepository endringsmeldingGruppeRepository;

    public List<Long> execute(Long id) {

        SkdEndringsmeldingGruppe skdEndringsmeldingGruppe = endringsmeldingGruppeRepository.findById(id);

        if (skdEndringsmeldingGruppe == null) {
            throw new NotFoundException(String.format("Gruppe %d finnes ikke.", id));
        }

        List<Long> idListe = Lists.newArrayListWithExpectedSize(skdEndringsmeldingGruppe.getSkdEndringsmeldinger().size());
        for(SkdEndringsmelding melding : skdEndringsmeldingGruppe.getSkdEndringsmeldinger()) {
            idListe.add(melding.getId());
        }

        return idListe;
    }
}