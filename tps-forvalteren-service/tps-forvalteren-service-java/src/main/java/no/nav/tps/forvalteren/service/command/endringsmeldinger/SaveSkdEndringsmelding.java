package no.nav.tps.forvalteren.service.command.endringsmeldinger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import no.nav.tps.forvalteren.domain.jpa.SkdEndringsmelding;
import no.nav.tps.forvalteren.domain.jpa.SkdEndringsmeldingGruppe;
import no.nav.tps.forvalteren.domain.rs.skd.RsMeldingstype;
import no.nav.tps.forvalteren.repository.jpa.SkdEndringsmeldingGruppeRepository;
import no.nav.tps.forvalteren.repository.jpa.SkdEndringsmeldingRepository;

@Service
public class SaveSkdEndringsmelding {

    @Autowired
    private SkdEndringsmeldingRepository skdEndringsmeldingRepository;

    @Autowired
    private SkdEndringsmeldingGruppeRepository skdEndringsmeldingGruppeRepository;

    @Autowired
    private ObjectMapper mapper;

    public void execute(Long gruppeId, RsMeldingstype melding) {
        SkdEndringsmeldingGruppe gruppe = skdEndringsmeldingGruppeRepository.findById(gruppeId);
        if (gruppe != null) {
            SkdEndringsmelding skdEndringsmelding = new SkdEndringsmelding();
            skdEndringsmelding.setGruppe(gruppe);
            String meldingAsJson = "";
            try {
                meldingAsJson = mapper.writeValueAsString(melding);
            } catch (JsonProcessingException e) {
                e.printStackTrace(); // TODO: kast ny exception
            }
            skdEndringsmelding.setEndringsmelding(meldingAsJson);
            skdEndringsmeldingRepository.save(skdEndringsmelding);
        } else {
            throw new IllegalArgumentException("skdEndringsmeldingGruppe med id: " + gruppeId + " finnes ikke.");
        }
    }

}
