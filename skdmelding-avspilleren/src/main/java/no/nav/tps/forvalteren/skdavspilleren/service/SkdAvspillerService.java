package no.nav.tps.forvalteren.skdavspilleren.service;

import java.util.ArrayList;
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
import no.nav.tps.forvalteren.skdavspilleren.domain.jpa.Avspillergruppe;
import no.nav.tps.forvalteren.skdavspilleren.domain.jpa.SkdmeldingAvspillerdata;
import no.nav.tps.forvalteren.skdavspilleren.repository.AvspillergruppeRepository;
import no.nav.tps.forvalteren.skdavspilleren.repository.SkdmeldingAvspillerdataRepository;
import no.nav.tps.forvalteren.skdavspilleren.service.requests.StartAvspillingRequest;
import no.nav.tps.forvalteren.skdavspilleren.service.response.AvspiltSkdMeldingTilTpsRespons;

@Service
public class SkdAvspillerService {
    
    private SkdmeldingAvspillerdataRepository skdmeldingAvspillerdataRepository;
    private AvspillergruppeRepository avspillergruppeRepository;
    private SendEnSkdMelding sendEnSkdMelding;
    private TpsSkdRequestMeldingDefinition skdRequestMeldingDefinition;
    private FilterEnvironmentsOnDeployedEnvironment filterEnvironmentsOnDeployedEnvironment;
    
    @Autowired
    public SkdAvspillerService(SkdmeldingAvspillerdataRepository skdmeldingAvspillerdataRepository,
            AvspillergruppeRepository avspillergruppeRepository,
            SendEnSkdMelding sendEnSkdMelding,
            FilterEnvironmentsOnDeployedEnvironment filterEnvironmentsOnDeployedEnvironment,
            SkdMeldingResolver innvandring) {
        this.skdmeldingAvspillerdataRepository = skdmeldingAvspillerdataRepository;
        this.avspillergruppeRepository = avspillergruppeRepository;
        this.filterEnvironmentsOnDeployedEnvironment = filterEnvironmentsOnDeployedEnvironment;
        this.sendEnSkdMelding = sendEnSkdMelding;
        this.skdRequestMeldingDefinition = innvandring.resolve();
    }
    
    public void start(StartAvspillingRequest startAvspillingRequest) {
        verifiserMiljo(startAvspillingRequest);
        List<SkdmeldingAvspillerdata> avspillerdataList = skdmeldingAvspillerdataRepository.findAllByAvspillergruppeIdOrderBySekvensnummerAsc(startAvspillingRequest.getGruppeId());
        if (avspillerdataList == null || avspillerdataList.isEmpty()) {
            throw new AvspillerDataNotFoundException("Ingen avspillergruppe funnet med gruppeId=" + startAvspillingRequest.getGruppeId());
        }
        
        List<AvspiltSkdMeldingTilTpsRespons> tpsResponse = new ArrayList();
        avspillerdataList.forEach(avspillerdata ->
                sendSkdMeldingAndAddResponseToList(tpsResponse, avspillerdata, skdRequestMeldingDefinition, startAvspillingRequest.getMiljoe()));
        //TODO rapportere avspillingen: Multi Exceptions og antall suksessfulle
    }
    
    private void verifiserMiljo(StartAvspillingRequest startAvspillingRequest) {
        Set<String> environmentsSet = new HashSet<>();
        environmentsSet.add(startAvspillingRequest.getMiljoe());
        Set<String> envToCheck = filterEnvironmentsOnDeployedEnvironment.execute(environmentsSet);
        if (envToCheck.isEmpty()) {
            throw new RuntimeException("The environment "+environmentsSet+" is not available");
        }
    }
    
    private void sendSkdMeldingAndAddResponseToList(List<AvspiltSkdMeldingTilTpsRespons> tpsResponse, SkdmeldingAvspillerdata avspillerdata, TpsSkdRequestMeldingDefinition skdRequestMeldingDefinition, String env) {
        String status = sendEnSkdMelding.sendSkdMelding(avspillerdata.getSkdmelding(), skdRequestMeldingDefinition, env);
        AvspiltSkdMeldingTilTpsRespons respons = AvspiltSkdMeldingTilTpsRespons.builder()
                .sekvensnummer(avspillerdata.getSekvensnummer())
                .status(status)
                .build();
        tpsResponse.add(respons);
    }
    
    public Iterable<Avspillergruppe> getAllAvspillergrupper() {
        return avspillergruppeRepository.findAll();
    }
    
    public void opprettGruppe(Avspillergruppe avspillergruppe) {
        avspillergruppeRepository.save(avspillergruppe);
    }
}
