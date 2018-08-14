package no.nav.tps.forvalteren.skdavspilleren.service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import no.nav.tps.forvalteren.domain.service.tps.servicerutiner.definition.TpsSkdRequestMeldingDefinition;
import no.nav.tps.forvalteren.domain.service.tps.servicerutiner.definition.resolvers.skdmeldinger.SkdMeldingResolver;
import no.nav.tps.forvalteren.service.command.FilterEnvironmentsOnDeployedEnvironment;
import no.nav.tps.forvalteren.service.command.testdata.skd.impl.SendEnSkdMelding;
import no.nav.tps.forvalteren.skdavspilleren.common.exceptions.AvspillerDataNotFoundException;
import no.nav.tps.forvalteren.skdavspilleren.domain.jpa.SkdmeldingAvspillerdata;
import no.nav.tps.forvalteren.skdavspilleren.repository.SkdmeldingAvspillerdataRepository;
import no.nav.tps.forvalteren.skdavspilleren.service.requests.StartAvspillingRequest;
import no.nav.tps.forvalteren.skdavspilleren.service.response.StartAvspillingResponse;
import no.nav.tps.forvalteren.skdavspilleren.service.response.StatusPaaAvspiltSkdMelding;

@Service
public class SkdAvspillerService {
    
    private SkdmeldingAvspillerdataRepository skdmeldingAvspillerdataRepository;
    private SendEnSkdMelding sendEnSkdMelding;
    private TpsSkdRequestMeldingDefinition skdRequestMeldingDefinition;
    private FilterEnvironmentsOnDeployedEnvironment filterEnvironmentsOnDeployedEnvironment;
    
    @Autowired
    public SkdAvspillerService(SkdmeldingAvspillerdataRepository skdmeldingAvspillerdataRepository,
            SendEnSkdMelding sendEnSkdMelding,
            FilterEnvironmentsOnDeployedEnvironment filterEnvironmentsOnDeployedEnvironment,
            SkdMeldingResolver innvandring) {
        this.skdmeldingAvspillerdataRepository = skdmeldingAvspillerdataRepository;
        this.filterEnvironmentsOnDeployedEnvironment = filterEnvironmentsOnDeployedEnvironment;
        this.sendEnSkdMelding = sendEnSkdMelding;
        this.skdRequestMeldingDefinition = innvandring.resolve();
    }
    
    public StartAvspillingResponse start(StartAvspillingRequest startAvspillingRequest) {
        verifiserMiljo(startAvspillingRequest);
        List<SkdmeldingAvspillerdata> avspillerdataList = skdmeldingAvspillerdataRepository.findAllByAvspillergruppeIdOrderBySekvensnummerAsc(startAvspillingRequest.getGruppeId());
        if (avspillerdataList == null || avspillerdataList.isEmpty()) {
            throw new AvspillerDataNotFoundException("Ingen avspillergruppe funnet med gruppeId=" + startAvspillingRequest.getGruppeId());
        }
        
        StartAvspillingResponse avspillingResponse = new StartAvspillingResponse();
        avspillerdataList.forEach(avspillerdata ->
                sendSkdMeldingAndAddResponseToList(avspillingResponse, avspillerdata, skdRequestMeldingDefinition, startAvspillingRequest.getMiljoe()));
        return avspillingResponse;
    }
    
    private void verifiserMiljo(StartAvspillingRequest startAvspillingRequest) {
        Set<String> environmentsSet = new HashSet<>();
        environmentsSet.add(startAvspillingRequest.getMiljoe());
        Set<String> envToCheck = filterEnvironmentsOnDeployedEnvironment.execute(environmentsSet);
        if (envToCheck.isEmpty()) {
            throw new RuntimeException("The environment " + environmentsSet + " is not available");
        }
    }
    
    private void sendSkdMeldingAndAddResponseToList(StartAvspillingResponse avspillingResponse, SkdmeldingAvspillerdata avspillerdata, TpsSkdRequestMeldingDefinition skdRequestMeldingDefinition, String env) {
        String status = sendEnSkdMelding.sendSkdMelding(avspillerdata.getSkdmelding(), skdRequestMeldingDefinition, env);
        avspillingResponse.incrementAntallSendte();
        if (!"00".equals(status)) {
            rapporterFeiletMelding(avspillerdata, status, avspillingResponse);
        }
    }
    
    private void rapporterFeiletMelding(SkdmeldingAvspillerdata avspillerdata, String status, StartAvspillingResponse avspillingResponse) {
        StatusPaaAvspiltSkdMelding respons = StatusPaaAvspiltSkdMelding.builder()
                .sekvensnummer(avspillerdata.getSekvensnummer())
                .status(status)
                .build();
        avspillingResponse.addStatusFraFeilendeMeldinger(respons);
        avspillingResponse.incrementAntallFeilet();
    }
}
