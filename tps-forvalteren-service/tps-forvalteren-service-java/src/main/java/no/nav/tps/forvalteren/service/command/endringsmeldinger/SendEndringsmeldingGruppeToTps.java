package no.nav.tps.forvalteren.service.command.endringsmeldinger;

import static no.nav.tps.forvalteren.common.java.message.MessageConstants.SKD_ENDRINGSMELDING_GRUPPE_NOT_FOUND;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;
import javax.persistence.Column;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.fasterxml.jackson.databind.ObjectMapper;

import no.nav.tps.forvalteren.common.java.message.MessageProvider;
import no.nav.tps.forvalteren.domain.jpa.SkdEndringsmelding;
import no.nav.tps.forvalteren.domain.jpa.SkdEndringsmeldingGruppe;
import no.nav.tps.forvalteren.domain.jpa.SkdEndringsmeldingLogg;
import no.nav.tps.forvalteren.domain.rs.skd.RsMeldingstype;
import no.nav.tps.forvalteren.domain.service.tps.servicerutiner.definition.TpsSkdRequestMeldingDefinition;
import no.nav.tps.forvalteren.domain.service.tps.servicerutiner.definition.resolvers.skdmeldinger.SkdMeldingResolver;
import no.nav.tps.forvalteren.repository.jpa.SkdEndringsmeldingGruppeRepository;
import no.nav.tps.forvalteren.repository.jpa.SkdEndringsmeldingLoggRepository;
import no.nav.tps.forvalteren.repository.jpa.SkdEndringsmeldingRepository;
import no.nav.tps.forvalteren.service.command.exceptions.SkdEndringsmeldingGruppeNotFoundException;
import no.nav.tps.forvalteren.service.command.testdata.skd.SendSkdMeldingTilGitteMiljoer;
import no.nav.tps.forvalteren.service.command.testdata.skd.SkdAddHeaderToSkdMelding;

@Service
public class SendEndringsmeldingGruppeToTps {

    @Autowired
    private MessageProvider messageProvider;

    @Autowired
    private ObjectMapper mapper;

    @Autowired
    private ConvertMeldingFromJsonToText convertMeldingFromJsonToText;

    @Autowired
    private SendSkdMeldingTilGitteMiljoer sendSkdMeldingTilGitteMiljoer;

    @Autowired
    private SkdEndringsmeldingGruppeRepository skdEndringsmeldingGruppeRepository;

    @Autowired
    private SkdEndringsmeldingRepository skdEndringsmeldingRepository;

    @Autowired
    private ConvertJsonToRsMeldingstype convertJsonToRsMeldingstype;

    @Autowired
    private SkdMeldingResolver innvandring;

    @Autowired
    private SkdAddHeaderToSkdMelding skdAddHeaderToSkdMelding;

    @Autowired
    private SkdEndringsmeldingLoggRepository skdEndringsmeldingLoggRepository;

    public void execute(Long gruppeId, String environment) {
        SkdEndringsmeldingGruppe gruppe = skdEndringsmeldingGruppeRepository.findById(gruppeId);
        if (gruppe != null) {
            List<SkdEndringsmelding> skdEndringsmeldinger = skdEndringsmeldingRepository.findAllByGruppe(gruppe);

            List<RsMeldingstype> rsMeldingstyper = skdEndringsmeldinger.stream()
                    .map(melding -> convertJsonToRsMeldingstype.execute(melding))
                    .collect(Collectors.toList());

            TpsSkdRequestMeldingDefinition skdRequestMeldingDefinition = innvandring.resolve();
            for (RsMeldingstype melding : rsMeldingstyper) {
                String skdMelding = convertMeldingFromJsonToText.execute(melding);
                StringBuilder skdMeldingMedHeader = skdAddHeaderToSkdMelding.execute(new StringBuilder(skdMelding));
                sendSkdMeldingTilGitteMiljoer.execute(skdMeldingMedHeader.toString(), skdRequestMeldingDefinition, new HashSet<>(Arrays.asList(environment)));
                SkdEndringsmeldingLogg log = new SkdEndringsmeldingLogg();
                log.setEndringsmelding(skdMelding);
                log.setBeskrivelse(melding.getBeskrivelse());
                log.setEnvironment(environment);
                log.setMeldingsgruppeId(gruppeId);
                skdEndringsmeldingLoggRepository.save(log);
            }

        } else {
            throw new SkdEndringsmeldingGruppeNotFoundException(messageProvider.get(SKD_ENDRINGSMELDING_GRUPPE_NOT_FOUND, gruppeId));
        }

    }

}
