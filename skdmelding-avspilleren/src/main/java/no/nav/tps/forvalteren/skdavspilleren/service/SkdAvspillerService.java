package no.nav.tps.forvalteren.skdavspilleren.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import no.nav.tps.forvalteren.skdavspilleren.repository.AvspillergruppeRepository;
import no.nav.tps.forvalteren.skdavspilleren.service.requests.StartAvspillingRequest;

@Service
public class SkdAvspillerService {
    
    @Autowired
    private AvspillergruppeRepository avspillergruppeRepository;
    
    public void start(StartAvspillingRequest startAvspillingRequest) {
        avspillergruppeRepository.findById(startAvspillingRequest.getGruppeId());
    }
}
