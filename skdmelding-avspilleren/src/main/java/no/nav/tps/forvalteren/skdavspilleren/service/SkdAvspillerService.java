package no.nav.tps.forvalteren.skdavspilleren.service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import no.nav.tps.forvalteren.domain.service.tps.servicerutiner.definition.TpsSkdRequestMeldingDefinition;
import no.nav.tps.forvalteren.domain.service.tps.servicerutiner.definition.resolvers.skdmeldinger.SkdMeldingResolver;
import no.nav.tps.forvalteren.service.command.testdata.skd.SendSkdMeldingTilGitteMiljoer;
import no.nav.tps.forvalteren.skdavspilleren.common.exceptions.AvspillerDataNotFoundException;
import no.nav.tps.forvalteren.skdavspilleren.domain.jpa.Avspillergruppe;
import no.nav.tps.forvalteren.skdavspilleren.domain.jpa.SkdmeldingAvspillerdata;
import no.nav.tps.forvalteren.skdavspilleren.repository.AvspillergruppeRepository;
import no.nav.tps.forvalteren.skdavspilleren.repository.SkdmeldingAvspillerdataRepository;
import no.nav.tps.forvalteren.skdavspilleren.service.requests.StartAvspillingRequest;

@Service
public class SkdAvspillerService {
    
    private SkdmeldingAvspillerdataRepository skdmeldingAvspillerdataRepository;
    private AvspillergruppeRepository avspillergruppeRepository;
    private SendSkdMeldingTilGitteMiljoer sendSkdMeldingTilGitteMiljoer;
    private TpsSkdRequestMeldingDefinition skdRequestMeldingDefinition ;

    @Autowired
    public SkdAvspillerService(SkdmeldingAvspillerdataRepository skdmeldingAvspillerdataRepository,
            AvspillergruppeRepository avspillergruppeRepository,
            SendSkdMeldingTilGitteMiljoer sendSkdMeldingTilGitteMiljoer,
            SkdMeldingResolver innvandring) {
        this.skdmeldingAvspillerdataRepository = skdmeldingAvspillerdataRepository;
        this.avspillergruppeRepository = avspillergruppeRepository;
        this.sendSkdMeldingTilGitteMiljoer = sendSkdMeldingTilGitteMiljoer;
        this.skdRequestMeldingDefinition = innvandring.resolve();
    }
    
    public void start(StartAvspillingRequest startAvspillingRequest) {
        Set<String> environmentsSet = new HashSet<>();
        environmentsSet.add(startAvspillingRequest.getMiljoe());
        
        //TODO hent gyldige miljøer. sjekk om miljøet i request er gyldig
        List<SkdmeldingAvspillerdata> avspillerdataList = skdmeldingAvspillerdataRepository.findAllByAvspillergruppeIdOrderBySekvensnummerAsc(startAvspillingRequest.getGruppeId());
        if (avspillerdataList == null || avspillerdataList.isEmpty()) {
            throw new AvspillerDataNotFoundException("Ingen avspillergruppe funnet med gruppeId=" + startAvspillingRequest.getGruppeId());
        }
        
        avspillerdataList.forEach(avspillerdata -> sendSkdMeldingTilGitteMiljoer.execute(avspillerdata.getSkdmelding(), skdRequestMeldingDefinition, environmentsSet));
    }
    
    public Iterable<Avspillergruppe> getAllAvspillergrupper() {
        return avspillergruppeRepository.findAll();
    }
    
    public void opprettGruppe(Avspillergruppe avspillergruppe) {
        avspillergruppeRepository.save(avspillergruppe);
    }
}
