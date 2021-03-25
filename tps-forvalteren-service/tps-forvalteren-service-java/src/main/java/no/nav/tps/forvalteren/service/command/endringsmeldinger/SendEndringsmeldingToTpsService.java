package no.nav.tps.forvalteren.service.command.endringsmeldinger;

import static no.nav.tps.forvalteren.common.message.MessageConstants.SKD_ENDRINGSMELDING_GRUPPE_NOT_FOUND;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import no.nav.tps.forvalteren.common.message.MessageProvider;
import no.nav.tps.forvalteren.domain.jpa.SkdEndringsmelding;
import no.nav.tps.forvalteren.domain.jpa.SkdEndringsmeldingGruppe;
import no.nav.tps.forvalteren.domain.jpa.SkdEndringsmeldingLogg;
import no.nav.tps.forvalteren.domain.rs.skd.RsMeldingstype;
import no.nav.tps.forvalteren.domain.rs.skd.RsSkdEndringsmeldingIdListToTps;
import no.nav.tps.forvalteren.domain.service.tps.servicerutiner.definition.TpsSkdRequestMeldingDefinition;
import no.nav.tps.forvalteren.domain.service.tps.servicerutiner.definition.resolvers.skdmeldinger.SkdMeldingResolver;
import no.nav.tps.forvalteren.repository.jpa.SkdEndringsmeldingGruppeRepository;
import no.nav.tps.forvalteren.repository.jpa.SkdEndringsmeldingLoggRepository;
import no.nav.tps.forvalteren.repository.jpa.SkdEndringsmeldingRepository;
import no.nav.tps.forvalteren.service.command.endringsmeldinger.response.AvspillingResponse;
import no.nav.tps.forvalteren.service.command.exceptions.SkdEndringsmeldingGruppeNotFoundException;
import no.nav.tps.forvalteren.service.command.testdata.skd.SkdAddHeaderToSkdMelding;
import no.nav.tps.forvalteren.service.command.testdata.utils.TpsPacemaker;

@Service
@RequiredArgsConstructor
public class SendEndringsmeldingToTpsService {

    private final MessageProvider messageProvider;
    private final SkdEndringsmeldingGruppeRepository skdEndringsmeldingGruppeRepository;
    private final SkdEndringsmeldingRepository skdEndringsmeldingRepository;
    private final SkdEndringsmeldingLoggRepository skdEndringsmeldingLoggRepository;
    private final ConvertMeldingFromJsonToText convertMeldingFromJsonToText;
    private final SendSkdMeldingerOgLeggTilResponslisteService sendSkdMeldinger;
    private final ConvertJsonToRsMeldingstype convertJsonToRsMeldingstype;
    private final SkdMeldingResolver innvandring;
    private final SkdAddHeaderToSkdMelding skdAddHeaderToSkdMelding;
    private final TpsPacemaker tpsPacemaker;

    public AvspillingResponse execute(Long skdMeldingsGruppeId, RsSkdEndringsmeldingIdListToTps skdEndringsmeldingIdListToTps) {
        SkdEndringsmeldingGruppe gruppe = skdEndringsmeldingGruppeRepository.findById(skdMeldingsGruppeId);
        AvspillingResponse avspillingResponse = new AvspillingResponse();
        if (gruppe != null) {
            List<SkdEndringsmelding> skdEndringsmeldinger = new ArrayList<>();
            String environment = skdEndringsmeldingIdListToTps.getEnvironment();
            List<Long> idList = skdEndringsmeldingIdListToTps.getIds();

            for (Long id : idList) {
                Optional<SkdEndringsmelding> skdEndringsmelding = skdEndringsmeldingRepository.findById(id);
                if (skdEndringsmelding.isPresent()) {
                    skdEndringsmeldinger.add(skdEndringsmelding.get());
                }
            }

            List<RsMeldingstype> rsMeldingstyper = skdEndringsmeldinger.stream()
                    .map(melding -> convertJsonToRsMeldingstype.execute(melding))
                    .collect(Collectors.toList());

            TpsSkdRequestMeldingDefinition skdRequestMeldingDefinition = innvandring.resolve();
            for (int i = 0; i < rsMeldingstyper.size(); i++) {
                RsMeldingstype melding = rsMeldingstyper.get(i);

                String skdMelding = convertMeldingFromJsonToText.execute(melding);
                String sekvensnummer = melding.getSekvensnr();
                String foedselsnummer = skdMelding.substring(0, 11);
                StringBuilder skdMeldingMedHeader = skdAddHeaderToSkdMelding.execute(new StringBuilder(skdMelding));
                sendSkdMeldinger.sendSkdMeldingAndAddResponseToList(avspillingResponse, skdMeldingMedHeader.toString(), skdRequestMeldingDefinition, environment, foedselsnummer, sekvensnummer);
                saveLogg(skdMelding, melding, skdMeldingsGruppeId, environment);

                tpsPacemaker.iteration(i);
            }
        } else {
            throw new SkdEndringsmeldingGruppeNotFoundException(messageProvider.get(SKD_ENDRINGSMELDING_GRUPPE_NOT_FOUND, skdMeldingsGruppeId));
        }
        return avspillingResponse;
    }

    public AvspillingResponse sendHeleGruppen(Long skdMeldingsGruppeId, RsSkdEndringsmeldingIdListToTps request) {

        var avspillingResponse = new AvspillingResponse();
        var skdRequestMeldingDefinition = innvandring.resolve();

        int pageNo = 0;
        var pacemaker = new AtomicLong(0);
        Page<SkdEndringsmelding> meldinger = skdEndringsmeldingRepository.findAllByGruppeIdOrderByIdAsc(skdMeldingsGruppeId,
                PageRequest.of(pageNo, 10));

        do {
            meldinger.getContent().stream()
                    .map(convertJsonToRsMeldingstype::execute)
                    .forEach(melding -> {

                        var skdMelding = convertMeldingFromJsonToText.execute(melding);
                        sendSkdMeldinger.sendSkdMeldingAndAddResponseToList(avspillingResponse,
                                skdAddHeaderToSkdMelding.execute(new StringBuilder(skdMelding)).toString(),
                                skdRequestMeldingDefinition,
                                request.getEnvironment(),
                                skdMelding.substring(1,11),
                                melding.getSekvensnr());
                        tpsPacemaker.iteration(pacemaker.addAndGet(1));
                    });

            meldinger = skdEndringsmeldingRepository.findAllByGruppeIdOrderByIdAsc(skdMeldingsGruppeId,
                    PageRequest.of(++pageNo, 10));

        }  while (pageNo < meldinger.getTotalPages());

        return avspillingResponse;
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
