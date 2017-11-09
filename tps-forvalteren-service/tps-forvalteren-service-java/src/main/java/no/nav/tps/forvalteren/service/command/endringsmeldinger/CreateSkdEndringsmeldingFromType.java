package no.nav.tps.forvalteren.service.command.endringsmeldinger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import no.nav.tps.forvalteren.domain.jpa.SkdEndringsmelding;
import no.nav.tps.forvalteren.domain.jpa.SkdEndringsmeldingGruppe;
import no.nav.tps.forvalteren.domain.rs.skd.RsMeldingstype;
import no.nav.tps.forvalteren.domain.rs.skd.RsMeldingstype1Felter;
import no.nav.tps.forvalteren.domain.rs.skd.RsMeldingstype2Felter;
import no.nav.tps.forvalteren.domain.rs.skd.RsNewSkdEndringsmelding;
import no.nav.tps.forvalteren.repository.jpa.SkdEndringsmeldingGruppeRepository;
import no.nav.tps.forvalteren.repository.jpa.SkdEndringsmeldingRepository;

@Service
public class CreateSkdEndringsmeldingFromType {

    @Autowired
    private SkdEndringsmeldingRepository skdEndringsmeldingRepository;

    @Autowired
    private SkdEndringsmeldingGruppeRepository skdEndringsmeldingGruppeRepository;

    @Autowired
    private ObjectMapper mapper;

    public void execute(Long gruppeId, RsNewSkdEndringsmelding rsNewSkdEndringsmelding) {
        SkdEndringsmeldingGruppe gruppe = skdEndringsmeldingGruppeRepository.findById(gruppeId);
        if (gruppe != null) {
            SkdEndringsmelding skdEndringsmelding = new SkdEndringsmelding();
            skdEndringsmelding.setGruppe(gruppe);
            RsMeldingstype melding = getCorrectMeldingFromType(rsNewSkdEndringsmelding.getMeldingstype());
            melding.setBeskrivelse(rsNewSkdEndringsmelding.getNavn());
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

    private RsMeldingstype getCorrectMeldingFromType(String meldingstype) {
        if (meldingstype.equalsIgnoreCase("t1")) {
            return new RsMeldingstype1Felter();
        } else if (meldingstype.equalsIgnoreCase("t2")) {
            return new RsMeldingstype2Felter();
        } else {
            throw new IllegalArgumentException("Ugyldig meldingstype: " + meldingstype);
        }
    }

}
