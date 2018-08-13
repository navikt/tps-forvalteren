package no.nav.tps.forvalteren.skdavspilleren.service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import no.nav.tps.forvalteren.domain.service.tps.servicerutiner.definition.TpsSkdRequestMeldingDefinition;
import no.nav.tps.forvalteren.domain.service.tps.servicerutiner.definition.resolvers.skdmeldinger.SkdMeldingResolver;
import no.nav.tps.forvalteren.service.command.testdata.skd.SendSkdMeldingTilGitteMiljoer;
import no.nav.tps.forvalteren.skdavspilleren.domain.jpa.SkdmeldingAvspillerdata;
import no.nav.tps.forvalteren.skdavspilleren.repository.SkdmeldingAvspillerdataRepository;
import no.nav.tps.forvalteren.skdavspilleren.service.requests.StartAvspillingRequest;

@Service
public class SkdAvspillerService {
    
    private SkdmeldingAvspillerdataRepository skdmeldingAvspillerdataRepository;
    private SendSkdMeldingTilGitteMiljoer sendSkdMeldingTilGitteMiljoer;
    private TpsSkdRequestMeldingDefinition skdRequestMeldingDefinition ;

    @Autowired
    public SkdAvspillerService(SkdmeldingAvspillerdataRepository skdmeldingAvspillerdataRepository, SendSkdMeldingTilGitteMiljoer sendSkdMeldingTilGitteMiljoer,
            SkdMeldingResolver innvandring) {
        this.skdmeldingAvspillerdataRepository = skdmeldingAvspillerdataRepository;
        this.sendSkdMeldingTilGitteMiljoer = sendSkdMeldingTilGitteMiljoer;
        this.skdRequestMeldingDefinition = innvandring.resolve();
    }
    
    public void start(StartAvspillingRequest startAvspillingRequest) {
        Set<String> environmentsSet = new HashSet<>();
        environmentsSet.add(startAvspillingRequest.getMiljoe());
        
        //TODO hent gyldige miljøer. sjekk om miljøet i request er gyldig
        List<SkdmeldingAvspillerdata> avspillerdataList = skdmeldingAvspillerdataRepository.findByAvspillergruppeAndOrderBySekvensnummerAsc(startAvspillingRequest.getGruppeId());
        if (avspillerdataList != null && !avspillerdataList.isEmpty()) {
            avspillerdataList.forEach(avspillerdata -> sendSkdMeldingTilGitteMiljoer.execute(avspillerdata.getSkdmelding(), skdRequestMeldingDefinition, environmentsSet));
        } else {
            throw new RuntimeException("Ingen avspillergruppe funnet med gruppeId=" + startAvspillingRequest.getGruppeId());
        }
    }
}
