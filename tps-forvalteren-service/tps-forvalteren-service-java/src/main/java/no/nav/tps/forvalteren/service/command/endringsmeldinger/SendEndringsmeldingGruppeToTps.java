package no.nav.tps.forvalteren.service.command.endringsmeldinger;

import static no.nav.tps.forvalteren.common.java.message.MessageConstants.SKD_ENDRINGSMELDING_GRUPPE_NOT_FOUND;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

import no.nav.tps.forvalteren.domain.rs.skd.RsSkdEndringsmeldingIdListToTps;
import no.nav.tps.forvalteren.service.command.testdata.skd.SkdAddHeaderToSkdMelding;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

@Service
public class SendEndringsmeldingGruppeToTps {

    @Autowired
    private MessageProvider messageProvider;

    @Autowired
    private SkdEndringsmeldingGruppeRepository skdEndringsmeldingGruppeRepository;

    @Autowired
    private SkdEndringsmeldingRepository skdEndringsmeldingRepository;

    @Autowired
    private SkdEndringsmeldingLoggRepository skdEndringsmeldingLoggRepository;

    @Autowired
    private ConvertMeldingFromJsonToText convertMeldingFromJsonToText;

    @Autowired
    private SendSkdMeldingTilGitteMiljoer sendSkdMeldingTilGitteMiljoer;

    @Autowired
    private ConvertJsonToRsMeldingstype convertJsonToRsMeldingstype;

    @Autowired
    private SkdMeldingResolver innvandring;

    @Autowired
    private SkdAddHeaderToSkdMelding skdAddHeaderToSkdMelding;


    public void execute(Long skdMeldingsGruppeId, RsSkdEndringsmeldingIdListToTps skdEndringsmeldingIdListToTps) {
        SkdEndringsmeldingGruppe gruppe = skdEndringsmeldingGruppeRepository.findById(skdMeldingsGruppeId);
        if(gruppe != null) {
            List<SkdEndringsmelding> skdEndringsmeldinger = new ArrayList<>();
            String environment = skdEndringsmeldingIdListToTps.getEnvironment();
            List<Long> idList = skdEndringsmeldingIdListToTps.getIds();

            for(Long id : idList) {
                SkdEndringsmelding skdEndringsmelding = skdEndringsmeldingRepository.findById(id);
                if(skdEndringsmelding != null){
                    skdEndringsmeldinger.add(skdEndringsmelding);
                }
            }

            List<RsMeldingstype> rsMeldingstyper = skdEndringsmeldinger.stream()
                    .map(melding -> convertJsonToRsMeldingstype.execute(melding))
                    .collect(Collectors.toList());

            TpsSkdRequestMeldingDefinition skdRequestMeldingDefinition = innvandring.resolve();
            for (RsMeldingstype melding : rsMeldingstyper) {
                String skdMelding = convertMeldingFromJsonToText.execute(melding);
                StringBuilder skdMeldingMedHeader = skdAddHeaderToSkdMelding.execute(new StringBuilder(skdMelding));
                sendSkdMeldingTilGitteMiljoer.execute(skdMeldingMedHeader.toString(), skdRequestMeldingDefinition, new HashSet<>(Arrays.asList(environment)));
                saveLogg(skdMelding, melding, skdMeldingsGruppeId, environment);
             }

        } else {
            throw new SkdEndringsmeldingGruppeNotFoundException(messageProvider.get(SKD_ENDRINGSMELDING_GRUPPE_NOT_FOUND, skdMeldingsGruppeId));
        }
    }

    private void saveLogg(String skdMelding, RsMeldingstype melding, Long skdMeldingsGruppeId, String environment) {
        SkdEndringsmeldingLogg log = new SkdEndringsmeldingLogg();
        log.setEndringsmelding(skdMelding);
        log.setBeskrivelse(melding.getBeskrivelse());
        log.setEnvironment(environment);
        log.setMeldingsgruppeId(skdMeldingsGruppeId);
        skdEndringsmeldingLoggRepository.save(log);
    }
}
